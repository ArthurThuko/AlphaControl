package alphacontrol.models;

public class Produto {
    private int produtoId;
    private String nome;
    private String categoria;
    private double valorCompra;
    private double valorVenda;
    private int qntEstoque;

    public Produto(int produtoId, String nome, String categoria, double valorCompra, double valorVenda, int qntEstoque) {
        this.setProdutoId(produtoId);
        this.setNome(nome);
        this.setCategoria(categoria);
        this.setValorCompra(valorCompra);
        this.setValorVenda(valorVenda);
        this.setQntEstoque(qntEstoque);
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        if (produtoId < 0)
            throw new IllegalArgumentException("O ID do produto não pode ser negativo.");
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome do produto não pode estar vazio.");
        this.nome = nome.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("A categoria não pode estar vazia.");
        this.categoria = categoria.trim();
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        if (valorCompra < 0)
            throw new IllegalArgumentException("O valor de compra não pode ser negativo.");
        this.valorCompra = valorCompra;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        if (valorVenda < 0)
            throw new IllegalArgumentException("O valor de venda não pode ser negativo.");
        if (valorVenda < this.valorCompra)
            System.out.println("⚠️ Atenção: o valor de venda está abaixo do valor de compra.");
        this.valorVenda = valorVenda;
    }

    public int getQntEstoque() {
        return qntEstoque;
    }

    public void setQntEstoque(int qntEstoque) {
        if (qntEstoque < 0)
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        this.qntEstoque = qntEstoque;
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
