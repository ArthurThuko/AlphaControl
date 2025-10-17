package alphacontrol.models;

public class LoginService {
    public boolean autenticar(String usuario, String senha) {
        return usuario.equals("MerceariaSR") && senha.equals("lucas123");
    }
}