package alphacontrol.controllers.modais;

import alphacontrol.controllers.cliente.ClienteController;
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
            if (!view.validarCampos()) {
                return;
            }
            
            Cliente dadosDosCampos = view.getClienteFromFields();
            
            if (clienteParaEditar == null) {
                clienteController.adicionar(dadosDosCampos);
                JOptionPane.showMessageDialog(view, "Cliente adicionado com sucesso!");
            } else {
                dadosDosCampos.setId(clienteParaEditar.getId());
                dadosDosCampos.setEnderecoId(clienteParaEditar.getEnderecoId());
                dadosDosCampos.setDebito(clienteParaEditar.getDebito());

                clienteController.atualizar(dadosDosCampos);
                JOptionPane.showMessageDialog(view, "Cliente atualizado com sucesso!");
            }
            
            view.dispose();

        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}