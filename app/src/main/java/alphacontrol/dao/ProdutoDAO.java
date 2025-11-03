package alphacontrol.dao;

import alphacontrol.models.Produto;
import java.sql.*;
// 1. Importar a classe Statement
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    // 2. O construtor agora cria a tabela
    public ProdutoDAO(Connection connection) {
        this.connection = connection;
        
        // Garante que a tabela exista ao criar o DAO
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos ("
                       + "produto_id INT PRIMARY KEY AUTO_INCREMENT,"
                       + "nome VARCHAR(255) NOT NULL,"
                       + "categoria VARCHAR(100),"
                       + "valor_compra DECIMAL(10, 2) NOT NULL," // DECIMAL é melhor para dinheiro
                       + "valor_venda DECIMAL(10, 2) NOT NULL,"
                       + "qnt_estoque INT DEFAULT 0"
                       + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // Se não conseguir criar a tabela, o app não pode funcionar.
            throw new RuntimeException("Erro ao criar a tabela 'produtos': " + e.getMessage(), e);
        }
    }

    // CREATE
    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, categoria, valor_compra, valor_venda, qnt_estoque) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getValorCompra());
            stmt.setDouble(4, produto.getValorVenda());
            stmt.setInt(5, produto.getQntEstoque());
            stmt.executeUpdate();
        }
    }

    // READ
    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("produto_id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getDouble("valor_compra"),
                        rs.getDouble("valor_venda"),
                        rs.getInt("qnt_estoque")
                );
                produtos.add(produto);
            }
        }
        return produtos;
    }
    
    // READ (Pesquisa)
    public List<Produto> pesquisarProdutos(String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("produto_id"),
                            rs.getString("nome"),
                            rs.getString("categoria"),
                            rs.getDouble("valor_compra"),
                            rs.getDouble("valor_venda"),
                            rs.getInt("qnt_estoque")
                    );
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }

    // UPDATE
    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome=?, categoria=?, valor_compra=?, valor_venda=?, qnt_estoque=? WHERE produto_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getValorCompra());
            stmt.setDouble(4, produto.getValorVenda());
            stmt.setInt(5, produto.getQntEstoque());
            stmt.setInt(6, produto.getProdutoId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deletarProduto(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE produto_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}