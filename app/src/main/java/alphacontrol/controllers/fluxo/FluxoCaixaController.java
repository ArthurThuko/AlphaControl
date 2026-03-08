package alphacontrol.controllers.fluxo;

import alphacontrol.dao.MovimentacaoCaixaDAO;
import alphacontrol.models.MovimentacaoCaixa;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluxoCaixaController {

    private MovimentacaoCaixaDAO movimentacaoCaixaDAO;

    public FluxoCaixaController(Connection connection) {
        this.movimentacaoCaixaDAO = new MovimentacaoCaixaDAO(connection);
    }

    public void adicionarEntrada(String nome, double valor, String data) {
        movimentacaoCaixaDAO.inserir(new MovimentacaoCaixa(nome, "entrada", valor, data));
    }

    public void adicionarSaida(String nome, double valor, String data) {
        movimentacaoCaixaDAO.inserir(new MovimentacaoCaixa(nome, "saida", valor, data));
    }

    public List<MovimentacaoCaixa> listarEntradas() {

        List<MovimentacaoCaixa> lista = new ArrayList<>();

        // entradas manuais
        lista.addAll(
                movimentacaoCaixaDAO.listar().stream()
                        .filter(m -> "entrada".equalsIgnoreCase(m.getTipo()))
                        .collect(Collectors.toList()));

        // entradas vindas das vendas
        lista.addAll(movimentacaoCaixaDAO.listarEntradasPorVenda());

        // ordenar por data
        lista.sort((a, b) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(b.getData()).compareTo(sdf.parse(a.getData()));
            } catch (Exception e) {
                return 0;
            }
        });

        return lista;
    }

    public List<MovimentacaoCaixa> listarSaidas() {

        List<MovimentacaoCaixa> lista = new ArrayList<>();

        // saídas manuais
        lista.addAll(
                movimentacaoCaixaDAO.listar().stream()
                        .filter(m -> "saida".equalsIgnoreCase(m.getTipo()))
                        .collect(Collectors.toList()));

        // saídas de fiado
        lista.addAll(movimentacaoCaixaDAO.listarSaidasFiadoClientes());

        // ordenar por data
        lista.sort((a, b) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(b.getData()).compareTo(sdf.parse(a.getData()));
            } catch (Exception e) {
                return 0;
            }
        });

        return lista;
    }

    public void removerMovimentacao(int id) {
        movimentacaoCaixaDAO.deletar(id);
    }

    public void atualizarMovimentacao(int id, String tipo, String nome, double valor, String data) {
        movimentacaoCaixaDAO.atualizar(new MovimentacaoCaixa(id, nome, tipo, valor, data));
    }

    public double[] calcularTotais() {
        List<MovimentacaoCaixa> lista = new ArrayList<>();

        lista.addAll(listarEntradas());
        lista.addAll(listarSaidas());

        double totalEntradas = lista.stream()
                .filter(m -> "entrada".equalsIgnoreCase(m.getTipo().trim()))
                .mapToDouble(MovimentacaoCaixa::getValor)
                .sum();

        double totalSaidas = lista.stream()
                .filter(m -> "saida".equalsIgnoreCase(m.getTipo().trim()))
                .mapToDouble(MovimentacaoCaixa::getValor)
                .sum();

        return new double[] { totalEntradas, totalSaidas };
    }
}