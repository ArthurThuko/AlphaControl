package alphacontrol.views.fluxo_caixa;

import alphacontrol.dao.PDVDAO;
import alphacontrol.models.Venda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TelaDetalhesVendas extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);

    private JTable tabela;
    private JLabel lblTotal;
    private String data;

    public TelaDetalhesVendas(String data) {

        this.data = data;

        setTitle("Detalhes das vendas - " + data);
        setSize(750, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BEGE_FUNDO);

        criarComponentes();
        carregarDados();
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("Vendas do dia " + data, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(MARROM_ESCURO);
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // ===== TABELA =====
        DefaultTableModel modelo = new DefaultTableModel(
                new String[] { "Pagamento", "Valor (R$)", "Hora" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // ===== RODAPÉ =====
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(VERDE_OLIVA);

        JButton btnFechar = new RoundedButton("Fechar", MARROM_CLARO, Color.WHITE, 120, 35);
        btnFechar.addActionListener(e -> dispose());

        rodape.add(lblTotal, BorderLayout.WEST);
        rodape.add(btnFechar, BorderLayout.EAST);

        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void configurarTabela(JTable tabela) {

        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(35);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowVerticalLines(false);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(VERDE_OLIVA);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
    }

    private void carregarDados() {

        PDVDAO dao = new PDVDAO();
        List<Venda> vendas = dao.listarPorData(data);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);

        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

        double total = 0;

        for (Venda v : vendas) {

            // Ignora vendas fiado
            if ("fiado".equalsIgnoreCase(v.getFormaPagamento().getNome())) {
                continue;
            }

            modelo.addRow(new Object[] {
                    v.getFormaPagamento().getNome(),
                    df.format(v.getTotal()),
                    horaFormat.format(v.getDataVenda())
            });

            total += v.getTotal();
        }

        lblTotal.setText("Total: R$ " + df.format(total));
    }

    // ===== BOTÃO ARREDONDADO =====

    static class RoundedButton extends JButton {

        private final Color bg;
        private final Color hover;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            this.bg = bg;
            this.hover = bg.brighter();
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}