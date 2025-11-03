package alphacontrol.controllers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alphacontrol.models.LoginService;
import alphacontrol.views.TelaPrincipal;

import alphacontrol.controllers.ProdutoController;
import alphacontrol.controllers.TelaPrincipalController;
import alphacontrol.dao.ProdutoDAO;
import java.sql.Connection;

// --- 1. IMPORTAR SUA CLASSE DE CONEXÃO ---
// (Estou assumindo que o nome da classe é 'Conexao' com 'C' maiúsculo)
import alphacontrol.Conexao; 


public class LoginController {
    private LoginService service;

    public LoginController() {
        this.service = new LoginService();
    }

    public void fazerLogin(String usuario, String senha, JFrame tela) {
        if (service.autenticar(usuario, senha)) {
            JOptionPane.showMessageDialog(tela, "Login realizado com sucesso!");
            tela.dispose(); // Fecha a tela de login

            try {
                // --- 2. USAR A CONEXÃO REAL ---
                // (Se sua classe for 'conexao' minúsculo, mude 'Conexao' para 'conexao' abaixo)
                Connection conexaoBD = Conexao.getConexao(); 

                ProdutoDAO produtoDAO = new ProdutoDAO(conexaoBD);
                ProdutoController produtoController = new ProdutoController(produtoDAO);
                
                TelaPrincipal telaPrincipal = new TelaPrincipal(null); 
                
                TelaPrincipalController principalController = new TelaPrincipalController(telaPrincipal, produtoController);
                
                telaPrincipal.setController(principalController); 
                telaPrincipal.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro crítico ao iniciar: " + e.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(tela, "Usuário ou senha incorretos.");
        }
    }
}