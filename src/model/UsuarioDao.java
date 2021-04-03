package model;

import modelDominio.Usuario;
import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.UsuarioComum;

public class UsuarioDao {

    private Connection con;

    public UsuarioDao() {
        con = Conector.getConnection();
    }

    public int inserir(Usuario usr) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into usuario (emailUsuario,senhaUsuario) values (?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usr.getEmailUsuario());
                stmt.setString(2, usr.getSenhaUsuario());

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

    public int excluir(Usuario usr) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "delete from usuario where codUsuario = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, usr.getCodUsuario());

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

    public int alterar(Usuario usr) {

        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update usuario set \n"
                        + "emailUsuario = ?, \n"
                        + "senhaUsuario = ?, \n"
                        + "where codUsuario = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usr.getEmailUsuario());
                stmt.setString(2, usr.getSenhaUsuario());
                stmt.setInt(3, usr.getCodUsuario());

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

    public Usuario efetuarLogin(Usuario usr) {
        PreparedStatement stmt = null; // usado para rodar SQL
        Usuario usrselecionado = null;

        try {
            String sql = " select * from usuario where emailUsuario = ? and senhaUsuario = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, usr.getEmailUsuario());
            stmt.setString(2, usr.getSenhaUsuario());

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                usrselecionado = new UsuarioComum(res.getInt("codUsuario"),
                        res.getString("emailUsuario"),
                        res.getString("senhaUsuario"));

            }

            res.close();
            stmt.close();
            con.close();

            return usrselecionado;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }

    }

    public ArrayList<Usuario> getListaUsuarios() {

        Statement stmt = null;
        ArrayList<Usuario> listUsuarios = new ArrayList<Usuario>();

        try {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("select * from usuario");

            while (res.next()) {
                Usuario rc = new Usuario(res.getInt("codUsuario"));
                listUsuarios.add(rc);
            }

            res.close();
            stmt.close();
            con.close();

            return listUsuarios;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }

    }
}
