package alphacontrol.controllers;

import alphacontrol.models.Produto;
import alphacontrol.views.estoque.ModalEditarProduto;
import java.sql.SQLException;

public class ModalEditarProdutoController {
    private final ProdutoController controller;
    private final ModalEditarProduto view;

    public ModalEditarProdutoController(ProdutoController controller, ModalEditarProduto view) {
        this.controller = controller;
        this.view = view;
        
        view.getBtnSalvar().addActionListener(e -> salvar());
    }

    private void salvar() {
        try {
            Produto produto = view.getProdutoFromFields();
            controller.atualizar(produto);
            view.dispose(); // Fecha o modal SÓ SE tiver sucesso
        } catch (NumberFormatException e) {
            view.mostrarErro("Erro de formato: Verifique se os valores (R$) e Quantidade são números válidos.");
        } catch (SQLException | IllegalArgumentException e) {
            view.mostrarErro(e.getMessage());
        }
    }
}