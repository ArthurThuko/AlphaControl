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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    telefone TEXT,
                    rua TEXT,
                    bairro TEXT,
                    debito REAL DEFAULT 0
                )
            """;
            stmt.execute(sql);
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
                int id = rs.getInt("id");

                Cliente c = new Cliente(
                        id,
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("rua"),
                        rs.getString("bairro"),
                        0
                );

                c.setDebito(calcularDebito(id));
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
                    int id = rs.getInt("id");

                    Cliente c = new Cliente(
                            id,
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("rua"),
                            rs.getString("bairro"),
                            0
                    );

                    c.setDebito(calcularDebito(id));
                    clientes.add(c);
                }
            }
        }
        return clientes;
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

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("rua"),
                        rs.getString("bairro"),
                        0
                );

                cliente.setDebito(calcularDebito(id));
            }
        }
        return cliente;
    }

    public double calcularDebito(int clienteId) throws SQLException {
        String sql = """
            SELECT
                COALESCE(SUM(
                    CASE
                        WHEN status = 'PENDENTE' THEN valor
                        WHEN status = 'PAGAMENTO' THEN -valor
                        ELSE 0
                    END
                ), 0) AS debito
            FROM fiado
            WHERE cliente_id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("debito");
            }
        }
        return 0;
    }
}