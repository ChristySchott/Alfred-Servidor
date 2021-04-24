package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.PratoPedido;

public class PratoPedidoDao {
    private Connection con;

    public PratoPedidoDao() {
        this.con = Conector.getConnection();
    }
    
    public int inserir(PratoPedido pratoPedido) {
        PreparedStatement stmt = null;

        System.out.println("iniciou");

        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into pratoPedido (quantidadePratoPedido, valorUnPratoPedido, codPedido) values (?, ?, ?);";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, pratoPedido.getQuantidadePratoPedido());
                stmt.setDouble(2, pratoPedido.getValorUnidadePratoPedido());
                stmt.setInt(3, pratoPedido.getCodPedido());

                System.out.println("inseriu");

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro na execução inserir PratoPedido");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir PratoPedido");
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
                System.out.println("Erro ao fechar operação - inserir PratoPedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
     public int excluir(PratoPedido pratoPedido) {
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "delete from pratoPedido where codPratoPedido = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, pratoPedido.getCodPratoPedido());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução excluir PratoPedido");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - excluir PratoPedido");
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
                System.out.println("Erro ao fechar operação - excluir PratoPedido");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
     
     public ArrayList<PratoPedido> getListaPedidosCarrinho(int codPedido) {
         Statement stmt = null;
         ArrayList<PratoPedido> listaPratosPedido = new ArrayList<PratoPedido>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select * from pratoPedido where codPedido = " + codPedido + " ;");

                while (res.next()) {
                    PratoPedido pedido = new PratoPedido(
                            res.getInt("codPratoPedido"),
                            res.getInt("quantidadePratoPedido"),
                            res.getDouble("valorUnPratoPedido")
                    );
                    listaPratosPedido.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPratosPedido;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosCarrinho");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosCarrinho");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
}
