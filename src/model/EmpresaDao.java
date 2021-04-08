/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelDominio.Empresa;

/**
 *
 * @author be_ha
 */
public class EmpresaDao {
    
    private Connection con;

    public EmpresaDao() {
        this.con = Conector.getConnection();
    }
    
    public int inserir(Empresa empresa) {
        PreparedStatement stmt = null;
        
        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into empresa (nomeEmpresa, cnpjEmpresa, codUsuario) values (?, ?, ?);";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, empresa.getNomeEmpresa());
                stmt.setString(2, empresa.getCnpjEmpresa());
                stmt.setInt(3, empresa.getCodUsuario());

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
    
    public boolean  empresaExiste(Empresa empresa) {
        PreparedStatement stmt = null;
        boolean existe = false;
    
        try {
            con.setAutoCommit(false);

            String sql = "select exists(\n" +
                        "select * from empresa \n" +
                        "join usuario on usuario.codUsuario = empresa.codUsuario\n" +
                        "where cnpjEmpresa=? or usuario.emailUsuario = ?\n" +
                        ") as existente;";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, empresa.getCnpjEmpresa());
            stmt.setString(2, empresa.getEmailUsuario());

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
    
    public Empresa efetuarLogin(Empresa empresa) {
        PreparedStatement stmt = null;
        Empresa empresaSelecionada = null;

        try {
            String sql = "select * from empresa join usuario on usuario.codUsuario = empresa.codUsuario where emailUsuario = ? and senhaUsuario = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, empresa.getEmailUsuario());
            stmt.setString(2, empresa.getSenhaUsuario());

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                empresaSelecionada = new Empresa(res.getString("nomeEmpresa"), res.getString("cnpjEmpresa"), res.getInt("codEmpresa"));
            }

            res.close();
            stmt.close();
            con.close();

            return empresaSelecionada;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
    }
}
