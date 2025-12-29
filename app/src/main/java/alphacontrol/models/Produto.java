package alphacontrol.models;

public class Produto {
    private int produtoId;
    private String nome;
    private int qntEstoque;
    private double preco;

    public Produto(int produtoId, String nome, int qntEstoque, double preco) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.qntEstoque = qntEstoque;
        this.preco = preco;
    }

    public Produto(String nome, int qntEstoque, double preco) {
        this.nome = nome;
        this.qntEstoque = qntEstoque;
        this.preco = preco;
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

    public int getQntEstoque() {
        return qntEstoque;
    }

    public void setQntEstoque(int qntEstoque) {
        this.qntEstoque = qntEstoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void incrementarEstoque(int quantidade) {
        if (quantidade <= 0)
            throw new IllegalArgumentException("A quantidade para adicionar deve ser positiva.");
        this.qntEstoque += quantidade;
    }

    public void decrementarEstoque(int quantidade) {
        if (quantidade <= 0)
            throw new IllegalArgumentException("A quantidade para remover deve ser positiva.");
        if (quantidade > this.qntEstoque)
            throw new IllegalStateException("Estoque insuficiente para realizar a operação.");
        this.qntEstoque -= quantidade;
    }
}