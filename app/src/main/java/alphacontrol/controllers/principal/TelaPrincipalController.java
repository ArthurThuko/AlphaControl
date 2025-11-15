package alphacontrol.controllers.principal;

import javax.swing.*;
import java.awt.*;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.pdv.PdvController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;
import alphacontrol.views.login.TelaLogin;
import alphacontrol.views.pdv.TelaPDV;
import alphacontrol.views.relatorios.TelaRelatorios;
import alphacontrol.views.fiado.TelaFiado; 

public class TelaPrincipalController {

    private JFrame telaPrincipal;
    private ProdutoController produtoController;
    private ClienteController clienteController; 
    private PdvController pdvController;
    private FiadoController fiadoController;
    private FluxoCaixaController fluxoCaixaController;

    public TelaPrincipalController(ProdutoController pCtrl, ClienteController cCtrl, PdvController pdvCtrl, FiadoController fCtrl, FluxoCaixaController fluxoCtrl) {
        this.produtoController = pCtrl;
        this.clienteController = cCtrl;
        this.pdvController = pdvCtrl;
        this.fiadoController = fCtrl;
        this.fluxoCaixaController = fluxoCtrl;
    }

    public void setView(JFrame view) {
        this.telaPrincipal = view;
    }
    
    public ProdutoController getProdutoController() { return this.produtoController; }
    public ClienteController getClienteController() { return this.clienteController; }
    public PdvController getPdvController() { return this.pdvController; }
    public FiadoController getFiadoController() { return this.fiadoController; }
    public FluxoCaixaController getFluxoCaixaController() { return this.fluxoCaixaController; }

    public void abrirTelaEstoque() {
        new TelaEstoque(this).setVisible(true);
        if (telaPrincipal != null) telaPrincipal.dispose(); 
    }

    public void abrirTelaPDV() {
        new TelaPDV(this).setVisible(true);
        if (telaPrincipal != null) telaPrincipal.dispose(); 
    }

    public void abrirTelaFluxoCaixa() {
        new TelaFluxoCaixa(this).setVisible(true);
        if (telaPrincipal != null) telaPrincipal.dispose(); 
    }

    public void abrirTelaRelatorios() {
        new TelaRelatorios(this).setVisible(true);
        if (telaPrincipal != null) telaPrincipal.dispose(); 
    }

    public void abrirTelaFiado() {
        new TelaFiado(this).setVisible(true);
        if (telaPrincipal != null) telaPrincipal.dispose(); 
    }
    
    public void logout() {
        if (telaPrincipal != null) telaPrincipal.dispose();
        new TelaLogin().setVisible(true);
    }
}