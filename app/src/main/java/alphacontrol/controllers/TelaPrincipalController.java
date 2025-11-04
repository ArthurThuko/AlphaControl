package alphacontrol.controllers;

import alphacontrol.views.TelaLogin;
import alphacontrol.views.TelaPDV;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.fiado.TelaFiado;
import alphacontrol.views.TelaRelatorios;
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;

import alphacontrol.controllers.ProdutoController; 

import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TelaPrincipalController {

    private JFrame telaPrincipal;

    private final ProdutoController produtoController;

    public TelaPrincipalController(JFrame telaPrincipal, ProdutoController produtoController) {
        this.telaPrincipal = telaPrincipal;
        this.produtoController = produtoController;
    }

    public void abrirTelaEstoque() {
        new TelaEstoque(this.produtoController).setVisible(true);
        telaPrincipal.dispose();
    }
    
    public void abrirTelaPDV() {
        new TelaPDV(this.produtoController).setVisible(true);
        telaPrincipal.dispose();
    }

    public void abrirTelaFiados() {
        new TelaFiado(this.produtoController).setVisible(true);
        telaPrincipal.dispose();
    }

    public void abrirTelaRelatorios() {
        new TelaRelatorios(this.produtoController).setVisible(true);
        telaPrincipal.dispose();
    }

    public void abrirTelaFluxoCaixa() {
        new TelaFluxoCaixa(this.produtoController).setVisible(true);
        telaPrincipal.dispose();
    }

    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                telaPrincipal,
                "Deseja realmente sair?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            telaPrincipal.dispose();
            
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        }
    }
}