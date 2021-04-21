package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.Prato;

public class PratoDao {

    private Connection con;

    public PratoDao() {
        con = Conector.getConnection();
    }

    public int inserir(Prato pt) {
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "insert into prato (nomePrato,descricaoPrato,valorPrato,codEmpresa)\n"
                        + "values (?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, pt.getNomePrato());
                stmt.setString(2, pt.getDescricaoPrato());
                stmt.setDouble(3, pt.getValorPrato());
                stmt.setInt(4, pt.getEmpresa().getCodEmpresa());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução inserir Prato");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - inserir Prato");
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
                System.out.println("Erro ao fechar operação - inserir Prato");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int excluir(Prato pt) {
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "delete from prato where codPrato = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, pt.getCodPrato());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução excluir Prato");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - excluir Prato");
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
                System.out.println("Erro ao fechar operação - excluir Prato");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public int alterar(Prato pt) {
        PreparedStatement stmt = null;
        try {
            try {
                con.setAutoCommit(false);

                String sql = "update prato set \n"
                        + "nomePrato = ?, \n"
                        + "descricaoPrato = ?, \n"
                        + "valorPrato = ?, \n"
                        + "codEmpresa = ? \n"
                        + "where codPrato = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, pt.getNomePrato());
                stmt.setString(2, pt.getDescricaoPrato());
                stmt.setDouble(3, pt.getValorPrato());
                stmt.setInt(4, pt.getEmpresa().getCodEmpresa());
                stmt.setInt(5, pt.getCodPrato());

                stmt.execute();
                con.commit();
                return -1;
            } catch (SQLException e) {
                try {
                    System.out.println("Erro execução alterar Prato");
                    con.rollback();
                    System.out.println(e.getErrorCode() + "-" + e.getMessage());
                    return e.getErrorCode();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fazer rollback - alterar Prato");
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
                System.out.println("Erro ao fechar operação - alterar Prato");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return e.getErrorCode();
            }
        }
    }

    public ArrayList<Prato> getListaPratos() {
        Statement stmt = null;
        ArrayList<Prato> listPratos = new ArrayList<Prato>();
        
        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select prato.*, empresa.nomeEmpresa from prato \n"
                        + "join empresa on (empresa.codEmpresa = prato.codEmpresa)");

                while (res.next()) {
                   Prato pt = new Prato(
                            res.getInt("codPrato"),
                            res.getString("nomePrato"),
                            res.getString("descricaoPrato"),
                            res.getDouble("valorPrato"),
                            res.getInt("codEmpresa")
                    );
                    listPratos.add(pt);
                }
                res.close();
                stmt.close();
                con.close();
                return listPratos;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPratos");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPratos");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

    public ArrayList<Prato> getListaPratoEmpresa(int codEmpresa) {
        Statement stmt = null;
        ArrayList<Prato> listPratos = new ArrayList<Prato>();

        try {
            try {
                stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("select prato.*, empresa.nomeEmpresa from prato join empresa on (empresa.codEmpresa = prato.codEmpresa) where empresa.codEmpresa = " + codEmpresa + "");

                while (res.next()) {
                    Prato pt = new Prato(
                            res.getInt("codPrato"),
                            res.getString("nomePrato"),
                            res.getString("descricaoPrato"),
                            res.getDouble("valorPrato"),
                            res.getInt("codEmpresa")
                    );
                    listPratos.add(pt);
                }
                res.close();
                return listPratos;
            } catch (SQLException e) {
                System.out.println("Erro execução getListaPratos");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar operação - getListaPratos");
                System.out.println(e.getErrorCode() + "-" + e.getMessage());
                return null;
            }
        }
    }

}
