package alphacontrol.dao;

import alphacontrol.models.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;

        try (Statement stmt = connection.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                    produto_id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    qnt_estoque INT NOT NULL DEFAULT 0,
                    preco DECIMAL(10,2) NOT NULL
                )
            """;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela 'produtos'", e);
        }
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = """
            INSERT INTO produtos (nome, qnt_estoque, preco)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQntEstoque());
            stmt.setDouble(3, produto.getPreco());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("produto_id"),
                        rs.getString("nome"),
                        rs.getInt("qnt_estoque"),
                        rs.getDouble("preco")
                );
                produtos.add(produto);
            }
        }
        return produtos;
    }

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
                            rs.getInt("qnt_estoque"),
                            rs.getDouble("preco")
                    );
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = """
            UPDATE produtos
            SET nome = ?, qnt_estoque = ?, preco = ?
            WHERE produto_id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQntEstoque());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getProdutoId());
            stmt.executeUpdate();
        }
    }

    public void deletarProduto(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE produto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}