package alphacontrol.controllers.modais;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import alphacontrol.views.fiado.ModalAdicionarFiado;
import alphacontrol.views.cliente.ModalBuscaCliente;

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
        view.getBtnSalvar().addActionListener(e -> salvarFiado());
        
        view.getBtnAdicionarCliente().addActionListener(e -> {
            view.getParentView().abrirModalAdicionarCliente();
        });
        
        view.getBtnBuscarCliente().addActionListener(e -> buscarCliente());
    }
    
    private void buscarCliente() {
        ModalBuscaCliente modalBusca = new ModalBuscaCliente(view, clienteController);
        modalBusca.setVisible(true); 
        
        Cliente cliente = modalBusca.getClienteSelecionado();
        if (cliente != null) {
            view.setClienteSelecionado(cliente);
        }
    }

    private void salvarFiado() {
        Cliente clienteSelecionado = view.getClienteSelecionado();
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