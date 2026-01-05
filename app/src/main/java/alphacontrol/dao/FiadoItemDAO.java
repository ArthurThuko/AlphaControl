package alphacontrol.dao;

import alphacontrol.models.FiadoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiadoItemDAO {

    private Connection conexao;

    public FiadoItemDAO(Connection conexao) {
        this.conexao = conexao;
        if (this.conexao == null) {
            throw new RuntimeException("Erro: conexão com banco é nula.");
        }
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS fiado_item ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "fiado_id INTEGER NOT NULL,"
                + "produto_id INTEGER NOT NULL,"
                + "quantidade INTEGER NOT NULL,"
                + "valor_unitario REAL NOT NULL,"
                + "FOREIGN KEY (fiado_id) REFERENCES fiado(id),"
                + "FOREIGN KEY (produto_id) REFERENCES produtos(produto_id)"
                + ")";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela fiado_item: " + e.getMessage());
        }
    }

    public void inserir(FiadoItem item) throws SQLException {

        String sql = """
                    INSERT INTO fiado_item
                    (fiado_id, produto_id, quantidade, valor_unitario)
                    VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, item.getFiadoId());
            pstmt.setInt(2, item.getProdutoId());
            pstmt.setInt(3, item.getQuantidade());
            pstmt.setDouble(4, item.getValorUnitario());
            pstmt.executeUpdate();
        }
    }

    public List<FiadoItem> listarPorFiado(int fiadoId) throws SQLException {
        List<FiadoItem> itens = new ArrayList<>();

        String sql = """
                    SELECT *
                    FROM fiado_item
                    WHERE fiado_id = ?
                """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, fiadoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FiadoItem item = new FiadoItem(
                        rs.getInt("id"),
                        rs.getInt("fiado_id"),
                        rs.getInt("produto_id"),
                        rs.getInt("quantidade"),
                        rs.getDouble("valor_unitario"));
                itens.add(item);
            }
        }

        return itens;
    }

    public void deletarPorFiado(int fiadoId) throws SQLException {

        String sql = "DELETE FROM fiado_item WHERE fiado_id = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, fiadoId);
            pstmt.executeUpdate();
        }
    }

    private FiadoItem instanciarItem(ResultSet rs) throws SQLException {

        FiadoItem item = new FiadoItem();
        item.setId(rs.getInt("id"));
        item.setFiadoId(rs.getInt("fiado_id"));
        item.setProdutoId(rs.getInt("produto_id"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setValorUnitario(rs.getDouble("valor_unitario"));
        return item;
    }
}