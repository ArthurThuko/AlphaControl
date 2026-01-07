package alphacontrol.controllers.fiado;

import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.FiadoItemDAO;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.FiadoItem;

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

    public int inserirFiadoComItens(Fiado fiado, List<FiadoItem> itens) {
        try {
            fiado.setStatus("PENDENTE");
            int fiadoId = fiadoDAO.inserir(fiado);

            for (FiadoItem item : itens) {
                item.setFiadoId(fiadoId);
                fiadoItemDAO.inserir(item);
            }

            return fiadoId;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar fiado", e);
        }
    }

    public List<Fiado> listarPorCliente(int clienteId) throws SQLException {
        return fiadoDAO.listarPorCliente(clienteId);
    }

    public List<FiadoItem> listarItens(int fiadoId) {
        try {
            return fiadoItemDAO.listarPorFiado(fiadoId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do fiado", e);
        }
    }

    public void pagarFiado(int clienteId, double valorPago) throws SQLException {
        Fiado pagamento = new Fiado();
        pagamento.setClienteId(clienteId);
        pagamento.setValor(valorPago);
        pagamento.setStatus("PAGAMENTO");
        pagamento.setData(java.time.LocalDateTime.now());

        fiadoDAO.inserir(pagamento);
    }

    public void quitarDividaCompleta(int clienteId) throws SQLException {
        double saldo = clienteDAO.calcularDebito(clienteId);

        if (saldo <= 0)
            return;

        Fiado pagamento = new Fiado();
        pagamento.setClienteId(clienteId);
        pagamento.setValor(saldo);
        pagamento.setStatus("PAGAMENTO");
        pagamento.setData(java.time.LocalDateTime.now());

        fiadoDAO.inserir(pagamento);
    }
}