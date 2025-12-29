package alphacontrol.models;

public class FiadoItem {

    private int id;
    private int fiadoId;
    private int produtoId;
    private int quantidade;
    private double valorUnitario;

    public FiadoItem(){};

    public FiadoItem(int id, int fiadoId, int produtoId, int quantidade, double valorUnitario) {
        this.id = id;
        this.fiadoId = fiadoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFiadoId() {
        return fiadoId;
    }

    public void setFiadoId(int fiadoId) {
        this.fiadoId = fiadoId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getSubtotal() {
        return quantidade * valorUnitario;
    }
}