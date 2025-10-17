package alphacontrol.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alphacontrol.models.LoginService;

public class LoginController {
    private LoginService service;

    public LoginController() {
        this.service = new LoginService();
    }

    public void login(String usuario, String senha, JFrame tela) {
        if (service.autenticar(usuario, senha)) {
            JOptionPane.showMessageDialog(tela, "Login realizado com sucesso!");
            tela.dispose();
        } else {
            JOptionPane.showMessageDialog(tela, "Usuário ou senha incorretos.");
        }
    }
}