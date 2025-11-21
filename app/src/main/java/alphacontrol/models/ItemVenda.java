package alphacontrol.models;

public class ItemVenda {

    private Produto produto;
    private int itemId;
    private int vendaId;
    private int produtoId;
    private int quantidade;
    private double valorUnitario;
    private double valorTotal;
    private double subtotal;

    public ItemVenda(int itemId, int vendaId, int produtoId, int quantidade, double valorUnitario, double valorTotal,
            Produto produto) {
        this.produto = produto;
        this.itemId = itemId;
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public ItemVenda() {
    }

    // Construtor para adicionar novo item (sem itemId)
    public ItemVenda(int vendaId, int produtoId, int quantidade, double valorUnitario) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = quantidade * valorUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
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

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = this.valorUnitario * this.quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        this.valorTotal = this.valorUnitario * this.quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
