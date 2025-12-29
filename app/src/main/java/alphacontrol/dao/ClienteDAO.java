package alphacontrol.dao;

import alphacontrol.models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;

        try (Statement stmt = connection.createStatement()) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS clientes (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            nome VARCHAR(255) NOT NULL,
                            telefone VARCHAR(20),
                            rua VARCHAR(255),
                            bairro VARCHAR(100),
                            debito DECIMAL(10,2) DEFAULT 0
                        )
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela 'clientes'", e);
        }
    }

    public void adicionar(Cliente cliente) throws SQLException {
        String sql = """
                    INSERT INTO clientes (nome, telefone, rua, bairro, debito)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getRua());
            stmt.setString(4, cliente.getBairro());
            stmt.setDouble(5, cliente.getDebito());
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("rua"),
                        rs.getString("bairro"),
                        rs.getDouble("debito"));
                clientes.add(c);
            }
        }
        return clientes;
    }

    public List<Cliente> pesquisar(String nome) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nome LIKE ? ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("rua"),
                            rs.getString("bairro"),
                            rs.getDouble("debito"));
                    clientes.add(c);
                }
            }
        }
        return clientes;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("rua"),
                            rs.getString("bairro"),
                            rs.getDouble("debito"));
                }
            }
        }
        return null;
    }

    public void atualizar(Cliente cliente) throws SQLException {
        String sql = """
                    UPDATE clientes
                    SET nome = ?, telefone = ?, rua = ?, bairro = ?, debito = ?
                    WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getRua());
            stmt.setString(4, cliente.getBairro());
            stmt.setDouble(5, cliente.getDebito());
            stmt.setInt(6, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void quitarDivida(int idCliente, double valorPago) throws SQLException {
        String sql = """
                    UPDATE clientes
                    SET debito = CASE
                        WHEN debito - ? < 0 THEN 0
                        ELSE debito - ?
                    END
                    WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, valorPago);
            stmt.setDouble(2, valorPago);
            stmt.setInt(3, idCliente);
            stmt.executeUpdate();
        }
    }
}