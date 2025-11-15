package alphacontrol.controllers.fiado;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.Cliente; // Importar o modelo Cliente
import javax.swing.JOptionPane;
import java.sql.SQLException; // Importar SQLException
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; // Importar List

public class FiadoController {

    private FiadoDAO fiadoDAO;
    private ClienteDAO clienteDAO;
    private FluxoCaixaController fluxoCaixaController;

    public FiadoController(FiadoDAO fiadoDAO, ClienteDAO clienteDAO, FluxoCaixaController fluxoCaixaController) {
        this.fiadoDAO = fiadoDAO;
        this.clienteDAO = clienteDAO;
        this.fluxoCaixaController = fluxoCaixaController;
    }

    // MÉTODO QUE ESTAVA FALTANDO
    public void adicionarFiado(Fiado fiado) throws SQLException {
        fiadoDAO.inserir(fiado);
        clienteDAO.atualizarDebito(fiado.getClienteId(), fiado.getValor());
    }
    
    // MÉTODO QUE ESTAVA FALTANDO
    public List<Fiado> listarPorCliente(int clienteId) throws SQLException {
        return fiadoDAO.listarPorCliente(clienteId);
    }

    // MÉTODO QUE ESTAVA FALTANDO
    public void pagarFiado(int clienteId, double valorPago) throws SQLException {
        fiadoDAO.pagarParcial(clienteId, valorPago);
        clienteDAO.atualizarDebito(clienteId, -valorPago);
        
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nomeEntrada = "Pgto. Parcial Fiado: " + cliente.getNome();
        fluxoCaixaController.adicionarEntrada(nomeEntrada, valorPago, dataHoje);
    }

    // MÉTODO QUE ESTAVA FALTANDO
    public void quitarDividaCompleta(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        double valorTotal = cliente.getDebito();
        
        fiadoDAO.quitarTudo(clienteId);
        clienteDAO.atualizarDebito(clienteId, -valorTotal);
        
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nomeEntrada = "Pgto. Total Fiado: " + cliente.getNome();
        fluxoCaixaController.adicionarEntrada(nomeEntrada, valorTotal, dataHoje);
    }
}