package alphacontrol.controllers.principal;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.pdv.PdvController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;
import alphacontrol.views.pdv.TelaPDV;
import alphacontrol.views.principal.TelaPrincipal;
import alphacontrol.views.relatorios.TelaRelatorios;
import alphacontrol.views.fiado.TelaFiado;

public class TelaPrincipalController {

    // 🔥 Agora representa QUALQUER tela aberta
    private JFrame telaAtual;

    private ProdutoController produtoController;
    private ClienteController clienteController;
    private PdvController pdvController;
    private FiadoController fiadoController;
    private FluxoCaixaController fluxoCaixaController;

    public TelaPrincipalController(
            ProdutoController pCtrl,
            ClienteController cCtrl,
            PdvController pdvCtrl,
            FiadoController fCtrl,
            FluxoCaixaController fluxoCtrl) {

        this.produtoController = pCtrl;
        this.clienteController = cCtrl;
        this.pdvController = pdvCtrl;
        this.fiadoController = fCtrl;
        this.fluxoCaixaController = fluxoCtrl;
    }

    private void trocarTela(JFrame novaTela) {

        if (telaAtual != null) {
            telaAtual.dispose();
        }

        telaAtual = novaTela;
        telaAtual.setVisible(true);
    }

    public ProdutoController getProdutoController() {
        return produtoController;
    }

    public ClienteController getClienteController() {
        return clienteController;
    }

    public PdvController getPdvController() {
        return pdvController;
    }

    public FiadoController getFiadoController() {
        return fiadoController;
    }

    public FluxoCaixaController getFluxoCaixaController() {
        return fluxoCaixaController;
    }

    public void abrirTelaPrincipal() {
        TelaPrincipal tela = new TelaPrincipal(this);
        trocarTela(tela);
    }

    public void abrirTelaEstoque() {
        TelaEstoque tela = new TelaEstoque(this);
        trocarTela(tela);
    }

    public void abrirTelaPDV() {
        TelaPDV tela = new TelaPDV(this);
        trocarTela(tela);
    }

    public void abrirTelaFluxoCaixa() {
        TelaFluxoCaixa tela = new TelaFluxoCaixa(this);
        trocarTela(tela);
    }

    public void abrirTelaRelatorios() {
        TelaRelatorios tela = new TelaRelatorios(this);
        trocarTela(tela);
    }

    public void abrirTelaFiado() {
        TelaFiado tela = new TelaFiado(this);
        trocarTela(tela);
    }

    public void logout() {
        int resp = JOptionPane.showConfirmDialog(
                telaAtual,
                "Deseja realmente sair do AlphaControl?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}