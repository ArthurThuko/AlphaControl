package alphacontrol.models;

public class LoginService {
    private String login = "MerceariaSR";
    private String senha = "lucas123";

    public boolean autenticar(String usuario, String senha) {
        return usuario.equals(login) && senha.equals(senha);
    }
}