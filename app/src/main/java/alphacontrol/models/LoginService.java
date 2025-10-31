package alphacontrol.models;

public class LoginService {
    private String login = "MerceariaSR";
    private String senha = "lucas123";

    public boolean autenticar(String usuario, String senha) {
        return usuario.equals(this.login) && senha.equals(this.senha);
    }
}