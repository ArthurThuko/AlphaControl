package alphacontrol.views.fiado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class TelaFiado extends JFrame {

    // ==== Cores base ====
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    // ==== Paleta de cores para os botões ====
    private static final Color VERDE_MUSGO = new Color(119, 140, 85); // Pesquisar
    private static final Color COBRE_SUAVE = new Color(198, 134, 78); // Filtrar
    private static final Color VERDE_OLIVA = new Color(101, 125, 64); // Adicionar Produto
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90); // Editar
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62); // Excluir

    public TelaFiado() {
        setTitle("Fiados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_FUNDO);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Fiados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 30, 0);
        painelPrincipal.add(titulo, gbc);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelBusca.setBackground(BEGE_FUNDO);

        JTextField txtPesquisa = new RoundedTextField("Pesquise por nome...");
        txtPesquisa.setPreferredSize(new Dimension(350, 45));

        JButton btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 150, 45);
        JButton btnAdd = new RoundedButton("Adicionar Cliente", Color.BLUE, Color.WHITE, 220, 45);

        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        JPanel painelAdd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelAdd.setBackground(BEGE_FUNDO);
        painelAdd.add(btnAdd);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(BEGE_FUNDO);
        painelTopo.add(painelBusca, BorderLayout.WEST);
        painelTopo.add(painelAdd, BorderLayout.EAST);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(painelTopo, gbc);

        // --- Colunas e dados ---
        String[] colunas = { "Nome", "Rua", "Bairro", "Telefone", "Débito", "Ações" };
        Object[][] dados = {
                { "Fulano", "Rua x", "Centro", "99999990", "1899.90", "ações" },
                { "Beltrano", "Rua y", "Favela", "45156156", "89.90", "ações" },
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela); // Configuração completa da tabela

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(1, 1, 1, 1));
        painelTabela.add(scroll, BorderLayout.CENTER);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(painelTabela, gbc);

        add(painelPrincipal);
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(60);
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
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));

        PaddedCellRenderer paddedRenderer = new PaddedCellRenderer();
        for (int i = 0; i < tabela.getColumnCount() - 1; i++) { // aplica padding em todas, menos Ações
            tabela.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
        }

        // 👉 Renderer para a coluna "Ações"
        tabela.getColumn("Ações").setCellRenderer(new AcoesRenderer());

        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(350);
        colModel.getColumn(1).setPreferredWidth(150);
        colModel.getColumn(2).setPreferredWidth(150);
        colModel.getColumn(3).setPreferredWidth(200);
        colModel.getColumn(4).setPreferredWidth(120);
        colModel.getColumn(5).setPreferredWidth(250);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaFiado tela = new TelaFiado();
            tela.setVisible(true);
        });
    }

    // ===== Componentes customizados =====
    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setVerticalAlignment(SwingConstants.CENTER);
            return this;
        }
    }

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

    static class RoundedTextField extends JTextField implements FocusListener {
        private final String placeholder;
        private boolean showingPlaceholder;

        public RoundedTextField(String placeholder) {
            super(placeholder);
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            addFocusListener(this);
            setForeground(CINZA_PLACEHOLDER);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setOpaque(false);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BEGE_CLARO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.setColor(MARROM_CLARO);
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (showingPlaceholder) {
                setText("");
                setForeground(MARROM_ESCURO);
                showingPlaceholder = false;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isEmpty()) {
                setText(placeholder);
                setForeground(CINZA_PLACEHOLDER);
                showingPlaceholder = true;
            }
        }

        @Override
        public String getText() {
            return showingPlaceholder ? "" : super.getText();
        }
    }

    static class ButtonRenderer extends DefaultTableCellRenderer {
        private final Color background;
        private final Color foreground;

        public ButtonRenderer(Color background, Color foreground) {
            this.background = background;
            this.foreground = foreground;
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            JButton button = new JButton((value == null) ? "" : value.toString());
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(foreground);
            button.setBackground(background);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(true);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setOpaque(true);

            // Manter arredondamento igual ao RoundedButton
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void update(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(button.getBackground());
                    g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 15, 15);
                    g2.setColor(MARROM_CLARO);
                    g2.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 15, 15);
                    g2.dispose();
                    super.update(g, c);
                }
            });

            return button;
        }
    }

    // ==== Renderer com dois botões (Editar e Excluir) ====
    static class AcoesRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            painel.setBackground(isSelected ? table.getSelectionBackground() : BEGE_CLARO);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(226, 180, 90)); // dourado suave
            btnEditar.setForeground(MARROM_ESCURO);
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 7));
            btnEditar.setFocusPainted(false);
            btnEditar.setBorderPainted(false);
            btnEditar.setPreferredSize(new Dimension(50, 20));
            btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton btnExcluir = new JButton("Excluir");
            btnExcluir.setBackground(new Color(178, 67, 62)); // vermelho terroso
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 7));
            btnExcluir.setFocusPainted(false);
            btnExcluir.setBorderPainted(false);
            btnExcluir.setPreferredSize(new Dimension(50, 20));
            btnExcluir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton btnVisualizar = new JButton("Visualizar");
            btnExcluir.setBackground(new Color(178, 67, 62)); // vermelho terroso
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 7));
            btnExcluir.setFocusPainted(false);
            btnExcluir.setBorderPainted(false);
            btnExcluir.setPreferredSize(new Dimension(50, 20));
            btnExcluir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            painel.add(btnEditar);
            painel.add(btnExcluir);
            painel.add(btnVisualizar);

            return painel;
        }
    }

    static class HeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            ((JComponent) c).setBorder(new EmptyBorder(0, 15, 0, 15));
            return c;
        }
    }
}
