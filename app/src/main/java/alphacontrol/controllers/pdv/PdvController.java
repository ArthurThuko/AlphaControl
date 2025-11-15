package alphacontrol.controllers.pdv;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.produto.ProdutoController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class PdvController {

    private ProdutoController produtoController;
    private ClienteController clienteController;
    private FluxoCaixaController fluxoCaixaController;

    public PdvController(ProdutoController produtoController, ClienteController clienteController, FluxoCaixaController fluxoCaixaController) {
        this.produtoController = produtoController;
        this.clienteController = clienteController;
        this.fluxoCaixaController = fluxoCaixaController;
    }

    public void finalizarVenda(double valorTotal, String metodoPagamento) {
        try {
            String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String nomeEntrada = "Venda (" + metodoPagamento + ")";
            
            fluxoCaixaController.adicionarEntrada(nomeEntrada, valorTotal, dataHoje);
            
            JOptionPane.showMessageDialog(null, "Venda finalizada e registrada no caixa!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar venda no caixa: " + e.getMessage());
        }
    }
}