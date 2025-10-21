package alphacontrol.models;

public class Produto {
    private int produtoId;
    private String nome;
    private String categoria;
    private double valorCompra;
    private double valorVenda;
    private int qntEstoque;

    public Produto(int produtoId, String nome, String categoria, double valorCompra, double valorVenda,
            int qntEstoque) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.categoria = categoria;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.qntEstoque = qntEstoque;
    }
    public int getProdutoId() {
        return produtoId;
    }
    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public double getValorCompra() {
        return valorCompra;
    }
    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }
    public double getValorVenda() {
        return valorVenda;
    }
    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }
    public int getQntEstoque() {
        return qntEstoque;
    }
    public void setQntEstoque(int qntEstoque) {
        this.qntEstoque = qntEstoque;
    }

    
}