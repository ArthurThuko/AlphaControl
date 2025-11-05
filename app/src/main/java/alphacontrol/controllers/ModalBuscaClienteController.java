package alphacontrol.controllers;

import alphacontrol.models.Cliente;
import alphacontrol.views.cliente.ModalBuscaCliente;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ModalBuscaClienteController {

    private ModalBuscaCliente view;
    private ClienteController clienteController;

    public ModalBuscaClienteController(ModalBuscaCliente view, ClienteController clienteController) {
        this.view = view;
        this.clienteController = clienteController;
        initController();
    }

    private void initController() {
        atualizarTabela();
        
        view.getBtnPesquisar().addActionListener(e -> atualizarTabela());
        
        view.getBtnSelecionar().addActionListener(e -> selecionarCliente());
    }

    private void atualizarTabela() {
        String busca = view.getTxtPesquisa().getText();
        if (busca.equals("Digite o nome ou CPF...")) {
            busca = "";
        }
        
        DefaultTableModel modelo = view.getModelo();
        modelo.setRowCount(0);
        
        try {
            List<Cliente> clientes = clienteController.pesquisar(busca);
            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpf(),
                    c.getTelefone()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao buscar clientes: " + e.getMessage());
        }
    }

    private void selecionarCliente() {
        int selectedRow = view.getTabela().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Por favor, selecione um cliente na tabela.");
            return;
        }

        int clienteId = (int) view.getModelo().getValueAt(selectedRow, 0);
        Cliente cliente = clienteController.buscarPorId(clienteId);
        
        if (cliente != null) {
            view.setClienteSelecionado(cliente);
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Erro ao selecionar o cliente.");
        }
    }
}