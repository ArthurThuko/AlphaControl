package alphacontrol.controllers;

import alphacontrol.dao.MovimentacaoCaixaDAO;
import alphacontrol.models.MovimentacaoCaixa;
import java.util.List;
import java.util.stream.Collectors;

public class FluxoCaixaController {

    private MovimentacaoCaixaDAO movimentacaoCaixaDAO;

    public FluxoCaixaController() {
        this.movimentacaoCaixaDAO = new MovimentacaoCaixaDAO();
    }

    public void adicionarEntrada(String nome, double valor, String data) {
        movimentacaoCaixaDAO.inserir(new MovimentacaoCaixa(nome, "entrada", valor, data));
    }

    public void adicionarSaida(String nome, double valor, String data) {
        movimentacaoCaixaDAO.inserir(new MovimentacaoCaixa(nome, "saida", valor, data));
    }

    public List<MovimentacaoCaixa> listarEntradas() {
        return movimentacaoCaixaDAO.listar().stream()
                .filter(m -> "entrada".equals(m.getTipo()))
                .collect(Collectors.toList());
    }

    public List<MovimentacaoCaixa> listarSaidas() {
        return movimentacaoCaixaDAO.listar().stream()
                .filter(m -> "saida".equals(m.getTipo()))
                .collect(Collectors.toList());
    }

    public void removerMovimentacao(int id) {
        movimentacaoCaixaDAO.deletar(id);
    }

    public void atualizarMovimentacao(int id, String tipo, String nome, double valor, String data) {
        movimentacaoCaixaDAO.atualizar(new MovimentacaoCaixa(id, nome, tipo, valor, data));
    }

    public double calcularSaldo() {
        List<MovimentacaoCaixa> lista = movimentacaoCaixaDAO.listar();

        double totalEntradas = lista.stream()
                .filter(m -> "entrada".equals(m.getTipo()))
                .mapToDouble(MovimentacaoCaixa::getValor)
                .sum();

        double totalSaidas = lista.stream()
                .filter(m -> "saida".equals(m.getTipo()))
                .mapToDouble(MovimentacaoCaixa::getValor)
                .sum();

        return totalEntradas - totalSaidas;
    }
}