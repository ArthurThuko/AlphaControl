package alphacontrol.controllers.produto;

import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.Produto;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class ProdutoController {
    private final ProdutoDAO produtoDAO;

    public ProdutoController(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void adicionar(Produto produto) throws SQLException {
        if (produto.getNome().isEmpty() || produto.getCategoria().isEmpty()) {
            throw new SQLException("Nome e Categoria são obrigatórios.");
        }
        produtoDAO.adicionarProduto(produto);
    }

    public void atualizar(Produto produto) throws SQLException {
        if (produto.getProdutoId() <= 0) {
            throw new SQLException("ID do produto inválido para atualização.");
        }
        produtoDAO.atualizarProduto(produto);
    }

    public void deletar(int id, String nome) throws SQLException {
        int resposta = JOptionPane.showConfirmDialog(
                null, 
                "Tem certeza que deseja excluir o produto '" + nome + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            produtoDAO.deletarProduto(id);
        }
    }
    
    public List<Produto> listar() {
        try {
            return produtoDAO.listarProdutos();
        } catch (SQLException e) {
            mostrarErro("Erro ao listar produtos: " + e.getMessage());
            return List.of(); 
        }
    }
    
    public List<Produto> pesquisar(String nome) {
        try {
            return produtoDAO.pesquisarProdutos(nome);
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar produtos: " + e.getMessage());
            return List.of(); 
        }
    }
    
    public void incrementarEstoque(Produto produto) throws SQLException {
        produto.incrementarEstoque(1); 
        this.atualizar(produto);       
    }

    public boolean decrementarEstoque(Produto produto) throws SQLException {
        if (produto.getQntEstoque() <= 0) {
            return false; 
        }
        produto.decrementarEstoque(1); 
        this.atualizar(produto);
        return true;
    }
    
    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}