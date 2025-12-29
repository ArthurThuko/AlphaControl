package alphacontrol.controllers.fiado;

import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.FiadoItemDAO;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.FiadoItem;
import alphacontrol.models.Cliente;

import java.sql.SQLException;
import java.util.List;

public class FiadoController {

    private FiadoDAO fiadoDAO;
    private FiadoItemDAO fiadoItemDAO;
    private ClienteDAO clienteDAO;

    public FiadoController(
            FiadoDAO fiadoDAO,
            FiadoItemDAO fiadoItemDAO,
            ClienteDAO clienteDAO) {

        this.fiadoDAO = fiadoDAO;
        this.fiadoItemDAO = fiadoItemDAO;
        this.clienteDAO = clienteDAO;
    }

    public int inserirFiadoComItens(Fiado fiado, List<FiadoItem> itens) throws SQLException {
        int fiadoId = fiadoDAO.inserir(fiado);

        for (FiadoItem item : itens) {
            item.setFiadoId(fiadoId);
            fiadoItemDAO.inserir(item);
        }

        clienteDAO.quitarDivida(fiado.getClienteId(), fiado.getValor());

        return fiadoId;
    }

    public void adicionarFiado(Fiado fiado) throws SQLException {
        fiadoDAO.inserir(fiado);
        clienteDAO.quitarDivida(fiado.getClienteId(), fiado.getValor());
    }

    public List<Fiado> listarPorCliente(int clienteId) throws SQLException {
        return fiadoDAO.listarPorCliente(clienteId);
    }

    public void pagarFiado(int clienteId, double valorPago) throws SQLException {
        fiadoDAO.pagarParcial(clienteId, valorPago);
        clienteDAO.quitarDivida(clienteId, -valorPago);
    }

    public void quitarDividaCompleta(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        double valorTotal = cliente.getDebito();

        fiadoDAO.quitarTudo(clienteId);
        clienteDAO.quitarDivida(clienteId, -valorTotal);
    }
}