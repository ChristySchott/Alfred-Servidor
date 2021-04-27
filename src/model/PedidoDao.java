/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelDominio.Cidade;
import modelDominio.Cliente;
import modelDominio.Estado;
import modelDominio.Pedido;
import modelDominio.PratoPedido;

/**
 *
 * @author christy
 */
public class PedidoDao {

    private Connection con;

    public PedidoDao() {
        this.con = Conector.getConnection();
    }

    public int inserir(Pedido pedido) {
        PreparedStatement stmt = null;

        System.out.println("iniciou");

        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into pedido (codCliente, codEmpresa) values (?, ?);";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, pedido.getCliente().getCodCliente());
                stmt.setInt(2, pedido.getEmpresa().getCodEmpresa());

                System.out.println("inseriu");

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro na execução inserir Pedido");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Pedido");
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - inserir Pedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int alterar(Pedido pedido) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "update pedido set \n"
                        + "statusPedido = ?, \n"
                        + "observacaoPedido = ?, \n"
                        + "formaPagamentoPedido = ? \n"
                        + "where codPedido = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, pedido.getStatusPedido());
                stmt.setString(2, pedido.getObservacaoPedido());
                stmt.setInt(3, pedido.getFormaPagamentoPedido());
                stmt.setInt(4, pedido.getCodPedido());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Pedido");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Pedido");
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - alterar Pedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int excluir(int codPedido) {
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "DELETE pratoPedido , pedido  FROM pratoPedido  INNER JOIN pedido  \n" +
"WHERE pratoPedido.codPedido= pedido.codPedido and pratoPedido.codPedido = '"+ codPedido + "'";
                stmt = con.prepareStatement(sql);

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução excluir Pedido");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - excluir Pedido");
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - excluir Prato");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int getCodPedido() {
        Statement stmt = null;
        int codPedido = 0;

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select * from pedido where updatedAt = (SELECT max(updatedAt) from pedido) and codPedido = (SELECT max(codPedido) from pedido);");

                while (res.next()) {
                    codPedido = res.getInt("codPedido");
                }

                res.close();
                stmt.close();
                con.close();
                return codPedido;
            } catch (SQLException e) {
                System.out.println("Erro execução getCodPedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return 0;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getCodPedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return 0;
            }
        }
    }

    public ArrayList<Pedido> getListaPedidosAnaliseCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosAnalise = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select pedido.*, cliente.*, empresa.* from pedido\n"
                        + "inner join cliente on (cliente.codCliente = pedido.codPedido) \n"
                        + "inner join empresa on (empresa.codEmpresa = pedido.codEmpresa) \n"
                        + "where statusPedido = 0 and codUsuario = ");

                while (res.next()) {
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codCliente"),
                            res.getInt("codEmpresa")
                    );
                    listaPedidosAnalise.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosAnalise;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAnalise");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAnalise");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

    public ArrayList<Pedido> getListaPedidosAprovadosCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosAprovados = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();

                ResultSet res = stmt.executeQuery("select empresa.*, categoria.nomeCategoria, avaliacao.notaAvaliacao from empresa\n"
                        + "inner join categoria on (categoria.codCategoria = empresa.codCategoria) \n"
                        + "inner join avaliacao on (avaliacao.codAvaliacao = empresa.codAvaliacao) \n"
                        + "where abertoFechadoEmpresa = true");

                while (res.next()) {
                    
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codCliente"),
                            res.getInt("codEmpresa")
                    );
                    listaPedidosAprovados.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosAprovados;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

    public ArrayList<Pedido> getListaPedidosReprovadosCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosReprovados = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();

                ResultSet res = stmt.executeQuery("select empresa.*, categoria.nomeCategoria, avaliacao.notaAvaliacao from empresa\n"
                        + "inner join categoria on (categoria.codCategoria = empresa.codCategoria) \n"
                        + "inner join avaliacao on (avaliacao.codAvaliacao = empresa.codAvaliacao) \n"
                        + "where abertoFechadoEmpresa = true");

                while (res.next()) {
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codCliente"),
                            res.getInt("codEmpresa")
                    );
                    listaPedidosReprovados.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosReprovados;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosReprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosReprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Pedido> getListaPedidosAnaliseEmpresa(int codEmpresa) {
        PreparedStatement stmt = null;
        ArrayList<Pedido> listaPedidosAnalise = new ArrayList<Pedido>();

        try {
            try {
                String sql = "select *, sum(pratopedido.valorUnPratoPedido * pratopedido.quantidadePratoPedido) as valorTotal from pedido\n" +
                            "join cliente on (cliente.codCliente = pedido.codCliente) \n" +
                            "join usuario on (usuario.codUsuario = cliente.codUsuario) \n" +
                            "join cidade on (cidade.codCidade = usuario.codCidade)\n" +
                            "join estado on (estado.codEstado = usuario.codEstado)\n" +
                            "join empresa on (empresa.codEmpresa = pedido.codEmpresa)\n" +
                            "join pratopedido on (pratopedido.codPedido = pedido.codPedido)\n" +
                            "where statusPedido = 0 and empresa.codEmpresa = ? group by pedido.codPedido;";
                
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, codEmpresa);
                
                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    Cliente cli = new Cliente(
                            res.getString("nomeCliente"), 
                            res.getString("sobrenomeCliente"), 
                            res.getBytes("imagemCliente"), 
                            cid, 
                            est, 
                            res.getString("ruaUsuario"), 
                            res.getString("bairroUsuario"), 
                            res.getString("complementoUsuario")
                    );
                    
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            cli,
                            res.getDouble("valorTotal")
                    );
                    listaPedidosAnalise.add(pedido);
                }
                res.close();
                return listaPedidosAnalise;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAnaliseEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAnaliseEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Pedido> getListaPedidosAprovadosEmpresa(int codEmpresa) {
        PreparedStatement stmt = null;
        ArrayList<Pedido> listaPedidosAnalise = new ArrayList<Pedido>();

        try {
            try {
                String sql = "select *, sum(pratopedido.valorUnPratoPedido * pratopedido.quantidadePratoPedido) as valorTotal from pedido\n" +
                            "join cliente on (cliente.codCliente = pedido.codCliente) \n" +
                            "join usuario on (usuario.codUsuario = cliente.codUsuario) \n" +
                            "join cidade on (cidade.codCidade = usuario.codCidade)\n" +
                            "join estado on (estado.codEstado = usuario.codEstado)\n" +
                            "join empresa on (empresa.codEmpresa = pedido.codEmpresa)\n" +
                            "join pratopedido on (pratopedido.codPedido = pedido.codPedido)\n" +
                            "where statusPedido = 1 and empresa.codEmpresa = ? group by pedido.codPedido;";
                
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, codEmpresa);
                
                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    Cliente cli = new Cliente(
                            res.getString("nomeCliente"), 
                            res.getString("sobrenomeCliente"), 
                            res.getBytes("imagemCliente"), 
                            cid, 
                            est, 
                            res.getString("ruaUsuario"), 
                            res.getString("bairroUsuario"), 
                            res.getString("complementoUsuario")
                    );
                    
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            cli,
                            res.getDouble("valorTotal")
                    );
                    listaPedidosAnalise.add(pedido);
                }
                res.close();
                return listaPedidosAnalise;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAprovadosEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAprovadosEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Pedido> getListaPedidosReprovadosEmpresa(int codEmpresa) {
        PreparedStatement stmt = null;
        ArrayList<Pedido> listaPedidosAnalise = new ArrayList<Pedido>();

        try {
            try {
                String sql = "select *, sum(pratopedido.valorUnPratoPedido * pratopedido.quantidadePratoPedido) as valorTotal from pedido\n" +
                            "join cliente on (cliente.codCliente = pedido.codCliente) \n" +
                            "join usuario on (usuario.codUsuario = cliente.codUsuario) \n" +
                            "join cidade on (cidade.codCidade = usuario.codCidade)\n" +
                            "join estado on (estado.codEstado = usuario.codEstado)\n" +
                            "join empresa on (empresa.codEmpresa = pedido.codEmpresa)\n" +
                            "join pratopedido on (pratopedido.codPedido = pedido.codPedido)\n" +
                            "where statusPedido = 2 and empresa.codEmpresa = ? group by pedido.codPedido;";
                
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, codEmpresa);
                
                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    Cliente cli = new Cliente(
                            res.getString("nomeCliente"), 
                            res.getString("sobrenomeCliente"), 
                            res.getBytes("imagemCliente"), 
                            cid, 
                            est, 
                            res.getString("ruaUsuario"), 
                            res.getString("bairroUsuario"), 
                            res.getString("complementoUsuario")
                    );
                    
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            cli,
                            res.getDouble("valorTotal")
                    );
                    listaPedidosAnalise.add(pedido);
                }
                res.close();
                return listaPedidosAnalise;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosReprovadosEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosReprovadosEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
}
