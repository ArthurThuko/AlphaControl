package alphacontrol.dao;

import alphacontrol.models.Produto;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
        
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos ("
                         + "produto_id INT PRIMARY KEY AUTO_INCREMENT,"
                         + "nome VARCHAR(255) NOT NULL,"
                         + "categoria VARCHAR(100),"
                         + "valor_compra DECIMAL(10, 2) NOT NULL,"
                         + "valor_venda DECIMAL(10, 2) NOT NULL,"
                         + "qnt_estoque INT DEFAULT 0"
                         + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela 'produtos': " + e.getMessage(), e);
        }
        
        adicionarColunaQntMinima();
    }
    
    private void adicionarColunaQntMinima() {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet columns = dbm.getColumns(null, null, "produtos", "qnt_minima");
            
            if (!columns.next()) {
                try (Statement stmt = connection.createStatement()) {
                    String sql = "ALTER TABLE produtos ADD COLUMN qnt_minima INT DEFAULT 5";
                    stmt.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar/adicionar coluna 'qnt_minima': " + e.getMessage());
        }
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, categoria, valor_compra, valor_venda, qnt_estoque, qnt_minima) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getValorCompra());
            stmt.setDouble(4, produto.getValorVenda());
            stmt.setInt(5, produto.getQntEstoque());
            stmt.setInt(6, produto.getQntMinima()); 
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
                        rs.getString("categoria"),
                        rs.getDouble("valor_compra"),
                        rs.getDouble("valor_venda"),
                        rs.getInt("qnt_estoque"),
                        rs.getInt("qnt_minima") 
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
                            rs.getString("categoria"),
                            rs.getDouble("valor_compra"),
                            rs.getDouble("valor_venda"),
                            rs.getInt("qnt_estoque"),
                            rs.getInt("qnt_minima") 
                    );
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome=?, categoria=?, valor_compra=?, valor_venda=?, qnt_estoque=?, qnt_minima=? WHERE produto_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getValorCompra());
            stmt.setDouble(4, produto.getValorVenda());
            stmt.setInt(5, produto.getQntEstoque());
            stmt.setInt(6, produto.getQntMinima()); 
            stmt.setInt(7, produto.getProdutoId()); 
            stmt.executeUpdate();
        }
    }

    public void deletarProduto(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE produto_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}