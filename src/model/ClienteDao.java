package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelDominio.Cliente;

public class ClienteDao {

    private Connection con;

    public ClienteDao() {
        this.con = Conector.getConnection();
    }

    public int inserir(Cliente cliente) {
        PreparedStatement stmt = null;

        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into cliente (codUsuario) values (?);";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, cliente.getCodUsuario());

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

    public boolean usuarioExiste(Cliente cliente) {
        PreparedStatement stmt = null;
        boolean existe = false;

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
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return false;
        }
    }

    public Cliente efetuarLogin(Cliente cliente) {
        PreparedStatement stmt = null;
        Cliente clienteSelecionado = null;

        try {
            String sql = "select * from cliente join usuario on usuario.codUsuario = cliente.codUsuario where emailUsuario = ? and senhaUsuario = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cliente.getEmailUsuario());
            stmt.setString(2, cliente.getSenhaUsuario());

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                // TODO - Criar cliente com infos completas
                if (!res.getString("nomeCliente").equals("")) {;
                    clienteSelecionado = new Cliente(res.getInt("codCliente"), res.getInt("codUsuario"), res.getString("emailUsuario"), res.getString("senhaUsuario"));
                } else {
                    clienteSelecionado = new Cliente(res.getInt("codCliente"), res.getInt("codUsuario"), res.getString("emailUsuario"), res.getString("senhaUsuario"));
                }

            }

            res.close();
            stmt.close();
            con.close();

            return clienteSelecionado;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
    }
}
