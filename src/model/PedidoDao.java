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
import java.util.List;
import modelDominio.Pedido;
import modelDominio.PratoPedido;

/**
 *
 * @author christy
 */
public class PedidoDao {
    private Connection con;

    public PedidoDao() {
        this.con = Conector.getConnection();
    }

    public ArrayList<Pedido> getListaPedidosAnaliseCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosAnalise = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select pedido.*, cliente.*, empresa.* from pedido\n"
                        + "inner join cliente on (cliente.codCliente = pedido.codPedido) \n"
                        + "inner join empresa on (empresa.codEmpresa = pedido.codEmpresa) \n"
                        + "where statusPedido = 0 and codUsuario = ");

                while (res.next()) {
                    // TODO - Conferir como busca array do banco
                    List<PratoPedido> pratoPedidoLista = (List<PratoPedido>) res.getArray("pratoPedido");
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codEmpresa"),
                            pratoPedidoLista
                    );
                    listaPedidosAnalise.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosAnalise;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAnalise");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAnalise");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

    public ArrayList<Pedido> getListaPedidosAprovadosCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosAprovados = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();

                ResultSet res = stmt.executeQuery("select empresa.*, categoria.nomeCategoria, avaliacao.notaAvaliacao from empresa\n"
                        + "inner join categoria on (categoria.codCategoria = empresa.codCategoria) \n"
                        + "inner join avaliacao on (avaliacao.codAvaliacao = empresa.codAvaliacao) \n"
                        + "where abertoFechadoEmpresa = true");

                while (res.next()) {
                    // TODO - Conferir como busca array do banco
                    List<PratoPedido> pratoPedidoLista = (List<PratoPedido>) res.getArray("pratoPedido");
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codEmpresa"),
                            pratoPedidoLista
                    );
                    listaPedidosAprovados.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosAprovados;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosAprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosAprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
    
    public ArrayList<Pedido> getListaPedidosReprovadosCliente() {
        Statement stmt = null;
        ArrayList<Pedido> listaPedidosReprovados = new ArrayList<Pedido>();

        try {
            try {
                stmt = con.createStatement();

                ResultSet res = stmt.executeQuery("select empresa.*, categoria.nomeCategoria, avaliacao.notaAvaliacao from empresa\n"
                        + "inner join categoria on (categoria.codCategoria = empresa.codCategoria) \n"
                        + "inner join avaliacao on (avaliacao.codAvaliacao = empresa.codAvaliacao) \n"
                        + "where abertoFechadoEmpresa = true");

                while (res.next()) {
                    // TODO - Conferir como busca array do banco
                    List<PratoPedido> pratoPedidoLista = (List<PratoPedido>) res.getArray("pratoPedido");
                    Pedido pedido = new Pedido(
                            res.getInt("codPedido"),
                            res.getInt("statusPedido"),
                            res.getString("observacaoPedido"),
                            res.getInt("formaPagamentoPedido"),
                            res.getInt("codEmpresa"),
                            pratoPedidoLista
                    );
                    listaPedidosReprovados.add(pedido);
                }
                res.close();
                stmt.close();
                con.close();
                return listaPedidosReprovados;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPedidosReprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPedidosReprovados");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }
}
