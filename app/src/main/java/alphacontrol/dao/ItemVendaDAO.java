package alphacontrol.dao;

import java.sql.*;
import java.util.*;

import alphacontrol.models.ItemVenda;

public class ItemVendaDAO {

    private final Connection conexao;

    public ItemVendaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarItem(ItemVenda item) throws SQLException {
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade, valor_unitario, valor_total) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, item.getVendaId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getValorUnitario());
            stmt.setDouble(5, item.getValorTotal());
            stmt.executeUpdate();
        }
    }

    public List<ItemVenda> listarItensPorVenda(int vendaId) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();

        String sql = "SELECT * FROM item_venda WHERE venda_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenda item = new ItemVenda(
                            rs.getInt("item_id"),
                            rs.getInt("venda_id"),
                            rs.getInt("produto_id"),
                            rs.getInt("quantidade"),
                            rs.getDouble("valor_unitario"),
                            rs.getDouble("valor_total"),
                            null // Produto não carregado aqui
                    );
                    itens.add(item);
                }
            }
        }

        return itens;
    }

    public void deletarItensPorVenda(int vendaId) throws SQLException {
        String sql = "DELETE FROM item_venda WHERE venda_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            stmt.executeUpdate();
        }
    }
}