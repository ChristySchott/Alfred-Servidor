package modelDominio;

public class PratoPedido {
    private static final long serialVersionUID = 123456789L;
    private int codPratoPedido;
    private int quantidadePratoPedido;
    private float valorUnidadePratoPedido;

    public PratoPedido(int codPratoPedido, int quantidadePratoPedido, float valorUnidadePratoPedido) {
        this.codPratoPedido = codPratoPedido;
        this.quantidadePratoPedido = quantidadePratoPedido;
        this.valorUnidadePratoPedido = valorUnidadePratoPedido;
    }

    public PratoPedido(int quantidadePratoPedido, float valorUnidadePratoPedido) {
        this.quantidadePratoPedido = quantidadePratoPedido;
        this.valorUnidadePratoPedido = valorUnidadePratoPedido;
    }

    public int getCodPratoPedido() {
        return codPratoPedido;
    }

    public void setCodPratoPedido(int codPratoPedido) {
        this.codPratoPedido = codPratoPedido;
    }

    public int getQuantidadePratoPedido() {
        return quantidadePratoPedido;
    }

    public void setQuantidadePratoPedido(int quantidadePratoPedido) {
        this.quantidadePratoPedido = quantidadePratoPedido;
    }

    public float getValorUnidadePratoPedido() {
        return valorUnidadePratoPedido;
    }

    public void setValorUnidadePratoPedido(float valorUnidadePratoPedido) {
        this.valorUnidadePratoPedido = valorUnidadePratoPedido;
    }

    @Override
    public String toString() {
        return "PratoPedido{" + "codPratoPedido=" + codPratoPedido + ", quantidadePratoPedido=" + quantidadePratoPedido + ", valorUnidadePratoPedido=" + valorUnidadePratoPedido + '}';
    }
    
}
