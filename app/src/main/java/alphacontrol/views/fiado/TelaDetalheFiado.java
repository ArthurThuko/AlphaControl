package alphacontrol.views.fiado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TelaDetalheFiado extends JFrame {

    // ==== Paleta ====
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_MUSGO = new Color(119, 140, 85);

    public TelaDetalheFiado(String dataFiado) {
        setTitle("Detalhes do Fiado");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_FUNDO);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        // === Título ===
        JLabel titulo = new JLabel("Fiado dia " + dataFiado);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(MARROM_ESCURO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(titulo, gbc);

        // === Tabela ===
        String[] colunas = { "Quant.", "Produtos" };
        Object[][] dados = {
                { "5", "Produto A" },
                { "2", "Produto B" }
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelTabela.add(scroll, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        painelPrincipal.add(painelTabela, gbc);

        // === Botão Fechar ===
        JButton btnFechar = new RoundedButton("Fechar", VERDE_MUSGO, Color.WHITE, 150, 45);
        btnFechar.addActionListener(e -> dispose());

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 0, 0, 0);
        painelPrincipal.add(btnFechar, gbc);

        add(painelPrincipal);
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(45);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(MARROM_CLARO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
        centralizar.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplica o centralizador a todas as colunas
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizar);
        }
    }

    // ==== Componentes visuais reutilizados ====
    static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            graphics.setColor(MARROM_CLARO);
            graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }

    static class RoundedButton extends JButton {
        private final Color backgroundColor, hoverColor;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            backgroundColor = bg;
            hoverColor = bg.brighter();
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hoverColor : backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==== Teste ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaDetalheFiado("25/10/2025").setVisible(true));
    }
}
