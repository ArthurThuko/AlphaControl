package alphacontrol.controllers;

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
            view.dispose(); // Fecha o modal SÓ SE tiver sucesso
        } catch (NumberFormatException e) {
            view.mostrarErro("Erro de formato: Verifique se os valores (R$) e Quantidade são números válidos.");
        } catch (SQLException | IllegalArgumentException e) {
            // Captura erros do DAO (SQLException) ou do Modelo (IllegalArgumentException)
            view.mostrarErro(e.getMessage());
        }
    }
}