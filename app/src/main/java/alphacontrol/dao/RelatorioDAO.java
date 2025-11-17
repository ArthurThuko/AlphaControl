package alphacontrol.dao;

import alphacontrol.views.conexao.Conexao;
import alphacontrol.models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    // -----------------------------------------------------
    // MOVIMENTAÇÃO DE CAIXA
    // -----------------------------------------------------
    public List<MovimentacaoCaixa> listarMovimentacoes(Date inicio, Date fim) {
        String sql = "SELECT * FROM movimentacaoCaixa WHERE data BETWEEN ? AND ? ORDER BY data ASC";
        List<MovimentacaoCaixa> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, inicio);
            stmt.setDate(2, fim);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MovimentacaoCaixa mov = new MovimentacaoCaixa();
                mov.setId(rs.getInt("id"));
                mov.setNome(rs.getString("nome"));
                mov.setTipo(rs.getString("tipo"));
                mov.setValor(rs.getDouble("valor"));
                mov.setData(rs.getString("data"));
                lista.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // -----------------------------------------------------
    // VENDAS
    // -----------------------------------------------------
    public List<Venda> listarVendas(Date inicio, Date fim) {
        String sql = "SELECT * FROM venda WHERE data_venda BETWEEN ? AND ? ORDER BY data_venda ASC";
        List<Venda> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, inicio);
            stmt.setDate(2, fim);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venda v = new Venda();
                v.setVendaId(rs.getInt("id"));
                v.setDataVenda(rs.getDate("data_venda"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // -----------------------------------------------------
    // FIADOS
    // -----------------------------------------------------
    public List<Fiado> listarFiados(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT * FROM fiado WHERE data BETWEEN ? AND ? ORDER BY data ASC";
        List<Fiado> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Converte LocalDateTime → Timestamp do JDBC
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fiado f = new Fiado();

                f.setId(rs.getInt("id"));
                f.setClienteId(rs.getInt("cliente_id"));
                f.setValor(rs.getDouble("valor"));

                // Converte Timestamp → LocalDateTime
                Timestamp ts = rs.getTimestamp("data");
                if (ts != null) {
                    f.setData(ts.toLocalDateTime());
                }

                f.setStatus(rs.getString("status"));

                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ProdutoMaisVendido> listarProdutosMaisVendidos(Date inicio, Date fim) {
        String sql = "SELECT p.nome, SUM(iv.quantidade) AS total " +
                "FROM itens_venda iv " +
                "JOIN produtos p ON p.id = iv.produto_id " +
                "JOIN vendas v ON v.id = iv.venda_id " +
                "WHERE v.data_venda BETWEEN ? AND ? " +
                "GROUP BY p.nome " +
                "ORDER BY total DESC";

        List<ProdutoMaisVendido> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, inicio);
            stmt.setDate(2, fim);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProdutoMaisVendido pmv = new ProdutoMaisVendido();
                pmv.setNome(rs.getString("nome"));
                pmv.setQuantidade(rs.getInt("total"));
                lista.add(pmv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
