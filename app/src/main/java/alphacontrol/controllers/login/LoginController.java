package alphacontrol.controllers.login;

import java.sql.Connection;

import javax.swing.JOptionPane;

import alphacontrol.conexao.Conexao;
import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.pdv.PdvController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.LoginService;
import alphacontrol.views.login.TelaLogin;

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

                FiadoDAO fiadoDAO = new FiadoDAO(connection);

                FluxoCaixaController fluxoCaixaController = new FluxoCaixaController(connection);

                FiadoController fiadoController = new FiadoController(fiadoDAO, clienteDAO, fluxoCaixaController);

                PdvController pdvController = new PdvController(produtoController);

                TelaPrincipalController principalController = new TelaPrincipalController(
                        produtoController,
                        clienteController,
                        pdvController,
                        fiadoController,
                        fluxoCaixaController);

                principalController.abrirTelaPrincipal();
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