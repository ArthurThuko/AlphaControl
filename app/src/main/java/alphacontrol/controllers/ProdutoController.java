package alphacontrol.controllers;

import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.Produto;
import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private final ProdutoDAO produtoDAO;

    public ProdutoController(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void adicionarProduto(Produto produto) {
        try {
            produtoDAO.adicionarProduto(produto);
            System.out.println("✅ Produto adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    public void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarProdutos();
            produtos.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
    }

    public void atualizarProduto(Produto produto) {
        try {
            produtoDAO.atualizarProduto(produto);
            System.out.println("✅ Produto atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void deletarProduto(int id) {
        try {
            produtoDAO.deletarProduto(id);
            System.out.println("✅ Produto deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
        }
    }
}
