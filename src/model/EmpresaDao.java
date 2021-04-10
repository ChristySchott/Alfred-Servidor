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
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.Empresa;
import modelDominio.Prato;

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
                    System.out.println("Erro execução inserir Empresa");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Empresa");
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
                System.out.println("Erro ao fechar operação - inserir Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }
    
    public boolean  empresaExiste(Empresa empresa) {
        PreparedStatement stmt = null;
        boolean existe = false;
        
        try {
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
                try {
                    System.out.println("Erro execução empresaExiste");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return false;
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - EmpresaExiste");
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
                System.out.println("Erro ao fechar operação - empresaExiste");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return false;
            }
        }
    }
    
    public Empresa efetuarLogin(Empresa empresa) {
        PreparedStatement stmt = null;
        Empresa empresaSelecionada = null;

        try {
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
                System.out.println("Erro execução efetuarLogin Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - EfetuarLogin Empresa");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Empresa> getListaEmpresasFechadas() {
        Statement stmt = null;
        ArrayList<Empresa> listEmpresasAbertas = new ArrayList<Empresa>();
        
        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select empresa from empresa \n"
                        + "where abertoFechadoEmpresa = false");

                while (res.next()) {
                    Empresa empresa = new Empresa(
                            res.getInt("codEmpresa"),
                            res.getString("nomeEmpresa"),
                            res.getInt("codCategoria"),
                            res.getInt("codAvaliacao"),
                            res.getDouble("precoMedioEmpresa"),
                            res.getInt("codEndereco"),
                            res.getBytes("imagemEmpresa")
                    );
                    listEmpresasAbertas.add(empresa);
                    System.out.println(empresa);
                }
                res.close();
                stmt.close();
                con.close();
                return listEmpresasAbertas;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaEmpresasFechadas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaEmpresasFechadas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Empresa> getListaEmpresasAbertas() {
        Statement stmt = null;
        ArrayList<Empresa> listEmpresasAbertas = new ArrayList<Empresa>();
        
        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select empresa from empresa \n"
                        + "where abertoFechadoEmpresa = true");

                while (res.next()) {
                    Empresa empresa = new Empresa(
                            res.getInt("codEmpresa"),
                            res.getString("nomeEmpresa"),
                            res.getInt("codCategoria"),
                            res.getInt("codAvaliacao"),
                            res.getDouble("precoMedioEmpresa"),
                            res.getInt("codEndereco"),
                            res.getBytes("imagemEmpresa")
                    );
                    listEmpresasAbertas.add(empresa);
                    System.out.println(empresa);
                }
                res.close();
                stmt.close();
                con.close();
                return listEmpresasAbertas;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaEmpresasAbertas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaEmpresasAbertas");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
}
