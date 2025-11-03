package alphacontrol.models;

public class MovimentacaoCaixa {
    private int id;
    private String nome;
    private String tipo;
    private double valor;
    private String data;

    public MovimentacaoCaixa(){}
    
    public MovimentacaoCaixa(String nome, String tipo, double valor, String data) {
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
