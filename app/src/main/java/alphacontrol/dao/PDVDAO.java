package alphacontrol.dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import alphacontrol.models.FormaPagamento;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Venda;

public class PDVDAO {

    private final Connection conexao;

    public PDVDAO(Connection conexao) {
        this.conexao = conexao;
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        try (Statement stmt = conexao.createStatement()) {

            // TABELA VENDA
            String sqlVenda = "CREATE TABLE IF NOT EXISTS venda ("
                    + "venda_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "cliente VARCHAR(100),"
                    + "forma_pagamento VARCHAR(50) NOT NULL,"
                    + "total DECIMAL(10,2) NOT NULL,"
                    + "data_venda DATE NOT NULL"
                    + ")";
            stmt.executeUpdate(sqlVenda);

            // TABELA ITEM_VENDA
            String sqlItem = "CREATE TABLE IF NOT EXISTS item_venda ("
                    + "item_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "venda_id INT NOT NULL,"
                    + "produto_id INT NOT NULL,"
                    + "quantidade INT NOT NULL,"
                    + "valor_unitario DECIMAL(10,2),"
                    + "valor_total DECIMAL(10,2),"
                    + "FOREIGN KEY (venda_id) REFERENCES venda(venda_id),"
                    + "FOREIGN KEY (produto_id) REFERENCES produtos(produto_id)"
                    + ")";
            stmt.executeUpdate(sqlItem);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas do PDV: " + e.getMessage(), e);
        }
    }

    public int registrarVenda(Venda venda) {
        String sql = "INSERT INTO venda (cliente, forma_pagamento, total, data_venda) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, venda.getCliente());
            stmt.setString(2, venda.getFormaPagamento().getNome());
            stmt.setDouble(3, venda.getTotal());

            java.sql.Date sqlDate = new java.sql.Date(venda.getDataVenda().getTime());
            stmt.setDate(4, sqlDate);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar venda: " + e.getMessage(), e);
        }

        return -1;
    }

    public void registrarItemVenda(int idVenda, ItemVenda item) {
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade, valor_unitario, valor_total) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getValorUnitario());
            stmt.setDouble(5, item.getValorTotal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar item da venda: " + e.getMessage(), e);
        }
    }

    public List<Venda> listarVendas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int id = rs.getInt("venda_id");
                String cliente = rs.getString("cliente");
                double total = rs.getDouble("total");

                String nomeForma = rs.getString("forma_pagamento");
                FormaPagamento forma = new FormaPagamento(nomeForma);

                Date data = new Date(rs.getDate("data_venda").getTime());

                Venda venda = new Venda(id, cliente, forma, total, data);

                vendas.add(venda);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }

        return vendas;
    }
}