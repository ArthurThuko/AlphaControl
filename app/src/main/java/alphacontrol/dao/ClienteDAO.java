package alphacontrol.dao;

import alphacontrol.models.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDAO {

    private Connection conexao;

    public ClienteDAO(Connection conexao) {
        if (conexao == null) {
            throw new RuntimeException("Erro: A conexão com o banco de dados é nula.");
        }
        this.conexao = conexao;
        criarTabelasSeNaoExistir();
    }

    private void criarTabelasSeNaoExistir() {
        String sqlEndereco = "CREATE TABLE IF NOT EXISTS endereco ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "rua TEXT,"
                + "bairro TEXT,"
                + "n_casa TEXT,"
                + "cep TEXT"
                + ");";
        
        String sqlCliente = "CREATE TABLE IF NOT EXISTS cliente ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "nome TEXT NOT NULL,"
                + "cpf TEXT,"
                + "telefone TEXT NOT NULL,"
                + "debito REAL DEFAULT 0.0,"
                + "endereco_id INTEGER UNIQUE,"
                + "FOREIGN KEY (endereco_id) REFERENCES endereco(id) ON DELETE CASCADE"
                + ");";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sqlEndereco);
            stmt.execute(sqlCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.*, e.*, c.id as cliente_id, e.id as endereco_id "
                + "FROM cliente c "
                + "JOIN endereco e ON c.endereco_id = e.id";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(instanciarCliente(rs));
            }
        }
        return clientes;
    }

    public List<Cliente> pesquisarClientes(String nome) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.*, e.*, c.id as cliente_id, e.id as endereco_id "
                + "FROM cliente c "
                + "JOIN endereco e ON c.endereco_id = e.id "
                + "WHERE c.nome LIKE ?";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(instanciarCliente(rs));
                }
            }
        }
        return clientes;
    }
    
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT c.*, e.*, c.id as cliente_id, e.id as endereco_id "
                + "FROM cliente c "
                + "JOIN endereco e ON c.endereco_id = e.id "
                + "WHERE c.id = ?";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return instanciarCliente(rs);
                }
            }
        }
        return null;
    }

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sqlEndereco = "INSERT INTO endereco (rua, bairro, n_casa, cep) VALUES (?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (nome, cpf, telefone, debito, endereco_id) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conexao.setAutoCommit(false);
            
            int enderecoId = -1;
            try (PreparedStatement pstmtEndereco = conexao.prepareStatement(sqlEndereco, Statement.RETURN_GENERATED_KEYS)) {
                pstmtEndereco.setString(1, cliente.getRua());
                pstmtEndereco.setString(2, cliente.getBairro());
                pstmtEndereco.setString(3, cliente.getNumeroCasa());
                pstmtEndereco.setString(4, cliente.getCep());
                pstmtEndereco.executeUpdate();
                
                try (ResultSet generatedKeys = pstmtEndereco.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        enderecoId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Falha ao criar endereço, nenhum ID obtido.");
                    }
                }
            }

            try (PreparedStatement pstmtCliente = conexao.prepareStatement(sqlCliente)) {
                pstmtCliente.setString(1, cliente.getNome());
                pstmtCliente.setString(2, cliente.getCpf());
                pstmtCliente.setString(3, cliente.getTelefone());
                pstmtCliente.setDouble(4, 0.0);
                pstmtCliente.setInt(5, enderecoId);
                pstmtCliente.executeUpdate();
            }
            
            conexao.commit();
            
        } catch (SQLException e) {
            conexao.rollback();
            throw new SQLException("Erro ao adicionar cliente: " + e.getMessage(), e);
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sqlCliente = "UPDATE cliente SET nome = ?, cpf = ?, telefone = ?, debito = ? WHERE id = ?";
        String sqlEndereco = "UPDATE endereco SET rua = ?, bairro = ?, n_casa = ?, cep = ? WHERE id = ?";

        try {
            conexao.setAutoCommit(false);

            try (PreparedStatement pstmtCliente = conexao.prepareStatement(sqlCliente)) {
                pstmtCliente.setString(1, cliente.getNome());
                pstmtCliente.setString(2, cliente.getCpf());
                pstmtCliente.setString(3, cliente.getTelefone());
                pstmtCliente.setDouble(4, cliente.getDebito());
                pstmtCliente.setInt(5, cliente.getId());
                pstmtCliente.executeUpdate();
            }
            
            try (PreparedStatement pstmtEndereco = conexao.prepareStatement(sqlEndereco)) {
                pstmtEndereco.setString(1, cliente.getRua());
                pstmtEndereco.setString(2, cliente.getBairro());
                pstmtEndereco.setString(3, cliente.getNumeroCasa());
                pstmtEndereco.setString(4, cliente.getCep());
                pstmtEndereco.setInt(5, cliente.getEnderecoId());
                pstmtEndereco.executeUpdate();
            }
            
            conexao.commit();

        } catch (SQLException e) {
            conexao.rollback();
            throw new SQLException("Erro ao atualizar cliente: " + e.getMessage(), e);
        } finally {
            conexao.setAutoCommit(true);
        }
    }
    
    public void quitarDivida(int clienteId) throws SQLException {
        String sql = "UPDATE cliente SET debito = 0.0 WHERE id = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
        }
    }

    public void deletarCliente(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    private Cliente instanciarCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("cliente_id"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("telefone"),
            rs.getString("rua"),
            rs.getString("bairro"),
            rs.getString("n_casa"),
            rs.getString("cep"),
            rs.getDouble("debito"),
            rs.getInt("endereco_id")
        );
    }
}