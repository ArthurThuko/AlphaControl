package alphacontrol.controllers;

import alphacontrol.models.Cliente;
import alphacontrol.views.fiado.ModalEditarFiado;
import javax.swing.JOptionPane;

public class ModalEditarFiadoController {
    
    private ModalEditarFiado view;
    private ClienteController clienteController;

    public ModalEditarFiadoController(ModalEditarFiado view, ClienteController clienteController) {
        this.view = view;
        this.clienteController = clienteController;
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
            
            Cliente clienteOriginal = view.getClienteOriginal();
            Cliente dadosEditados = view.getDadosEditadosDosCampos();
            
            clienteOriginal.setNome(dadosEditados.getNome());
            clienteOriginal.setCpf(dadosEditados.getCpf());
            clienteOriginal.setTelefone(dadosEditados.getTelefone());
            clienteOriginal.setCep(dadosEditados.getCep());
            clienteOriginal.setRua(dadosEditados.getRua());
            clienteOriginal.setBairro(dadosEditados.getBairro());
            clienteOriginal.setNumeroCasa(dadosEditados.getNumeroCasa());

            clienteController.atualizar(clienteOriginal);
            JOptionPane.showMessageDialog(view, "Cliente atualizado com sucesso!");
            
            view.dispose();

        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}