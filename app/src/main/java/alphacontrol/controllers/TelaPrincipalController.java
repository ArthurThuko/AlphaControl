package alphacontrol.controllers;

import javax.swing.*;
import java.awt.*;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.TelaPDV; 
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;
import alphacontrol.views.TelaRelatorios;
import alphacontrol.views.fiado.TelaFiado;
import alphacontrol.views.TelaLogin; 

public class TelaPrincipalController {

    private JFrame telaPrincipal;
    private ProdutoController produtoController;
    private ClienteController clienteController; 
    private PdvController pdvController;
    private FiadoController fiadoController;

    public TelaPrincipalController(ProdutoController pCtrl, ClienteController cCtrl, PdvController pdvCtrl, FiadoController fCtrl) {
        this.produtoController = pCtrl;
        this.clienteController = cCtrl;
        this.pdvController = pdvCtrl;
        this.fiadoController = fCtrl;
    }

    public void setView(JFrame view) {
        this.telaPrincipal = view;
    }
    
    public ProdutoController getProdutoController() { return this.produtoController; }
    public ClienteController getClienteController() { return this.clienteController; }
    public PdvController getPdvController() { return this.pdvController; }
    public FiadoController getFiadoController() { return this.fiadoController; }

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