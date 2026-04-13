package alphacontrol.controllers.fiado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.dao.FiadoDAO;
import alphacontrol.dao.ItemVendaDAO;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;

public class FiadoController {

    private FiadoDAO fiadoDAO;
    private ClienteDAO clienteDAO;

    public FiadoController(FiadoDAO fiadoDAO, ClienteDAO clienteDAO, FluxoCaixaController fluxoCaixaController) {
        this.fiadoDAO = fiadoDAO;
        this.clienteDAO = clienteDAO;
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

        clienteDAO.buscarPorId(clienteId);
    }

    public void realizarPagamentoFiado(int clienteId, double valorPago, String nomeCliente) throws Exception {
        fiadoDAO.realizarPagamentoFiado(clienteId, valorPago, nomeCliente);
    }

    // MÉTODO QUE ESTAVA FALTANDO
    public void quitarDividaCompleta(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        double valorTotal = cliente.getDebito();

        fiadoDAO.quitarTudo(clienteId);
        clienteDAO.atualizarDebito(clienteId, -valorTotal);
    }

    public List<ItemVenda> listarProdutosFiado(int vendaId) {

        List<ItemVenda> itens = new ArrayList<>();

        try {

            ItemVendaDAO itemDAO = new ItemVendaDAO(null);
            ProdutoDAO produtoDAO = new ProdutoDAO(null);

            List<ItemVenda> itensVenda = itemDAO.listarItensPorVenda(vendaId);

            for (ItemVenda item : itensVenda) {

                Produto produto = produtoDAO.buscarPorId(item.getProdutoId());

                item.setProduto(produto);

                itens.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itens;
    }
}