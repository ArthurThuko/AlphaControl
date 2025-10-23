package alphacontrol.views.fluxo_caixa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

public class TelaFluxoCaixa extends JFrame {

    // ==== Cores base ====
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);

    // ==== Verde para o painel ====
    private static final Color VERDE_CLARO = new Color(202, 219, 183);
    private static final Color VERDE_BORDA = new Color(139, 160, 118);
    private static final Color VERDE_BOTAO = new Color(101, 125, 64);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);

    private JTable tabelaEntradas;
    private JLabel lblTotal;

    public TelaFluxoCaixa() {
        setTitle("Fluxo de Caixa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_FUNDO);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        // === Título principal ===
        JLabel titulo = new JLabel("Fluxo de Caixa", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(titulo, gbc);

        // === PAINÉIS LADO A LADO ===
        // Agora teremos: ENTRADAS | SAÍDAS | (SALDO + GRÁFICOS)

        JPanel painelLateral = new JPanel(new GridBagLayout());
        painelLateral.setOpaque(false);

        // ---- Painéis individuais ----
        JPanel painelEntradas = criarPainelEntradas();
        JPanel painelSaidas = criarPainelSaidas();

        // ---- Coluna da direita: Saldo em cima e Gráficos embaixo ----
        JPanel painelDireita = new JPanel(new GridBagLayout());
        painelDireita.setOpaque(false);

        GridBagConstraints gbcDir = new GridBagConstraints();
        gbcDir.gridx = 0;
        gbcDir.fill = GridBagConstraints.BOTH;
        gbcDir.weightx = 1;
        gbcDir.insets = new Insets(0, 0, 20, 0);

        // Painel de Saldo (parte superior)
        gbcDir.gridy = 0;
        gbcDir.weighty = 0.35;
        painelDireita.add(criarPainelSaldo(), gbcDir);

        // Painel de Gráficos (parte inferior)
        gbcDir.gridy = 1;
        gbcDir.weighty = 0.65;
        painelDireita.add(criarPainelGraficos(), gbcDir);

        // ---- Montagem do layout geral ----
        GridBagConstraints gbcPainel = new GridBagConstraints();
        gbcPainel.gridy = 0;
        gbcPainel.fill = GridBagConstraints.BOTH;
        gbcPainel.weighty = 1;
        gbcPainel.insets = new Insets(0, 15, 0, 15);

        // Entradas (mais espaço)
        gbcPainel.gridx = 0;
        gbcPainel.weightx = 0.4;
        painelLateral.add(painelEntradas, gbcPainel);

        // Saídas (mais espaço)
        gbcPainel.gridx = 1;
        gbcPainel.weightx = 0.4;
        painelLateral.add(painelSaidas, gbcPainel);

        // Direita (Saldo + Gráficos)
        gbcPainel.gridx = 2;
        gbcPainel.weightx = 0.2;
        painelLateral.add(painelDireita, gbcPainel);

        // ---- Adiciona tudo ao painel principal ----
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        painelPrincipal.add(painelLateral, gbc);

        add(painelPrincipal);
    }

    private JPanel criarPainelEntradas() {
        JPanel painel = new RoundedPanel(25, VERDE_CLARO, VERDE_BORDA);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // === Título centralizado dentro do quadro ===
        JLabel lblTitulo = new JLabel("Entradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblTitulo, gbc);

        // === Dados da tabela (sem cabeçalho) ===
        Object[][] dados = {
                { "Mensalidade João", "250.00", "20/10/2025", "Editar", "Excluir" },
                { "Mensalidade Ana", "250.00", "20/10/2025", "Editar", "Excluir" },
                { "Venda de Suplemento", "79.90", "21/10/2025", "Editar", "Excluir" }
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, new String[] { "", "", "", "", "" }) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelaEntradas = new JTable(modelo);
        configurarTabela(tabelaEntradas);

        JScrollPane scroll = new JScrollPane(tabelaEntradas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(VERDE_CLARO);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        // === Total ===
        lblTotal = new JLabel("Total: R$ " + calcularTotal(modelo));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(MARROM_ESCURO);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblTotal, gbc);

        // === Botão adicionar ===
        JButton btnAdd = new RoundedButton("Adicionar Entrada", VERDE_BOTAO, Color.WHITE, 220, 45);
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaidas() {
        JPanel painel = new RoundedPanel(25, new Color(236, 204, 200), new Color(178, 67, 62));
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // === Título centralizado ===
        JLabel lblTitulo = new JLabel("Saídas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        painel.add(lblTitulo, gbc);

        // === Dados da tabela (sem cabeçalho) ===
        Object[][] dados = {
                { "Compra de Material", "120.00", "18/10/2025", "Editar", "Excluir" },
                { "Conta de Luz", "340.00", "20/10/2025", "Editar", "Excluir" },
                { "Pagamento Professor", "1500.00", "21/10/2025", "Editar", "Excluir" }
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, new String[] { "", "", "", "", "" }) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable tabelaSaidas = new JTable(modelo);
        configurarTabela(tabelaSaidas);

        // === Força o tema vermelho ===
        Color vermelhoFundo = new Color(236, 204, 200); // fundo suave
        Color vermelhoTexto = new Color(77, 30, 30); // texto escuro

        tabelaSaidas.setBackground(vermelhoFundo);
        tabelaSaidas.setForeground(vermelhoTexto);
        tabelaSaidas.setSelectionBackground(new Color(214, 160, 160));
        tabelaSaidas.setSelectionForeground(vermelhoTexto);
        tabelaSaidas.setTableHeader(null);

        JScrollPane scroll = new JScrollPane(tabelaSaidas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(vermelhoFundo);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        // === Total ===
        JLabel lblTotal = new JLabel("Total: R$ " + calcularTotal(modelo));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(MARROM_ESCURO);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblTotal, gbc);

        // === Botão adicionar ===
        JButton btnAdd = new RoundedButton("Adicionar Saída", VERMELHO_TERROSO, Color.WHITE, 220, 45);
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaldo() {
        JPanel painel = new RoundedPanel(25, new Color(210, 225, 245), new Color(85, 120, 170));
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Saldo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(40, 60, 90));
        gbc.gridy = 0;
        painel.add(lblTitulo, gbc);

        // Valor do saldo (entradas - saídas)
        double entradas = 0; // você pode calcular com base nas tabelas
        double saidas = 0; // depois conectar a base de dados
        double saldo = entradas - saidas;

        JLabel lblValor = new JLabel(String.format("R$ %.2f", saldo), SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(new Color(30, 60, 110));
        gbc.gridy = 1;
        painel.add(lblValor, gbc);

        return painel;
    }

    private JPanel criarPainelGraficos() {
        JPanel painel = new RoundedPanel(25, new Color(245, 240, 225), MARROM_CLARO);
        painel.setLayout(new GridLayout(3, 1, 10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // === Título ===
        JLabel lblTitulo = new JLabel("Gráficos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(MARROM_ESCURO);
        painel.add(lblTitulo);

        // === Gráficos (mock para exemplo) ===
        painel.add(criarGraficoPizza("Entradas", new double[] { 50, 30, 20 }));
        painel.add(criarGraficoPizza("Saídas", new double[] { 40, 25, 35 }));
        painel.add(criarGraficoPizza("Comparativo", new double[] { 70, 30 }));

        return painel;
    }

    private JPanel criarGraficoPizza(String titulo, double[] valores) {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getWidth(), getHeight()) - 40;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                Color[] cores = { new Color(119, 140, 85), new Color(178, 67, 62), new Color(226, 180, 90) };
                double total = 0;
                for (double v : valores)
                    total += v;

                double anguloInicio = 0;
                for (int i = 0; i < valores.length; i++) {
                    double angulo = 360 * (valores[i] / total);
                    g2.setColor(cores[i % cores.length]);
                    g2.fillArc(x, y, size, size, (int) anguloInicio, (int) angulo);
                    anguloInicio += angulo;
                }

                g2.setColor(MARROM_ESCURO);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(titulo, (getWidth() - fm.stringWidth(titulo)) / 2, getHeight() - 10);
            }
        };
        painel.setOpaque(false);
        return painel;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(50);
        tabela.setBackground(VERDE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(VERDE_BORDA);
        tabela.setShowGrid(false);
        tabela.setTableHeader(null); // remove o cabeçalho
        tabela.setIntercellSpacing(new Dimension(0, 8));
        tabela.setSelectionBackground(VERDE_BORDA.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        for (int i = 0; i < tabela.getColumnCount() - 2; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(new PaddedCellRenderer());
        }

        tabela.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer(DOURADO_SUAVE, MARROM_ESCURO));
        tabela.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(VERMELHO_TERROSO, Color.WHITE));

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabela.rowAtPoint(e.getPoint());
                int col = tabela.columnAtPoint(e.getPoint());
                DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

                if (col == 4) { // Excluir
                    int resp = JOptionPane.showConfirmDialog(null,
                            "Excluir entrada \"" + tabela.getValueAt(row, 0) + "\"?",
                            "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

                    if (resp == JOptionPane.YES_OPTION) {
                        modelo.removeRow(row);
                        lblTotal.setText("Total: R$ " + calcularTotal(modelo));
                    }
                }
            }
        });
    }

    private String calcularTotal(DefaultTableModel modelo) {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += Double.parseDouble(modelo.getValueAt(i, 1).toString().replace(",", "."));
        }
        return new DecimalFormat("#,##0.00").format(total);
    }

    // ==== Componentes reutilizados ====

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
    }

    static class RoundedButton extends JButton {
        private final Color bg, hover;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            this.bg = bg;
            this.hover = bg.brighter();
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g);
        }
    }

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }
    }

    static class ButtonRenderer extends DefaultTableCellRenderer {
        private final Color bg, fg;

        public ButtonRenderer(Color bg, Color fg) {
            this.bg = bg;
            this.fg = fg;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            JButton btn = new JButton(value.toString());
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(bg);
            btn.setForeground(fg);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void update(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                    super.update(g, c);
                }
            });
            return btn;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaFluxoCaixa().setVisible(true));
    }
}