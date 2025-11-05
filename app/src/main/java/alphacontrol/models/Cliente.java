package alphacontrol.models;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String rua;
    private String bairro;
    private String numeroCasa;
    private String cep;
    private double debito;
    private int enderecoId;

    public Cliente(String nome, String cpf, String telefone, String cep, String rua, String bairro, String numeroCasa) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.numeroCasa = numeroCasa;
        this.debito = 0.0;
    }

    public Cliente(int id, String nome, String cpf, String telefone, String rua, String bairro, String numeroCasa, String cep, double debito, int enderecoId) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.numeroCasa = numeroCasa;
        this.cep = cep;
        this.debito = debito;
        this.enderecoId = enderecoId;
    }

    public String getEnderecoCompleto() {
        return String.format("%s, %s, %s", this.rua, this.numeroCasa, this.bairro);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(String numeroCasa) { this.numeroCasa = numeroCasa; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public double getDebito() { return debito; }
    public void setDebito(double debito) { this.debito = debito; }

    public int getEnderecoId() { return enderecoId; }
    public void setEnderecoId(int enderecoId) { this.enderecoId = enderecoId; }

    @Override
    public String toString() {
        return this.nome;
    }
}