package alphacontrol.models;

public class FormaPagamento {
    private int formaPagamentoId;
    private String nome;

    public FormaPagamento(String nome) {
        this.nome = nome;
    }

    public int getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(int formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

}
