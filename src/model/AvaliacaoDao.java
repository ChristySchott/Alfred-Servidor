package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.Avaliacao;


// TODO - VERIFICAR O JOIN, COMO SÓ PASSAMOS O CODCLIENTE E CODEMPRESA
public class AvaliacaoDao {
    private Connection con;

    public AvaliacaoDao() {
        con = Conector.getConnection();
    }

    public int inserir(Avaliacao avl) {
        
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                  
                String sql = "insert into endereco (descricaoAvaliacao,notaAvaliacao,codCliente,codEmpresa) values (?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, avl.getDescricaoAvaliacao());
                stmt.setInt(2, avl.getNotaAvaliacao());
                stmt.setInt(3, avl.getCodCliente());
                stmt.setInt(4, avl.getCodEmpresa());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Avaliacao");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Avaliacao");
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
                System.out.println("Erro ao fechar operação - inserir Avaliacao");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public ArrayList<Avaliacao> getListaAvaliacoes() {
        
        Statement stmt = null;
        ArrayList<Avaliacao> listAvaliacoes = new ArrayList<Avaliacao>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select * from avaliacao");

                while (res.next()) {
                    Avaliacao avl = new Avaliacao(res.getInt("codAvaliacao"));
                    listAvaliacoes.add(avl);
                }

                res.close();
                stmt.close();
                con.close();

                return listAvaliacoes;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaAvaliacoes");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaAvaliacoes");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }

    }
}
