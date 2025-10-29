package alphacontrol.dao;

import alphacontrol.models.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
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
