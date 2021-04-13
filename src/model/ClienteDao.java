package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelDominio.Cidade;
import modelDominio.Cliente;
import modelDominio.Estado;
import modelDominio.Usuario;

public class ClienteDao {

    private Connection con;

    public ClienteDao() {
        this.con = Conector.getConnection();
    }

    public int inserir(Cliente cliente) {
        PreparedStatement stmt = null;
        
        System.out.println("iniciou");

        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into cliente (codUsuario) values (?);";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, cliente.getCodUsuario());
                
                System.out.println("inseriu");

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro na execução inserir Cliente");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Cliente");
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
                System.out.println("Erro ao fechar operação - inserir Cliente");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public boolean usuarioExiste(Cliente cliente) {
        PreparedStatement stmt = null;
        boolean existe = false;

        try {
            try {
                con.setAutoCommit(false);

                String sql = "select exists(\n"
                        + "select * from usuario \n"
                        + "where emailUsuario = ?\n"
                        + ") as existente;";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, cliente.getEmailUsuario());

                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    existe = (res.getInt("existente") == 1);
                }

                res.close();
                stmt.close();
                con.close();

                return existe;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução usuarioExiste");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return false;
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - usuarioExiste");
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return false;
                }
            }
        } finally {
            try {
                stmt.close();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - usuarioExiste");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return false;
            }
        }
    }
    
    public Cliente efetuarLogin(Usuario usuario) {
        PreparedStatement stmt = null;
        Cliente clienteSelecionado = null;

        try {
            try {
                String sql = "select * from cliente \n" +
                            "join usuario on usuario.codUsuario = cliente.codUsuario \n" +
                            "left join cidade on (usuario.codCidade IS NOT NULL AND cidade.codCidade = usuario.codCidade)\n" +
                            "left join estado on (usuario.codEstado IS NOT NULL AND estado.codEstado= usuario.codEstado)\n" +
                            " where emailUsuario = ? and senhaUsuario = ?;";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, usuario.getEmailUsuario());
                stmt.setString(2, usuario.getSenhaUsuario());

                ResultSet res = stmt.executeQuery();
                
                while (res.next()) {
                    //Talvez testar antes
                    Cidade cid = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"));
                    Estado est = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));

                    clienteSelecionado = new Cliente(
                            res.getInt("codCliente"),
                            res.getString("nomeCliente"),
                            res.getString("sobrenomeCliente"), 
                            res.getDate("dataNascimentoCliente"), 
                            res.getInt("areaCliente"), 
                            res.getInt("telefoneCliente"), 
                            res.getBytes("imagemCliente"),
                            res.getInt("codUsuario"),
                            res.getString("emailUsuario"), 
                            cid,
                            est,
                            res.getString("ruaUsuario"),
                            res.getString("bairroUsuario"), 
                            res.getString("complementoUsuario"),
                            res.getInt("numeroUsuario")
                    );
                }

                res.close();
                return clienteSelecionado;
            } catch (SQLException e) {
                System.out.println("Erro ao executar o Efetuar Login - Cliente");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação Efetuar Login - Cliente");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

}
