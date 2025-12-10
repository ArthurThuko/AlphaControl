package alphacontrol.controllers.principal;

import javax.swing.*;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.fiado.TelaFiado;

public class TelaPrincipalController {

    private JFrame telaPrincipal;
    private ProdutoController produtoController;
    private ClienteController clienteController;
    private FiadoController fiadoController;

    public TelaPrincipalController(ProdutoController pCtrl, ClienteController cCtrl,
            FiadoController fCtrl) {
        this.produtoController = pCtrl;
        this.clienteController = cCtrl;
        this.fiadoController = fCtrl;
    }

    public void setView(JFrame view) {
        this.telaPrincipal = view;
    }

    public ProdutoController getProdutoController() {
        return this.produtoController;
    }

    public ClienteController getClienteController() {
        return this.clienteController;
    }

    public FiadoController getFiadoController() {
        return this.fiadoController;
    }

    public void abrirTelaEstoque() {
        new TelaEstoque(this).setVisible(true);
        if (telaPrincipal != null)
            telaPrincipal.dispose();
    }

    public void abrirTelaFiado() {
        new TelaFiado(this).setVisible(true);
        if (telaPrincipal != null)
            telaPrincipal.dispose();
    }

    public void logout() {
        int resp = JOptionPane.showConfirmDialog(
                telaPrincipal, "Deseja realmente sair do AlphaControl?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}