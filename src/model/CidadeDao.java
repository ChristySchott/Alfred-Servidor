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
import java.util.ArrayList;
import modelDominio.Cidade;
import modelDominio.Estado;

/**
 *
 * @author be_ha
 */
public class CidadeDao {
    
    private Connection con;

    public CidadeDao() {
        con = Conector.getConnection();
    }
    
    public ArrayList<Cidade> getListaCidadesEstado(Estado estado) {
        PreparedStatement stmt = null;
        ArrayList<Cidade> listaCidades = new ArrayList<>();
        
        try {
            try {
                String sql = "SELECT * from cidade where codEstado = ?;";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, estado.getCodEstado());

                ResultSet res = stmt.executeQuery();
                
                while (res.next()) {
                    Cidade cidade = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"), res.getInt("codEstado"));
                    listaCidades.add(cidade);
                }

                res.close();
                return listaCidades;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaCidadesEstado");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaCidadesEstado");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public Cidade buscarCidade(Cidade cid) {
        PreparedStatement stmt = null;
        Cidade cidadeSelecionada = null;
        
        try {
            try {
                String sql = "SELECT * from cidade where nomeCidade = ? and codEstado = ?;";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, cid.getNomeCidade());
                stmt.setInt(2, cid.getCodEstado());

                ResultSet res = stmt.executeQuery();
                
                while (res.next()) {
                    cidadeSelecionada = new Cidade(res.getInt("codCidade"), res.getString("nomeCidade"), res.getInt("codEstado"));                    
                }
                System.out.println("cidade Selecionada CidadeDao" + cidadeSelecionada);
                res.close();
                return cidadeSelecionada;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaCidadesEstado");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaCidadesEstado");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
}
