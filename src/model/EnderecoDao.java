package model;

import modelDominio.Endereco;
import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EnderecoDao {
    
    private Connection con;

    public EnderecoDao() {
        con = Conector.getConnection();
    }

    public int inserir(Endereco end) {
        
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                   
                String sql = "insert into endereco (cidadeEndereco,estadoEndereco,ruaEndereco,bairroEndereco,complementoEndereco) values (?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, end.getCidadeEndereco());
                stmt.setString(2, end.getEstadoEndereco());
                stmt.setString(3, end.getRuaEndereco());
                stmt.setString(4, end.getBairroEndereco());
                stmt.setString(5, end.getComplementoEndereco());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Endereço");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Endereço");
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
                System.out.println("Erro ao fechar operação - inserir Endereço");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    //Acho q não vamos excluir
//    public int excluir(Endereco end) {
//        
//        PreparedStatement stmt = null;
//        try {
//            try {
//                con.setAutoCommit(false);
//                String sql = "delete from endereco where codEndereco = ?";
//                stmt = con.prepareStatement(sql);
//                stmt.setInt(1, end.getCodEndereco());
//
//                stmt.execute();
//                con.commit();
//                return -1;
//            } catch (SQLException e) {
//                try {
//                    con.rollback(); 
//                    return e.getErrorCode();
//                } catch (SQLException ex) {
//                    return ex.getErrorCode();
//                }
//            }
//        } finally {
//            try {
//                stmt.close();
//                con.setAutoCommit(true);
//                con.close();
//            } catch (SQLException e) {
//                return e.getErrorCode();
//            }
//        }
//    }

    public int alterar(Endereco end) {
        
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);
                String sql = "update endereco set \n"
                        + "cidadeEndereco = ?, \n"
                        + "estadoEndereco = ?, \n"
                        + "ruaEndereco = ?, \n"
                        + "bairroEndereco = ?, \n"
                        + "complementoEndereco = ? \n"
                        + "where codEndereco = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, end.getCidadeEndereco());
                stmt.setString(2, end.getEstadoEndereco());
                stmt.setString(3, end.getRuaEndereco());
                stmt.setString(4, end.getBairroEndereco());
                stmt.setString(5, end.getComplementoEndereco());
                stmt.setInt(6, end.getCodEndereco());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Endereço");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Endereço");
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
                System.out.println("Erro ao fechar operação - alterar Endereço");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    //Acho q não precisa
//    public ArrayList<Endereco> getListaEnderecos() {
//        
//        Statement stmt = null;
//        ArrayList<Endereco> listEnderecos = new ArrayList<Endereco>();
//
//        try {
//            stmt = con.createStatement();
//            ResultSet res = stmt.executeQuery("select * from endereco");
//
//            while (res.next()) {
//                Endereco end = new Endereco(res.getInt("codEndereco"));
//                listEnderecos.add(end);
//            }
//            
//            res.close();
//            stmt.close();
//            con.close();
//            
//            return listEnderecos;
//        } catch (SQLException e) {
//            System.out.println(e.getErrorCode() + "-" + e.getMessage());
//            return null;
//        }
//
//    }
}
