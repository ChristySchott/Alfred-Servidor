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
                String sql = "insert into categoria (nomeCategoria) values (?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ct.getNomeCategoria());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    con.rollback();
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                return e.getErrorCode();
            }
        }
    }

    public int excluir(Categoria ct) {
        
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "delete from categoria where codCategoria = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, ct.getCodCategoria());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    con.rollback(); 
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                return e.getErrorCode();
            }
        }
    }

    public int alterar(Categoria ct) {
        
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update categoria set nomeCategoria = ? where codCategoria = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ct.getNomeCategoria());
                stmt.setInt(2, ct.getCodCategoria());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    con.rollback(); 
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    return ex.getErrorCode();
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                return e.getErrorCode();
            }
        }
    }

    public ArrayList<Categoria> getListaCategorias() {
        
        Statement stmt = null;
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();

        try {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("select * from categoria");

            while (res.next()) {
                Categoria ct = new Categoria(res.getInt("codCategoria"),
                        res.getString("nomeCategoria"));
                listCategorias.add(ct);
            }
            res.close();
            stmt.close();
            con.close();
            return listCategorias;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }

    }

    public ArrayList<Categoria> getListaCategoriasNome(String nome) {
        
        Statement stmt = null;
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();

        try {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("select * from categoria where nomeCategoria like '%" + nome + "%'");

            while (res.next()) {
                Categoria ct = new Categoria(res.getInt("codCategoria"),
                        res.getString("nomeCategoria"));
                listCategorias.add(ct);
            }
            res.close();
            stmt.close();
            con.close();
            
            return listCategorias;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }

    }
}
