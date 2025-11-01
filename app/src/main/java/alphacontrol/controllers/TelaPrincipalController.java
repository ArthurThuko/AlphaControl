package alphacontrol.controllers;

import alphacontrol.views.TelaLogin;
import alphacontrol.views.TelaPDV;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.fiado.TelaFiado;
import alphacontrol.views.TelaRelatorios;
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TelaPrincipalController {

    private JFrame telaPrincipal;

    public TelaPrincipalController(JFrame telaPrincipal) {
        this.telaPrincipal = telaPrincipal;
    }

    public void abrirTelaEstoque() {
        new TelaEstoque().setVisible(true);
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
