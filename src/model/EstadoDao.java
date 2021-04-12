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
import modelDominio.Estado;

/**
 *
 * @author be_ha
 */
public class EstadoDao {    
    
    private Connection con;

    public EstadoDao() {
        con = Conector.getConnection();
    }

    public ArrayList<Estado> getListaEstados() {
        PreparedStatement stmt = null;
        ArrayList<Estado> listaEstados = new ArrayList<>();
        
        try {
            try {
                String sql = "SELECT * from estado;";
                stmt = con.prepareStatement(sql);
                ResultSet res = stmt.executeQuery();
                
                while (res.next()) {
                    Estado estado = new Estado(res.getInt("codEstado"), res.getString("nomeEstado"), res.getString("siglaEstado"));
                    listaEstados.add(estado);
                }

                res.close();
                return listaEstados;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaEstados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaEstados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
}
