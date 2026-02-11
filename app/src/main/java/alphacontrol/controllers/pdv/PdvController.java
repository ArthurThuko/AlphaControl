package alphacontrol.controllers.pdv;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import alphacontrol.conexao.Conexao;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.dao.PDVDAO;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;
import alphacontrol.models.Venda;

public class PdvController {

    private final ProdutoController produtoController;
    private final PDVDAO pdvDAO;

    public PdvController(ProdutoController produtoController) {
        this.produtoController = produtoController;
        try {
            this.pdvDAO = new PDVDAO(Conexao.getConexao());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    /**
     * Finaliza a venda usando transação atômica.
     * @param venda Objeto com dados da venda
     * @param itens Lista de itens do carrinho
     * @param clienteId ID do cliente selecionado (Pode ser NULL se for venda à vista/anônima)
     */
    public void finalizarVenda(Venda venda, List<ItemVenda> itens, Integer clienteId) {

        // 1. Validações básicas
        if (itens == null || itens.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum item na venda!");
            return;
        }

        // 2. Validação de Estoque (Antes de tentar gravar no banco)
        for (ItemVenda item : itens) {
            Produto p = item.getProduto();
            // Verifica se o produto existe e tem estoque
            if (p != null && item.getQuantidade() > p.getQntEstoque()) {
                JOptionPane.showMessageDialog(null,
                        "Estoque insuficiente para o produto \"" + p.getNome() + "\".\n"
                        + "Estoque atual: " + p.getQntEstoque() + "\n"
                        + "Tentativa de venda: " + item.getQuantidade());
                return; // Para tudo se não tiver estoque
            }
        }

        // 3. Tenta realizar a venda completa (Venda + Itens + Fiado + Cliente)
        try {
            // O método realizarVendaCompleta já valida se é FIADO e se tem clienteId
            boolean sucesso = pdvDAO.realizarVendaCompleta(venda, itens, clienteId);

            if (sucesso) {
                // 4. Se o banco gravou tudo certo, agora baixamos o estoque
                baixarEstoque(itens);
                JOptionPane.showMessageDialog(null, "Venda finalizada com sucesso!");
            }

        } catch (IllegalArgumentException ie) {
            // Erros de validação (ex: Fiado sem cliente)
            JOptionPane.showMessageDialog(null, "Atenção: " + ie.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            // Erros de Banco de Dados
            JOptionPane.showMessageDialog(null, "Erro crítico ao finalizar venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para baixar o estoque após o sucesso da venda
    private void baixarEstoque(List<ItemVenda> itens) {
        for (ItemVenda item : itens) {
            Produto p = item.getProduto();
            if (p != null) {
                int novaQtd = p.getQntEstoque() - item.getQuantidade();
                p.setQntEstoque(novaQtd);
                try {
                    produtoController.atualizar(p);
                } catch (Exception e) {
                    System.err.println("Erro ao baixar estoque visualmente: " + e.getMessage());
                    // Nota: O ideal seria o estoque baixar dentro da transação do DAO também, 
                    // mas mantendo sua arquitetura atual, baixamos aqui via Controller.
                }
            }
        }
    }
}