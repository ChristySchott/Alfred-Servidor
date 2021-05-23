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
import modelDominio.Avaliacao;
import modelDominio.Categoria;
import modelDominio.Cidade;
import modelDominio.Cliente;
import modelDominio.Empresa;
import modelDominio.Estado;
import modelDominio.Prato;
import modelDominio.Usuario;

/**
 *
 * @author be_ha
 */
public class EmpresaDao {

    private Connection con;

    public EmpresaDao() {
        this.con = Conector.getConnection();
    }

    public int inserir(Empresa empresa) {
        PreparedStatement stmt = null;

        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into empresa (nomeEmpresa, cnpjEmpresa, codUsuario) values (?, ?, ?);";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, empresa.getNomeEmpresa());
                stmt.setString(2, empresa.getCnpjEmpresa());
                stmt.setInt(3, empresa.getCodUsuario());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Empresa");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Empresa");
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
                System.out.println("Erro ao fechar operação - inserir Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    public int alterar(Empresa emp) {
        PreparedStatement stmt = null;
        
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update empresa set \n"
                        + "nomeEmpresa = ?, \n"
                        + "cnpjEmpresa = ?, \n"
                        + "imagemEmpresa = ?, \n"
                        + "codCategoria = ? \n"
                        + "where codEmpresa = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, emp.getNomeEmpresa());
                stmt.setString(2, emp.getCnpjEmpresa());
                stmt.setBytes(3, emp.getImagemEmpresa());
                stmt.setInt(4, emp.getCategoriaEmpresa().getCodCategoria());
                stmt.setInt(5, emp.getCodEmpresa());
                
                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Empresa");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Empresa");
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
                System.out.println("Erro ao fechar operação - alterar Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    public int abrirFecharEmpresa(Empresa emp) {
        PreparedStatement stmt = null;
        
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update empresa set \n"
                        + "abertoFechadoEmpresa = ? \n"
                        + "where codEmpresa = ?";
                stmt = con.prepareStatement(sql);
                stmt.setBoolean(1, emp.getAbertoFechadoEmpresa());
                stmt.setInt(2, emp.getCodEmpresa());
                
                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução abreFechaEmpresa ");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - abreFechaEmpresa");
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
                System.out.println("Erro ao fechar operação - abreFechaEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    // Mudar statment - com autoCommit
    public boolean empresaExiste(Empresa empresa) {
        PreparedStatement stmt = null;
        boolean existe = false;

        try {
            try {

                String sql = "select exists(\n"
                        + "select * from empresa \n"
                        + "join usuario on usuario.codUsuario = empresa.codUsuario\n"
                        + "where cnpjEmpresa=? or usuario.emailUsuario = ?\n"
                        + ") as existente;";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, empresa.getCnpjEmpresa());
                stmt.setString(2, empresa.getEmailUsuario());

                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    existe = (res.getInt("existente") == 1);
                }

                res.close();

                return existe;
            } catch (SQLException e) {
                System.out.println("Erro execução empresaExiste");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return false;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - empresaExiste");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return false;
            }
        }
    }

    public Empresa efetuarLogin(Usuario usuario) {
        PreparedStatement stmt = null;
        Empresa empresaSelecionada = null;

        try {
            try {
                String sql = "select *, AVG(prato.valorPrato) as precoMedioEmpresa from empresa \n"
                        + "join usuario on usuario.codUsuario = empresa.codUsuario \n"
                        + "left join categoria on (empresa.codCategoria IS NOT NULL AND  categoria.codCategoria = empresa.codCategoria)\n"
                        + "left join cidade on (usuario.codCidade IS NOT NULL AND cidade.codCidade = usuario.codCidade)\n"
                        + "left join estado on (usuario.codEstado IS NOT NULL AND estado.codEstado= usuario.codEstado)\n"
                        + "left join avaliacao on (avaliacao.codEmpresa IS NOT NULL AND avaliacao.codEmpresa = empresa.codEmpresa)\n"
                        + "left join prato on prato.codEmpresa = empresa.codEmpresa\n"
                        + " where emailUsuario = ? and senhaUsuario = ?;";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usuario.getEmailUsuario());
                stmt.setString(2, usuario.getSenhaUsuario());

                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    //Talvez testar antes
                    Categoria cat = new Categoria(res.getInt("codCategoria"), res.getString("nomeCategoria"));
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    Cliente cli = new Cliente(res.getInt("codCliente"));
                    Avaliacao avl = new Avaliacao(res.getInt("codAvaliacao"), res.getString("descricaoAvaliacao"), res.getInt("notaAvaliacao"), cli);

                    empresaSelecionada = new Empresa(
                            res.getInt("codEmpresa"),
                            res.getString("nomeEmpresa"),
                            res.getString("cnpjEmpresa"),
                            res.getBoolean("abertoFechadoEmpresa"),
                            cat,
                            res.getBytes("imagemEmpresa"),
                            avl,
                            res.getDouble("precoMedioEmpresa"),
                            res.getInt("codUsuario"),
                            res.getString("emailUsuario"),
                            res.getString("senhaUsuario"),
                            cid,
                            est,
                            res.getString("ruaUsuario"),
                            res.getString("bairroUsuario"),
                            res.getString("complementoUsuario"),
                            res.getInt("numeroUsuario")
                    );
                }
                
                res.close();
                
                return empresaSelecionada;
            } catch (SQLException e) {
                System.out.println("Erro execução efetuarLogin Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - EfetuarLogin Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

    public ArrayList<Empresa> getListaEmpresas(String nome, String codCategoria) {
        Statement stmt = null;
        ArrayList<Empresa> listaEmpresas = new ArrayList<Empresa>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select *,  AVG(prato.valorPrato) as precoMedioEmpresa from empresa\n"
                        + "inner join categoria on (categoria.codCategoria = empresa.codCategoria) \n"
                        + "join usuario on usuario.codUsuario = empresa.codUsuario \n"
                        + "left join cidade on (usuario.codCidade IS NOT NULL AND cidade.codCidade = usuario.codCidade)\n"
                        + "left join estado on (usuario.codEstado IS NOT NULL AND estado.codEstado= usuario.codEstado)\n"
                        + "left join avaliacao on (avaliacao.codEmpresa IS NOT NULL AND avaliacao.codEmpresa = empresa.codEmpresa) \n"
                        + "left join prato on prato.codEmpresa = empresa.codEmpresa\n"
                        + "where nomeEmpresa like '%" + nome + "%' and categoria.codCategoria like '%" + codCategoria + "%' group by empresa.codEmpresa");

                while (res.next()) {
                    Categoria cat = new Categoria(res.getInt("codCategoria"), res.getString("nomeCategoria"));
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    Cliente cli = new Cliente(res.getInt("codCliente"));
                    Avaliacao avl = new Avaliacao(res.getInt("codAvaliacao"), res.getString("descricaoAvaliacao"), res.getInt("notaAvaliacao"), cli);
                    
                    Empresa empresa = new Empresa(
                            res.getInt("codEmpresa"),
                            res.getString("nomeEmpresa"),
                            res.getBoolean("abertoFechadoEmpresa"),
                            cat,
                            avl,
                            res.getDouble("precoMedioEmpresa"),
                            res.getBytes("imagemEmpresa"),
                            cid,
                            est,
                            res.getString("ruaUsuario")
                    );
                    listaEmpresas.add(empresa);
                }
                res.close();
                stmt.close();
                return listaEmpresas;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaEmpresasAbertas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaEmpresasAbertas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public double buscarPrecoMedioEmpresa(int codEmpresa) {
        Statement stmt = null;
        double precoMedio = 0;

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select AVG(prato.valorPrato) as precoMedioEmpresa from prato where prato.codEmpresa = " + codEmpresa);

                while (res.next()) {
                    precoMedio = res.getDouble("precoMedioEmpresa");
                }
                
                res.close();
                return precoMedio;
            } catch (SQLException e) {
                System.out.println("Erro execução buscarPrecoMedioEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return precoMedio;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - buscarPrecoMedioEmpresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return precoMedio;
            }
        }
    }

}
