package modelDominio;

import java.io.Serializable;

public class Prato implements Serializable {
    private static final long serialVersionUID = 123456789L;
    private int codPrato;
    private String nomePrato;
    private String descricaoPrato;
    private Float valorPrato;
    private Empresa empresa;

    public Prato(int codPrato, String nomePrato, String descricaoPrato, Float valorPrato, int codEmpresa) {
        this.codPrato = codPrato;
        this.nomePrato = nomePrato;
        this.descricaoPrato = descricaoPrato;
        this.valorPrato = valorPrato;
        Empresa emp = new Empresa(codEmpresa);
        this.empresa = emp;
    }

    public Prato(String nomePrato, String descricaoPrato, Float valorPrato, int codEmpresa) {
        this.nomePrato = nomePrato;
        this.descricaoPrato = descricaoPrato;
        this.valorPrato = valorPrato;
        Empresa emp = new Empresa(codEmpresa);
        this.empresa = emp;
    }

    @Override
    public String toString() {
        return "Prato{" + "codPrato=" + codPrato + ", nomePrato=" + nomePrato + ", descricaoPrato=" + descricaoPrato + ", valorPrato=" + valorPrato + ", empresa=" + empresa + '}';
    }
    
}
