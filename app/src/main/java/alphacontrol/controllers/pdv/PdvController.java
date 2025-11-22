package alphacontrol.controllers.pdv;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.dao.PDVDAO;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;
import alphacontrol.models.Venda;
import alphacontrol.views.conexao.Conexao;

public class PdvController {

    private final ProdutoController produtoController;
    private final PDVDAO pdvDAO;

    public PdvController(
            ProdutoController produtoController) {
        this.produtoController = produtoController;

        try {
            this.pdvDAO = new PDVDAO(Conexao.getConexao());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    public void finalizarVenda(Venda venda, List<ItemVenda> itens) {

        if (itens == null || itens.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum item na venda!");
            return;
        }

        // Verificação final de estoque
for (ItemVenda item : itens) {
    Produto p = item.getProduto();

    if (item.getQuantidade() > p.getQntEstoque()) {
        JOptionPane.showMessageDialog(null,
                "O produto \"" + p.getNome() + "\" possui apenas "
                + p.getQntEstoque() + " em estoque.\n"
                + "A quantidade no carrinho é: " + item.getQuantidade());
        return;
    }
}


        try {
            int idVendaGerado = pdvDAO.registrarVenda(venda);

            if (idVendaGerado <= 0) {
                JOptionPane.showMessageDialog(null, "Erro ao registrar venda!");
                return;
            }

            for (ItemVenda item : itens) {

                item.setVendaId(idVendaGerado);

                Produto p = item.getProduto();

                // Verificação de estoque
                if (p.getQntEstoque() < item.getQuantidade()) {
                    JOptionPane.showMessageDialog(null,
                            "Estoque insuficiente para o produto: " + p.getNome());
                    return;
                }

                pdvDAO.registrarItemVenda(idVendaGerado, item);

                int novaQtd = p.getQntEstoque() - item.getQuantidade();
                p.setQntEstoque(novaQtd);

                produtoController.atualizar(p);
            }


            JOptionPane.showMessageDialog(null, "Venda finalizada com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao finalizar venda: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
