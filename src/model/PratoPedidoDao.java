package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                stmt.setInt(2, pratoPedido.getCodPedido());

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
}
