package alphacontrol.controllers;

import alphacontrol.dao.ClienteDAO;
import alphacontrol.dao.FiadoDAO;
import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import java.sql.SQLException;

public class FiadoController {

    private FiadoDAO fiadoDAO;
    private ClienteDAO clienteDAO;

    public FiadoController(FiadoDAO fiadoDAO, ClienteDAO clienteDAO) {
        this.fiadoDAO = fiadoDAO;
        this.clienteDAO = clienteDAO;
    }

    public void adicionarFiado(Fiado fiado) throws SQLException {
        try {
            fiadoDAO.adicionarFiado(fiado);
            
            Cliente cliente = clienteDAO.buscarPorId(fiado.getClienteId());
            if (cliente != null) {
                double novoDebito = cliente.getDebito() + fiado.getValor();
                cliente.setDebito(novoDebito);
                clienteDAO.atualizarCliente(cliente);
            } else {
                throw new SQLException("Cliente não encontrado para atualizar o débito.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao processar fiado e atualizar débito: " + e.getMessage(), e);
        }
    }
}