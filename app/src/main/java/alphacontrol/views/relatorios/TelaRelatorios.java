package alphacontrol.views.relatorios;

import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.controllers.relatorio.RelatorioController;
import alphacontrol.views.components.Navbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class TelaRelatorios extends JFrame {

    private TelaPrincipalController mainController;
    private RelatorioController controller;

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color AZUL_ACAO = new Color(70, 130, 180);

    // ======= CAMPOS E COMPONENTES ACESSÍVEIS AO CONTROLLER =======
    public JTextField campoInicio;
    public JTextField campoFinal;
    public JRadioButton rbVendas, rbProdutos, rbFluxo, rbFiados;
    public JButton btnVisualizar, btnBaixar;
    public JTable tabela;
    // =============================================================

    public TelaRelatorios(TelaPrincipalController mainController) {
        this.mainController = mainController;
        this.controller = new RelatorioController(this);

        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setJMenuBar(new Navbar(this, this.mainController, "Relatório"));

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Relatórios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        titulo.setBorder(new EmptyBorder(0, 0, 30, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPrincipal.add(titulo, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(criarPainelFiltros(), gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(criarPainelTabela(), gbc);

        add(painelPrincipal);
        setVisible(true);

        configurarEventos();
    }

    private JPanel criarPainelFiltros() {
        RoundedPanel painelFiltros = new RoundedPanel(15);
        painelFiltros.setBackground(BEGE_CLARO);
        painelFiltros.setLayout(new GridBagLayout());
        painelFiltros.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // ================= LINHA 0 ===================
        gbc.gridy = 0;

        gbc.gridx = 0;
        JLabel lblTipo = new JLabel("Tipo de Relatório:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTipo.setForeground(MARROM_ESCURO);
        painelFiltros.add(lblTipo, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        painelRadios.setOpaque(false);

        rbVendas = new RoundedRadioButton("Vendas");
        rbProdutos = new RoundedRadioButton("Produtos + vendidos");
        rbFluxo = new RoundedRadioButton("Fluxo de Caixa");
        rbFiados = new RoundedRadioButton("Fiados");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbVendas);
        grupo.add(rbProdutos);
        grupo.add(rbFluxo);
        grupo.add(rbFiados);
        rbVendas.setSelected(true);

        painelRadios.add(rbVendas);
        painelRadios.add(rbProdutos);
        painelRadios.add(rbFluxo);
        painelRadios.add(rbFiados);

        painelFiltros.add(painelRadios, gbc);

        gbc.gridwidth = 1;

        // ================= LINHA 1 ===================
        gbc.gridy = 1;

        gbc.gridx = 0;
        JLabel lblInicio = new JLabel("Data Início:");
        lblInicio.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInicio.setForeground(MARROM_ESCURO);
        painelFiltros.add(lblInicio, gbc);

        gbc.gridx = 1;
        campoInicio = new RoundedTextField("dd/mm/aaaa");
        campoInicio.setPreferredSize(new Dimension(200, 45));
        painelFiltros.add(campoInicio, gbc);

        gbc.gridx = 2;
        JLabel lblFinal = new JLabel("Data Final:");
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFinal.setForeground(MARROM_ESCURO);
        painelFiltros.add(lblFinal, gbc);

        gbc.gridx = 3;
        campoFinal = new RoundedTextField("dd/mm/aaaa");
        campoFinal.setPreferredSize(new Dimension(200, 45));
        painelFiltros.add(campoFinal, gbc);

        // ================= LINHA 2 (BOTÕES) ===================
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBotoes.setOpaque(false);

        btnVisualizar = new RoundedButton("Visualizar", AZUL_ACAO, Color.WHITE, 150, 45);
        btnBaixar = new RoundedButton("Baixar", VERDE_OLIVA, Color.WHITE, 150, 45);

        painelBotoes.add(btnVisualizar);
        painelBotoes.add(btnBaixar);

        painelFiltros.add(painelBotoes, gbc);

        return painelFiltros;
    }

    private JPanel criarPainelTabela() {
        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setLayout(new BorderLayout());

        String[] colunas = { "", "", "", "", "" };
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        painelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        return painelTabela;
    }

    private void configurarEventos() {
        btnVisualizar.addActionListener(e -> {

            String tipo = rbVendas.isSelected() ? "vendas"
                    : rbProdutos.isSelected() ? "produtos"
                            : rbFluxo.isSelected() ? "fluxo"
                                    : "fiados";

            controller.visualizarRelatorio(
                    tipo,
                    campoInicio.getText(),
                    campoFinal.getText(),
                    tabela);
        });
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

        PaddedCellRenderer.apply(tabela, 0, SwingConstants.CENTER);
        PaddedCellRenderer.apply(tabela, 1, SwingConstants.LEFT);
        PaddedCellRenderer.apply(tabela, 2, SwingConstants.CENTER);
        PaddedCellRenderer.apply(tabela, 3, SwingConstants.RIGHT);
        PaddedCellRenderer.apply(tabela, 4, SwingConstants.CENTER);
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
            if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(backgroundColor);
            }
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

    static class RoundedRadioButton extends JRadioButton {
        public RoundedRadioButton(String text) {
            super(text);
            setOpaque(false);
            setForeground(MARROM_ESCURO);
            setFont(new Font("Segoe UI", Font.BOLD, 17));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
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

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer(int alignment) {
            setHorizontalAlignment(alignment);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                c.setBackground(table.getBackground());
                c.setForeground(table.getForeground());
            }
            return c;
        }

        public static void apply(JTable table, int col, int alignment) {
            table.getColumnModel().getColumn(col).setCellRenderer(new PaddedCellRenderer(alignment));
        }
    }
}