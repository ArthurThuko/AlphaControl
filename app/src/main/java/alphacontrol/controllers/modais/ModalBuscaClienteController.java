package alphacontrol.controllers.modais;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.models.Cliente;
import alphacontrol.views.cliente.ModalBuscaCliente;

public class ModalBuscaClienteController {

    private ModalBuscaCliente view;
    private ClienteController clienteController;

    public ModalBuscaClienteController(ModalBuscaCliente view, ClienteController clienteController) {
        this.view = view;
        this.clienteController = clienteController;
        initController();
    }

    private void initController() {
        // Carrega a tabela inicialmente
        atualizarTabela();
        
        // Configura o botão de busca
        view.getBtnPesquisar().addActionListener(e -> atualizarTabela());
        
        // Configura o botão de selecionar
        view.getBtnSelecionar().addActionListener(e -> selecionarCliente());
    }

    private void atualizarTabela() {
        String busca = view.getTxtPesquisa().getText().trim();
        // Limpa o placeholder se necessário
        if (busca.equals("Digite o nome...")) {
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

        // Pega o ID da primeira coluna (que está oculta na view, mas existe no modelo)
        int clienteId = (int) view.getModelo().getValueAt(selectedRow, 0);
        Cliente cliente = clienteController.buscarPorId(clienteId);
        
        if (cliente != null) {
            // AQUI ESTÁ A CORREÇÃO:
            // Usamos o método finalizarSelecao da View. 
            // Ele guarda o cliente no atributo da View e dá o dispose().
            // Isso faz o código da tela anterior (Adicionar Fiado) continuar a execução.
            view.finalizarSelecao(cliente);
        } else {
            JOptionPane.showMessageDialog(view, "Erro ao recuperar dados do cliente selecionado.");
        }
    }
}