package alphacontrol.models;

import java.util.Date;

public class Venda {

    private int vendaId;
    private String cliente;
    private FormaPagamento formaPagamento;
    private double total;
    private Date dataVenda;

    // Construtor corrigido e compatível com o DAO
    public Venda(int vendaId, String cliente, FormaPagamento formaPagamento, double total, Date dataVenda) {
        this.vendaId = vendaId;
        this.cliente = cliente;
        this.formaPagamento = formaPagamento;
        this.total = total;
        this.dataVenda = dataVenda;
    }

    public Venda(){}

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }
}

