package alphacontrol.models;

public class FormaPagamento {
    private int formaPagamentoId;
    private String nome;

    public FormaPagamento(int formaPagamentoId, String nome) {
        this.formaPagamentoId = formaPagamentoId;
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
}
