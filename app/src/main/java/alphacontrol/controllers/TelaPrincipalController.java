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

    // --- 3. Mudar o construtor para receber o ProdutoController ---
    public TelaPrincipalController(JFrame telaPrincipal, ProdutoController produtoController) {
        this.telaPrincipal = telaPrincipal;
        this.produtoController = produtoController; // Armazena o controller
    }

    // --- 4. Corrigir o método para passar o controller ---
    public void abrirTelaEstoque() {
        new TelaEstoque(this.produtoController).setVisible(true); // (Linha corrigida)
    }
    
    public void abrirTelaPDV() {
        new TelaPDV().setVisible(true);
    }

    public void abrirTelaFiados() {
        new TelaFiado().setVisible(true);
    }

    public void abrirTelaRelatorios() {
        new TelaRelatorios().setVisible(true);
    }

    public void abrirTelaFluxoCaixa() {
        new TelaFluxoCaixa().setVisible(true);
    }

    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                telaPrincipal,
                "Deseja realmente sair?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            for (Frame frame : Frame.getFrames()) {
                if (frame instanceof JFrame) {
                    frame.dispose();
                }
            }

            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        }
    }
}