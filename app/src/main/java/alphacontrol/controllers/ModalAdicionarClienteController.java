package alphacontrol.controllers;

import alphacontrol.models.Cliente;
import alphacontrol.views.cliente.ModalAdicionarCliente;
import javax.swing.JOptionPane;

public class ModalAdicionarClienteController {
    
    private ModalAdicionarCliente view;
    private ClienteController clienteController;
    private Cliente clienteParaEditar;

    public ModalAdicionarClienteController(ModalAdicionarCliente view, ClienteController clienteController) {
        this.view = view;
        this.clienteController = clienteController;
        this.clienteParaEditar = null;
        this.initController();
    }
    
    public ModalAdicionarClienteController(ModalAdicionarCliente view, ClienteController clienteController, Cliente cliente) {
        this.view = view;
        this.clienteController = clienteController;
        this.clienteParaEditar = cliente;
        this.initController();
    }

    private void initController() {
        this.view.getBtnSalvar().addActionListener(e -> salvarCliente());
    }

    private void salvarCliente() {
        try {
            Cliente dadosDosCampos = view.getClienteFromFields();
            
            if (clienteParaEditar == null) {
                clienteController.adicionar(dadosDosCampos);
                JOptionPane.showMessageDialog(view, "Cliente adicionado com sucesso!");
            } else {
                dadosDosCampos.setId(clienteParaEditar.getId());
                clienteController.atualizar(dadosDosCampos);
                JOptionPane.showMessageDialog(view, "Cliente atualizado com sucesso!");
            }
            
            view.dispose();

        } catch (NumberFormatException ex) {
            view.mostrarErro("Erro de formato. Verifique os campos numéricos.");
        } catch (Exception ex) {
            view.mostrarErro(ex.getMessage());
        }
    }
}