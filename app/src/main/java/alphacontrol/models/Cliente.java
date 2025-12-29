package alphacontrol.models;

public class Cliente {

    private int id;
    private String nome;
    private String telefone;
    private String rua;
    private String bairro;
    private double debito;

    public Cliente(int id, String nome, String telefone, String rua, String bairro, double debito) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.debito = debito;
    }

    public Cliente(String nome, String telefone, String rua, String bairro) {
        this.nome = nome;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public double getDebito() {
        return debito;
    }

    public void setDebito(double debito) {
        this.debito = debito;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}