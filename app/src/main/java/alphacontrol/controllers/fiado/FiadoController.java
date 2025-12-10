package alphacontrol.controllers.fiado;

import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.Cliente;
import java.sql.SQLException;
import java.util.List;

public class FiadoController {

    private FiadoDAO fiadoDAO;
    private ClienteDAO clienteDAO;
    public FiadoController(FiadoDAO fiadoDAO, ClienteDAO clienteDAO) {
        this.fiadoDAO = fiadoDAO;
        this.clienteDAO = clienteDAO;
    }

    public void adicionarFiado(Fiado fiado) throws SQLException {
        fiadoDAO.inserir(fiado);
        clienteDAO.atualizarDebito(fiado.getClienteId(), fiado.getValor());
    }
    
    public List<Fiado> listarPorCliente(int clienteId) throws SQLException {
        return fiadoDAO.listarPorCliente(clienteId);
    }

    public void pagarFiado(int clienteId, double valorPago) throws SQLException {
        fiadoDAO.pagarParcial(clienteId, valorPago);
        clienteDAO.atualizarDebito(clienteId, -valorPago);
        
        clienteDAO.buscarPorId(clienteId);
    }

    public void quitarDividaCompleta(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        double valorTotal = cliente.getDebito();
        
        fiadoDAO.quitarTudo(clienteId);
        clienteDAO.atualizarDebito(clienteId, -valorTotal);
    }
}