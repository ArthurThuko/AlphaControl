package alphacontrol.controllers;

import alphacontrol.views.TelaLogin;
import alphacontrol.views.TelaPrincipal;
import alphacontrol.dao.ClienteDAO; 
import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.dao.VendaDAO;
import alphacontrol.Conexao;
import alphacontrol.models.LoginService;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class LoginController {

    private TelaLogin view;
    private LoginService loginService;

    public LoginController(TelaLogin view) {
        this.view = view;
        this.loginService = new LoginService();
        this.view.getBtnLogin().addActionListener(e -> {
            autenticar();
        });
    }

    private void autenticar() {
        String usuario = view.getTxtUsuario();
        String senha = view.getTxtSenha();

        if (loginService.autenticar(usuario, senha)) {
            try {
                Connection connection = Conexao.getConexao();
                
                if (connection == null) {
                    JOptionPane.showMessageDialog(view, "Erro ao conectar ao banco de dados!");
                    return;
                }
                
                ProdutoDAO produtoDAO = new ProdutoDAO(connection);
                ProdutoController produtoController = new ProdutoController(produtoDAO);
                
                ClienteDAO clienteDAO = new ClienteDAO(connection);
                ClienteController clienteController = new ClienteController(clienteDAO);
                
                VendaDAO vendaDAO = new VendaDAO(connection);
                
                FiadoDAO fiadoDAO = new FiadoDAO(connection);
                FiadoController fiadoController = new FiadoController(fiadoDAO, clienteDAO);
                
                PdvController pdvController = new PdvController(produtoController, clienteController);

                TelaPrincipalController principalController = new TelaPrincipalController(produtoController, clienteController, pdvController, fiadoController);
                
                TelaPrincipal telaPrincipal = new TelaPrincipal(principalController);
                principalController.setView(telaPrincipal);
                
                telaPrincipal.setVisible(true);
                view.dispose();

            } catch (Exception ex) {
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog(view, "Erro ao iniciar: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(view, "Usuário ou senha inválidos!");
        }
    }
}