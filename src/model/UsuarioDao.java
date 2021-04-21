package model;

import modelDominio.Usuario;
import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
                System.out.println(usr);
                String sql = "insert into usuario (emailUsuario,senhaUsuario) values (?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usr.getEmailUsuario());
                stmt.setString(2, usr.getSenhaUsuario());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Usuario");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Usuario");
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
                System.out.println("Erro ao fechar operação - inserir Usuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int alterar(Usuario usr) {
        PreparedStatement stmt = null;
        System.out.println("USUario" + usr.toString());
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update usuario set \n"
                        + "emailUsuario = ?, \n"
                        + "senhaUsuario = ?, \n"
                        + "codCidade = ?, \n"
                        + "codEstado = ?, \n"
                        + "ruaUsuario = ?, \n"
                        + "bairroUsuario = ?, \n"
                        + "complementoUsuario = ?, \n"
                        + "numeroUsuario = ? \n"
                        + "where codUsuario = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usr.getEmailUsuario());
                stmt.setString(2, usr.getSenhaUsuario());
                stmt.setInt(3, usr.getCidadeUsuario().getCodCidade());
                stmt.setInt(4, usr.getEstadoUsuario().getCodEstado());
                stmt.setString(5, usr.getRuaUsuario());
                stmt.setString(6, usr.getBairroUsuario());
                stmt.setString(7, usr.getComplementoUsuario());
                stmt.setInt(8, usr.getNumeroUsuario());
                stmt.setInt(9, usr.getCodUsuario());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Usuario");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Usuario");
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
                System.out.println("Erro ao fechar operação - alterar Usuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public Usuario buscarUsuario(Usuario usr) {
        PreparedStatement stmt = null;
        Usuario usrselecionado = null;

        try {
            try {
                String sql = "select * from usuario where emailUsuario = ? and senhaUsuario = ? ";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usr.getEmailUsuario());
                stmt.setString(2, usr.getSenhaUsuario());

                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    usrselecionado = new Usuario(res.getInt("codUsuario"),
                            res.getString("emailUsuario"),
                            res.getString("senhaUsuario"));

                }

                res.close();

                return usrselecionado;
            } catch (SQLException e) {
                System.out.println("Erro execução buscarUsuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - buscarUsuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public Usuario buscarUsuarioPorEmail(String email) {
        PreparedStatement stmt = null;
        Usuario usrselecionado = null;

        try {
            try {
                String sql = "select * from usuario where emailUsuario = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, email);

                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    usrselecionado = new Usuario(res.getInt("codUsuario"),
                            res.getString("emailUsuario"),
                            res.getString("senhaUsuario"));

                }

                res.close();
                
                return usrselecionado;
            } catch (SQLException e) {
                System.out.println("Erro execução buscarUsuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - buscarUsuario");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

}
