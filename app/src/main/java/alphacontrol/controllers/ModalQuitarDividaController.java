package alphacontrol.controllers;

import alphacontrol.models.Cliente;
import alphacontrol.views.fiado.ModalQuitarDivida;

import javax.swing.*;
import java.text.DecimalFormat;

public class ModalQuitarDividaController {

    private ModalQuitarDivida modal;
    private FiadoController fiadoController;
    private Cliente cliente;

    public ModalQuitarDividaController(ModalQuitarDivida modal) {
        this.modal = modal;
        this.fiadoController = modal.getFiadoController();
        this.cliente = modal.getCliente();
        
        this.modal.getBtnSalvar().addActionListener(e -> salvarPagamento());
        this.modal.getBtnCancelar().addActionListener(e -> modal.dispose());
    }

    private void salvarPagamento() {
        String valorStr = modal.getTxtValorPagar();
        double valorPago;

        try {
            valorPago = Double.parseDouble(valorStr.replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(modal, "Valor inválido. Insira um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (valorPago <= 0) {
            JOptionPane.showMessageDialog(modal, "O valor a pagar deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (valorPago > cliente.getDebito()) {
            String debitoFormatado = new DecimalFormat("R$ #,##0.00").format(cliente.getDebito());
            JOptionPane.showMessageDialog(modal, "O valor a pagar não pode ser maior que a dívida total (" + debitoFormatado + ").", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (valorPago == cliente.getDebito()) {
                fiadoController.quitarDividaCompleta(cliente.getId());
                JOptionPane.showMessageDialog(modal, "Dívida quitada com sucesso!");
            } else {
                fiadoController.pagarFiado(cliente.getId(), valorPago);
                JOptionPane.showMessageDialog(modal, "Pagamento parcial realizado com sucesso!");
            }
            
            modal.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(modal, "Erro ao processar pagamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}