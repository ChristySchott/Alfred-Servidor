package model;

import modelDominio.Categoria;
import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoriaDao {

    private Connection con;

    public CategoriaDao() {
        con = Conector.getConnection();
    }

    public int inserir(Categoria ct) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "insert into categoria (nomeCategoria, imagemCategoria) values (?, ?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ct.getNomeCategoria());
                stmt.setBytes(2, ct.getImagemCategoria());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Categoria");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Categoria");
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
                System.out.println("Erro ao fechar operação - inserir Categoria");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    public int alterar(Categoria ct) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "update categoria set \n"
                        + "nomeCategoria = ?, \n"
                        + "imagemCategoria = ?, \n"
                        + "where codCategoria = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ct.getNomeCategoria());
                stmt.setBytes(2, ct.getImagemCategoria());
                stmt.setInt(3, ct.getCodCategoria());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Categoria");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Categoria");
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
                System.out.println("Erro ao fechar operação - alterar Categoria");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public ArrayList<Categoria> getListaCategorias() {

        Statement stmt = null;
        ArrayList<Categoria> listCategorias = new ArrayList<>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select * from categoria");

                while (res.next()) {
                    Categoria ct = new Categoria(res.getInt("codCategoria"),
                            res.getString("nomeCategoria"),
                            res.getBytes("imagemCategoria"));
                    listCategorias.add(ct);
                }
                res.close();
                System.out.println(listCategorias);
                return listCategorias;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaCategoria");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaCategoria");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }

    }

    // Talvez não precise
    public ArrayList<Categoria> getListaCategoriasNome(String nome) {

        Statement stmt = null;
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        
        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select * from categoria where nomeCategoria like '%" + nome + "%'");

                while (res.next()) {
                    Categoria ct = new Categoria(res.getInt("codCategoria"),
                            res.getString("nomeCategoria"),
                            res.getBytes("imagemCategoria"));
                    listCategorias.add(ct);
                }
                res.close();
                return listCategorias;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaCategoriaNome");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaCategoriaNome");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }

    }
}
