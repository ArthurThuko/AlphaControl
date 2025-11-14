package alphacontrol.controllers.modais;

import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.models.Produto;
import alphacontrol.views.estoque.ModalAdicionarProduto;
import java.sql.SQLException;

public class ModalAdicionarProdutoController {
    private final ProdutoController controller;
    private final ModalAdicionarProduto view;

    public ModalAdicionarProdutoController(ProdutoController controller, ModalAdicionarProduto view) {
        this.controller = controller;
        this.view = view;

        view.getBtnSalvar().addActionListener(e -> salvar());
    }

    private void salvar() {
        try {
            Produto produto = view.getProdutoFromFields();
            controller.adicionar(produto);
            view.dispose();
        } catch (NumberFormatException e) {
            view.mostrarErro("Erro de formato: Verifique se os valores (R$) e Quantidade são números válidos.");
        } catch (SQLException | IllegalArgumentException e) {
            view.mostrarErro(e.getMessage());
        }
    }
}