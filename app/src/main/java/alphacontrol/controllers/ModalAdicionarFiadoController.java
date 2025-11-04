package alphacontrol.controllers;

import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import alphacontrol.views.fiado.ModalAdicionarFiado;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.util.List;

public class ModalAdicionarFiadoController {

    private ModalAdicionarFiado view;
    private FiadoController fiadoController;
    private ClienteController clienteController;

    public ModalAdicionarFiadoController(ModalAdicionarFiado view, FiadoController fc, ClienteController cc) {
        this.view = view;
        this.fiadoController = fc;
        this.clienteController = cc;
        
        initController();
    }

    private void initController() {
        carregarClientes();
        
        view.getBtnSalvar().addActionListener(e -> salvarFiado());
        
        view.getBtnAdicionarCliente().addActionListener(e -> {
            view.getParentView().abrirModalAdicionarCliente();
            carregarClientes();
        });
    }

    public void carregarClientes() {
        try {
            List<Cliente> clientes = clienteController.listar();
            DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel<>();
            for (Cliente c : clientes) {
                model.addElement(c);
            }
            view.getCmbClientes().setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void salvarFiado() {
        Cliente clienteSelecionado = (Cliente) view.getCmbClientes().getSelectedItem();
        String valorStr = view.getTxtValor().getText().replace(",", ".");

        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(view, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Valor inválido. Insira um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Fiado novoFiado = new Fiado();
            novoFiado.setClienteId(clienteSelecionado.getId());
            novoFiado.setValor(valor);
            novoFiado.setData(LocalDateTime.now());
            novoFiado.setStatus("PENDENTE"); 

            fiadoController.adicionarFiado(novoFiado);
            
            JOptionPane.showMessageDialog(view, "Fiado registrado com sucesso!");
            view.dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar fiado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}