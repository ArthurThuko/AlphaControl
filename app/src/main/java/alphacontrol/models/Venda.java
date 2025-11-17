package alphacontrol.models;   

import java.util.Date;

public class Venda{

    private int vendaId;
    private Date dataVenda;
    private double total;
    private FormaPagamento formaPagamento;

    public Venda(int vendaId, Date dataVenda, double total, FormaPagamento formaPagamento) {
        this.vendaId = vendaId;
        this.dataVenda = dataVenda;
        this.total = total;
        this.formaPagamento = formaPagamento;
    }

    public Venda(){}

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

}