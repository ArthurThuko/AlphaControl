package alphacontrol.models;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String cep;
    private String rua;
    private String bairro;
    private String nCasa;
    private double debito;
    private int enderecoId;

    public Cliente(int id, String nome, String cpf, String telefone, String rua, String bairro, String nCasa, String cep, double debito, int enderecoId) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.nCasa = nCasa;
        this.cep = cep;
        this.debito = debito;
        this.enderecoId = enderecoId;
    }

    public Cliente(String nome, String cpf, String telefone, String cep, String rua, String bairro, String nCasa) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.nCasa = nCasa;
        this.debito = 0.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getCep() { return cep; }
    public String getRua() { return rua; }
    public String getBairro() { return bairro; }
    public String getNumeroCasa() { return nCasa; }
    public double getDebito() { return debito; }
    public void setDebito(double debito) { this.debito = debito; }
    public int getEnderecoId() { return enderecoId; }
    public void setEnderecoId(int enderecoId) { this.enderecoId = enderecoId; }
    
    public String getEnderecoCompleto() {
        return rua + ", " + nCasa + ", " + bairro;
    }
}