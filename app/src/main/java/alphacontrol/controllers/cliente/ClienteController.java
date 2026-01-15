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

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public boolean excluir(int idCliente) {
        try {
            clienteDAO.deletar(idCliente);
            return true;
        } catch (SQLException e) {
            mostrarErro("Não foi possível excluir o cliente.\n" + e.getMessage());
            return false;
        }
    }
}