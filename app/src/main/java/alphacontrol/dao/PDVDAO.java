package alphacontrol.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import alphacontrol.conexao.Conexao;
import alphacontrol.models.FormaPagamento;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Venda;

public class PDVDAO {

    private final Connection conexao;

    public PDVDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Construtor para consultas simples
    public PDVDAO() {
        this.conexao = null;
    }

    /**
     * Este é o método MÁGICO. Ele faz tudo: grava venda, itens e se for fiado, gera
     * a dívida.
     * 
     * @param venda     O objeto da venda
     * @param itens     A lista de produtos que ele comprou
     * @param clienteId O ID do cliente (OBRIGATÓRIO SE FOR FIADO, pode ser null se
     *                  for à vista)
     */
    public boolean realizarVendaCompleta(Venda venda, List<ItemVenda> itens, Integer clienteId) {

        // Verifica se é fiado e se temos o cliente
        boolean isFiado = venda.getFormaPagamento().getNome().equalsIgnoreCase("FIADO");
        if (isFiado && (clienteId == null || clienteId <= 0)) {
            throw new IllegalArgumentException("Para venda no FIADO, é obrigatório informar o Cliente cadastrado!");
        }

        PreparedStatement stmtVenda = null;
        PreparedStatement stmtItem = null;
        PreparedStatement stmtFiado = null;
        PreparedStatement stmtUpdateCliente = null;
        ResultSet rs = null;

        try {
            // 1. Desliga o save automático para iniciar a transação manual
            conexao.setAutoCommit(false);

            // ---------------------------------------------------
            // PASSO 1: Registrar a Venda
            // ---------------------------------------------------
            String sqlVenda = "INSERT INTO venda (cliente, forma_pagamento, total, data_venda) VALUES (?, ?, ?, ?)";
            stmtVenda = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            stmtVenda.setString(1, venda.getCliente()); // Nome do cliente
            stmtVenda.setString(2, venda.getFormaPagamento().getNome());
            stmtVenda.setDouble(3, venda.getTotal());
            stmtVenda.setTimestamp(4, new Timestamp(venda.getDataVenda().getTime()));
            stmtVenda.executeUpdate();

            // Pegar o ID da venda gerado
            rs = stmtVenda.getGeneratedKeys();
            int idVendaGerada = -1;
            if (rs.next()) {
                idVendaGerada = rs.getInt(1);
            } else {
                throw new SQLException("Falha ao gerar ID da venda.");
            }

            // ---------------------------------------------------
            // PASSO 2: Registrar os Itens
            // ---------------------------------------------------
            String sqlItem = "INSERT INTO item_venda (venda_id, produto_id, quantidade, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";
            stmtItem = conexao.prepareStatement(sqlItem);

            for (ItemVenda item : itens) {
                stmtItem.setInt(1, idVendaGerada);
                stmtItem.setInt(2, item.getProduto().getProdutoId());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getValorUnitario());
                stmtItem.setDouble(5, item.getValorTotal());
                stmtItem.addBatch(); // Adiciona ao lote para salvar tudo de uma vez
            }
            stmtItem.executeBatch();

            // ---------------------------------------------------
            // PASSO 3: Lógica do FIADO (Se for o caso)
            // ---------------------------------------------------
            if (isFiado) {
                // A. Criar registro na tabela FIADO
                String sqlFiado = "INSERT INTO fiado (cliente_id, venda_id, valor, data, status) VALUES (?, ?, ?, ?, ?)";
                stmtFiado = conexao.prepareStatement(sqlFiado);
                stmtFiado.setInt(1, clienteId);
                stmtFiado.setInt(2, idVendaGerada);
                stmtFiado.setDouble(3, venda.getTotal());
                stmtFiado.setTimestamp(4, new Timestamp(venda.getDataVenda().getTime()));
                stmtFiado.setString(5, "PENDENTE"); // Status inicial é sempre pendente
                stmtFiado.executeUpdate();

                // B. Atualizar o débito total na tabela CLIENTE
                String sqlUpdateCliente = "UPDATE cliente SET debito = debito + ? WHERE id = ?";
                stmtUpdateCliente = conexao.prepareStatement(sqlUpdateCliente);
                stmtUpdateCliente.setDouble(1, venda.getTotal());
                stmtUpdateCliente.setInt(2, clienteId);
                stmtUpdateCliente.executeUpdate();
            }

            // ---------------------------------------------------
            // FINALIZAÇÃO: Deu tudo certo? Salva no banco (COMMIT)
            // ---------------------------------------------------
            conexao.commit();
            return true;

        } catch (SQLException | IllegalArgumentException e) {
            try {
                // Se deu erro em QUALQUER etapa, desfaz tudo (ROLLBACK)
                conexao.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao processar venda: " + e.getMessage(), e);
        } finally {
            // Restaura o comportamento padrão e fecha recursos
            try {
                conexao.setAutoCommit(true);
                if (rs != null)
                    rs.close();
                if (stmtVenda != null)
                    stmtVenda.close();
                if (stmtItem != null)
                    stmtItem.close();
                if (stmtFiado != null)
                    stmtFiado.close();
                if (stmtUpdateCliente != null)
                    stmtUpdateCliente.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método antigo de listagem continua igual...
    public List<Venda> listarPorData(String dataBR) {

        List<Venda> lista = new ArrayList<>();

        String sql = """
                    SELECT venda_id, cliente, forma_pagamento, total, data_venda
                    FROM venda
                    WHERE DATE(data_venda) = ?
                    ORDER BY data_venda DESC
                """;

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            SimpleDateFormat br = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dataSQL = sqlFormat.format(br.parse(dataBR));

            stmt.setString(1, dataSQL);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Venda v = new Venda();
                    v.setVendaId(rs.getInt("venda_id"));
                    v.setCliente(rs.getString("cliente"));
                    v.setTotal(rs.getDouble("total"));
                    v.setDataVenda(rs.getTimestamp("data_venda"));

                    // ✔ FormaPagamento como objeto
                    FormaPagamento fp = new FormaPagamento(rs.getString("forma_pagamento"));

                    v.setFormaPagamento(fp);

                    lista.add(v);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}