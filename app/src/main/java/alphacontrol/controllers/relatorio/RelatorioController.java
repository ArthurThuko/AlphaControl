package alphacontrol.controllers.relatorio;

import alphacontrol.dao.RelatorioDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.models.ProdutoMaisVendido;
import alphacontrol.models.Venda;
import alphacontrol.views.relatorios.TelaRelatorios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

public class RelatorioController {

    private TelaRelatorios view;
    private RelatorioDAO dao;

    public RelatorioController(TelaRelatorios view) {
        this.view = view;
        this.dao = new RelatorioDAO();
    }

    // ---------------------------------------------------------
    // HELPERS DE CONVERSÃO
    // ---------------------------------------------------------

    // converte java.util.Date -> java.sql.Date (para queries que usam
    // java.sql.Date)
    private java.sql.Date toSqlDate(Date d) {
        return d == null ? null : new java.sql.Date(d.getTime());
    }

    // converte java.util.Date -> java.time.LocalDateTime (para listarFiados)
    private LocalDateTime toLocalDateTime(Date d) {
        if (d == null)
            return null;
        return Instant.ofEpochMilli(d.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // ---------------------------------------------------------
    // MÉTODO PRINCIPAL DO BOTÃO "VISUALIZAR"
    // ---------------------------------------------------------

    public void visualizarRelatorio(String tipo, String inicioStr, String fimStr, JTable tabela) {

        Date inicio = validarData(inicioStr);
        Date fim = validarData(fimStr);

        if (inicio == null || fim == null) {
            JOptionPane.showMessageDialog(view, "Datas inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fim.before(inicio)) {
            JOptionPane.showMessageDialog(view, "Data final não pode ser menor que a inicial!", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (tipo) {

            case "produtos":
                carregarProdutosMaisVendidos(inicio, fim, tabela);
                break;

            case "vendas":
                carregarVendas(inicio, fim, tabela);
                break;

            case "fluxo":
                carregarFluxo(inicio, fim, tabela);
                break;

            case "fiados":
                carregarFiados(inicio, fim, tabela);
                break;

            default:
                JOptionPane.showMessageDialog(view, "Selecione um tipo de relatório.", "Atenção",
                        JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------------------------------------------------------
    // CARREGADORES DE CADA TIPO DE RELATÓRIO
    // ---------------------------------------------------------

    private void carregarProdutosMaisVendidos(Date inicio, Date fim, JTable tabela) {
        List<ProdutoMaisVendido> lista = dao.listarProdutosMaisVendidos(toSqlDate(inicio), toSqlDate(fim));

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[] { "Produto", "Quantidade" }, 0);

        for (ProdutoMaisVendido p : lista) {
            modelo.addRow(new Object[] {
                    p.getNome(),
                    p.getQuantidade()
            });
        }

        tabela.setModel(modelo);
    }

    private void carregarVendas(Date inicio, Date fim, JTable tabela) {
        List<Venda> lista = dao.listarVendas(toSqlDate(inicio), toSqlDate(fim));

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[] { "ID", "Total", "Data", "Forma de Pagamento" }, 0);

        for (Venda v : lista) {
            modelo.addRow(new Object[] {
                    v.getVendaId(),
                    v.getTotal(),
                    v.getDataVenda(),
                    v.getFormaPagamento().getNome()
            });
        }

        tabela.setModel(modelo);
    }

        private void carregarFluxo(Date inicio, Date fim, JTable tabela) {
            List<MovimentacaoCaixa> lista = dao.listarMovimentacoes(toSqlDate(inicio), toSqlDate(fim));

            DefaultTableModel modelo = new DefaultTableModel(
                    new Object[] { "ID", "Nome", "Tipo", "Valor", "Data" }, 0);

            for (MovimentacaoCaixa m : lista) {
                modelo.addRow(new Object[] {
                        m.getId(),
                        m.getNome(),
                        m.getTipo(),
                        m.getValor(),
                        m.getData()
                });
            }

            tabela.setModel(modelo);
        }

    private void carregarFiados(Date inicio, Date fim, JTable tabela) {
        // converte para LocalDateTime antes de chamar o DAO
        LocalDateTime inicioLdt = toLocalDateTime(inicio).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimLdt = toLocalDateTime(fim).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        List<Fiado> lista = dao.listarFiados(inicioLdt, fimLdt);

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[] { "ID", "Cliente", "Valor", "Data", "Status" }, 0);

        for (Fiado f : lista) {
            modelo.addRow(new Object[] {
                    f.getId(),
                    f.getClienteId(),
                    f.getValor(),
                    f.getData(),
                    f.getStatus()
            });
        }

        tabela.setModel(modelo);
    }

    // ---------------------------------------------------------
    // VALIDAÇÃO DAS DATAS (dd/MM/yyyy)
    // ---------------------------------------------------------

    private Date validarData(String dataStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(dataStr);
        } catch (ParseException e) {
            return null;
        }
    }
}