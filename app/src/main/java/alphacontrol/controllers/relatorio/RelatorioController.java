package alphacontrol.controllers.relatorio;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import alphacontrol.dao.RelatorioDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.models.ProdutoMaisVendido;
import alphacontrol.models.Venda;
import alphacontrol.views.relatorios.TelaRelatorios;

public class RelatorioController {

    private TelaRelatorios view;
    private RelatorioDAO dao;
    private NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public RelatorioController(TelaRelatorios view) {
        this.view = view;
        this.dao = new RelatorioDAO();
    }

    private java.sql.Date toSqlDate(Date d) { return d == null ? null : new java.sql.Date(d.getTime()); }

    private LocalDateTime toLocalDateTime(Date d) {
        if (d == null) return null;
        return Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void visualizarRelatorio(String tipo, String inicioStr, String fimStr, JTable tabela) {
        Date inicio = validarData(inicioStr);
        Date fim = validarData(fimStr);

        if (inicio == null || fim == null) {
            JOptionPane.showMessageDialog(view, "Datas inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (fim.before(inicio)) {
            JOptionPane.showMessageDialog(view, "Data final menor que a inicial!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (tipo) {
            case "produtos": carregarProdutosMaisVendidos(inicio, fim, tabela); break;
            case "vendas": carregarVendas(inicio, fim, tabela); break;
            case "fluxo": carregarFluxo(inicio, fim, tabela); break;
            case "fiados": carregarFiados(inicio, fim, tabela); break;
        }
    }

    private void carregarProdutosMaisVendidos(Date in, Date fi, JTable t) {
        List<ProdutoMaisVendido> lista = dao.listarProdutosMaisVendidos(toSqlDate(in), toSqlDate(fi));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Produto", "Quantidade"}, 0);
        for (ProdutoMaisVendido p : lista) modelo.addRow(new Object[]{p.getNome(), p.getQuantidade()});
        t.setModel(modelo);
    }

    private void carregarVendas(Date in, Date fi, JTable t) {
        List<Venda> lista = dao.listarVendas(toSqlDate(in), toSqlDate(fi));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Cliente", "Total", "Data", "Pagamento"}, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Venda v : lista) {
            modelo.addRow(new Object[]{
                v.getNomeCliente() != null ? v.getNomeCliente() : "Consumidor Final",
                moeda.format(v.getTotal()),
                sdf.format(v.getDataVenda()),
                v.getFormaPagamento().getNome()
            });
        }
        t.setModel(modelo);
    }

    private void carregarFluxo(Date in, Date fi, JTable t) {
        List<MovimentacaoCaixa> lista = dao.listarMovimentacoes(toSqlDate(in), toSqlDate(fi));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Data", "Descrição", "Tipo", "Valor"}, 0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (MovimentacaoCaixa m : lista) {
            String dataFormatada = m.getData();
            try { dataFormatada = LocalDate.parse(m.getData()).format(dtf); } catch (Exception e) {}
            modelo.addRow(new Object[]{ dataFormatada, m.getNome(), m.getTipo(), moeda.format(m.getValor()) });
        }
        t.setModel(modelo);
    }

    private void carregarFiados(Date in, Date fi, JTable t) {
        LocalDateTime ldtIn = toLocalDateTime(in).withHour(0).withMinute(0);
        LocalDateTime ldtFi = toLocalDateTime(fi).withHour(23).withMinute(59);
        List<Fiado> lista = dao.listarFiados(ldtIn, ldtFi);
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Cliente", "Valor", "Data", "Status"}, 0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Fiado f : lista) {
            modelo.addRow(new Object[]{
                f.getNomeCliente() != null ? f.getNomeCliente() : "Desconhecido",
                moeda.format(f.getValor()),
                f.getData().format(dtf),
                f.getStatus()
            });
        }
        t.setModel(modelo);
    }

    private Date validarData(String dataStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(dataStr);
        } catch (ParseException e) { return null; }
    }

    public void gerarPdf(JTable tabela, String caminho) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                stream.beginText();
                stream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                stream.newLineAtOffset(50, 750);
                stream.showText("Relatório Gerencial");
                stream.endText();

                float y = 700;
                stream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                for (int i = 0; i < tabela.getColumnCount(); i++) {
                    stream.beginText(); stream.newLineAtOffset(50 + (i * 130), y);
                    stream.showText(tabela.getColumnName(i)); stream.endText();
                }

                y -= 20;
                stream.setFont(PDType1Font.HELVETICA, 10);
                for (int row = 0; row < tabela.getRowCount(); row++) {
                    for (int col = 0; col < tabela.getColumnCount(); col++) {
                        Object val = tabela.getValueAt(row, col);
                        stream.beginText(); stream.newLineAtOffset(50 + (col * 130), y);
                        stream.showText(val != null ? val.toString() : ""); stream.endText();
                    }
                    y -= 15;
                    if (y < 50) break; // Simplificação para 1 página
                }
            }
            document.save(caminho);
            JOptionPane.showMessageDialog(null, "PDF Gerado!");
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage()); }
    }

    public void baixarPDF(JTable tabela) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("relatorio_alfa.pdf"));
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            gerarPdf(tabela, chooser.getSelectedFile().getAbsolutePath());
        }
    }
}