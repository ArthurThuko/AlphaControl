package alphacontrol.controllers.cliente;

import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Cliente;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente buscarPorId(int id) {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (SQLException e) {
            mostrarErro("Erro ao buscar cliente: " + e.getMessage());
            return null;
        }
    }

    public List<Cliente> listar() {
        try {
            return clienteDAO.listar();
        } catch (SQLException e) {
            mostrarErro("Erro ao listar clientes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Cliente> pesquisar(String nome) {
        try {
            return clienteDAO.pesquisar(nome);
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar clientes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void adicionar(Cliente cliente) throws SQLException {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new SQLException("O nome do cliente é obrigatório.");
        }

        clienteDAO.adicionar(cliente);
    }

    public void atualizar(Cliente cliente) throws SQLException {
        if (cliente.getId() == 0) {
            throw new SQLException("Cliente inválido para atualização.");
        }
        clienteDAO.atualizar(cliente);
    }

    public void quitarDivida(Cliente cliente, double valorPago) {

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente inválido.");
        }

        if (valorPago <= 0) {
            throw new IllegalArgumentException("O valor pago deve ser maior que zero.");
        }

        if (cliente.getDebito() <= 0) {
            throw new IllegalStateException("Este cliente não possui dívida.");
        }

        try {
            clienteDAO.quitarDivida(cliente.getId(), valorPago);

            double novoDebito = cliente.getDebito() - valorPago;
            cliente.setDebito(Math.max(0, novoDebito));

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao quitar dívida do cliente.", e);
        }
    }

    public void deletar(int id, String nome) {
        int resposta = JOptionPane.showConfirmDialog(
                null,
                "Tem certeza que deseja excluir o cliente '" + nome + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                clienteDAO.deletar(id);
            } catch (SQLException e) {
                mostrarErro("Erro ao deletar cliente: " + e.getMessage());
            }
        }
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}