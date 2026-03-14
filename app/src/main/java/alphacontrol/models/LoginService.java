package alphacontrol.models;

public class LoginService {

    private String loginAdm = "Adm";
    private String senhaAdm = "654231";

    private String loginGerente = "Gerente";
    private String senhaGerente = "123456";

    public String autenticar(String usuario, String senha) {

        if(usuario.equals(loginAdm) && senha.equals(senhaAdm)) {
            return "ADM";
        }

        if(usuario.equals(loginGerente) && senha.equals(senhaGerente)) {
            return "GERENTE";
        }

        return null;
    }
}