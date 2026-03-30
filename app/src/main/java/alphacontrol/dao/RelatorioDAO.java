package alphacontrol.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import alphacontrol.conexao.Conexao;
import alphacontrol.models.Fiado;
import alphacontrol.models.FormaPagamento;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.models.ProdutoMaisVendido;
import alphacontrol.models.Venda;

public class RelatorioDAO {

    // -----------------------------------------------------
    // MOVIMENTAÇÃO DE CAIXA
    // -----------------------------------------------------
    public List<MovimentacaoCaixa> listarMovimentacoes(Date inicio, Date fim) {

        List<MovimentacaoCaixa> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao()) {

            // =========================
            // 1. ENTRADAS - VENDAS
            // =========================

            String sqlVendas = """
                        SELECT DATE(data_venda) as data, SUM(total) as total
                        FROM venda
                        WHERE forma_pagamento <> 'FIADO'
                        AND DATE(data_venda) BETWEEN ? AND ?
                        GROUP BY DATE(data_venda)
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlVendas)) {

                stmt.setDate(1, inicio);
                stmt.setDate(2, fim);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {

                    MovimentacaoCaixa mov = new MovimentacaoCaixa();

                    mov.setNome("Vendas do dia");
                    mov.setTipo("ENTRADA");
                    mov.setValor(rs.getDouble("total"));
                    mov.setData(rs.getString("data"));

                    lista.add(mov);
                }
            }

            // =========================
            // 2. SAÍDA - FIADOS
            // =========================

            String sqlFiado = """
                        SELECT DATE(data) as data, SUM(valor) as total
                        FROM fiado
                        WHERE DATE(data) BETWEEN ? AND ?
                        GROUP BY DATE(data)
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlFiado)) {

                stmt.setDate(1, inicio);
                stmt.setDate(2, fim);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {

                    MovimentacaoCaixa mov = new MovimentacaoCaixa();

                    mov.setNome("Fiado clientes");
                    mov.setTipo("SAIDA");
                    mov.setValor(rs.getDouble("total"));
                    mov.setData(rs.getString("data"));

                    lista.add(mov);
                }
            }

            // =========================
            // 3. MOVIMENTAÇÕES MANUAIS
            // =========================

            String sqlManual = """
                        SELECT * FROM movimentacaocaixa
                        WHERE data BETWEEN ? AND ?
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlManual)) {

                stmt.setDate(1, inicio);
                stmt.setDate(2, fim);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {

                    MovimentacaoCaixa mov = new MovimentacaoCaixa();

                    mov.setId(rs.getInt("idMovimentacaoCaixa"));
                    mov.setNome(rs.getString("nome"));
                    mov.setTipo(rs.getString("tipo"));
                    mov.setValor(rs.getDouble("valor"));
                    mov.setData(rs.getString("data"));

                    lista.add(mov);
                }
            }

        } catch (Exception e) {
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
                v.setVendaId(rs.getInt("venda_id"));
                v.setDataVenda(rs.getDate("data_venda"));
                v.setTotal(rs.getDouble("total"));

                String formaPg = rs.getString("forma_pagamento");
                FormaPagamento fp = new FormaPagamento(formaPg);

                v.setFormaPagamento(fp);
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

            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fiado f = new Fiado();

                f.setId(rs.getInt("id"));
                f.setClienteId(rs.getInt("cliente_id"));
                f.setValor(rs.getDouble("valor"));

                int vendaId = rs.getInt("venda_id");
                f.setVendaId(rs.wasNull() ? null : vendaId);

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
                "FROM item_venda iv " +
                "JOIN produtos p ON p.produto_id = iv.produto_id " +
                "JOIN venda v ON v.venda_id = iv.venda_id " +
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
