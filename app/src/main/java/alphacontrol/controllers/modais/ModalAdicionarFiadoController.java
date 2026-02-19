package alphacontrol.controllers.modais;

import java.time.LocalDateTime;

import javax.swing.JOptionPane;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import alphacontrol.views.fiado.ModalAdicionarFiado;
import alphacontrol.views.fiado.TelaFiado;

public class ModalAdicionarFiadoController {

    private ModalAdicionarFiado view;
    private FiadoController fiadoController;
    public ModalAdicionarFiadoController(ModalAdicionarFiado view, FiadoController fc, ClienteController cc) {
        this.view = view;
        this.fiadoController = fc;
        initController();
    }

    private void initController() {
        for (java.awt.event.ActionListener al : view.getBtnSalvar().getActionListeners()) {
            view.getBtnSalvar().removeActionListener(al);
        }
        view.getBtnSalvar().addActionListener(e -> salvarFiado());
    }

    private void salvarFiado() {
        Cliente clienteSelecionado = view.getClienteSelecionado();
        String valorTexto = view.getTxtValor().getText().trim();
        
        if (valorTexto.equals("0,00") || valorTexto.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Informe um valor válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(view, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto.replace(",", "."));
            if (valor <= 0) throw new NumberFormatException();

            Fiado novoFiado = new Fiado();
            novoFiado.setClienteId(clienteSelecionado.getId());
            novoFiado.setValor(valor);
            novoFiado.setData(LocalDateTime.now());
            novoFiado.setStatus("PENDENTE"); 

            fiadoController.adicionarFiado(novoFiado);
            
            JOptionPane.showMessageDialog(view, "Fiado registrado com sucesso!");

            // SOLUÇÃO PARA O ERRO GETPARENTVIEW:
            // Pegamos o 'Owner' (dono) do JDialog, que foi passado no construtor da View
            java.awt.Window owner = view.getOwner();
            if (owner instanceof TelaFiado) {
                ((TelaFiado) owner).atualizarTabela();
            }
            
            view.dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Valor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}