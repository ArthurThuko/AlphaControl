package alphacontrol.views.relatorios;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.controllers.relatorio.RelatorioController;
import alphacontrol.views.components.Navbar;

public class TelaRelatorios extends JFrame {

    private TelaPrincipalController mainController;
    private RelatorioController controller;

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color AZUL_ACAO = new Color(70, 130, 180);

    public JTextField campoInicio;
    public JTextField campoFinal;
    public JRadioButton rbVendas, rbProdutos, rbFluxo, rbFiados;
    public JButton btnVisualizar, btnBaixar;
    public JTable tabela;

    public TelaRelatorios(TelaPrincipalController mainController) {
        this.mainController = mainController;
        this.controller = new RelatorioController(this);

        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);
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
        painelFiltros.setBorder(new EmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 25, 0);

        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelSuperior.setOpaque(false);

        MaskFormatter mascaraData = null;
        try {
            mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
        } catch (ParseException e) { e.printStackTrace(); }

        JLabel lblInicio = new JLabel("Data Início:");
        lblInicio.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInicio.setForeground(MARROM_ESCURO);
        
        campoInicio = new RoundedTextField(mascaraData);
        configurarEstiloCampoData(campoInicio);

        JLabel lblFinal = new JLabel("Data Final:");
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFinal.setForeground(MARROM_ESCURO);
        
        campoFinal = new RoundedTextField(mascaraData);
        configurarEstiloCampoData(campoFinal);

        painelSuperior.add(lblInicio);
        painelSuperior.add(campoInicio);
        painelSuperior.add(Box.createRigidArea(new Dimension(5, 0)));
        painelSuperior.add(lblFinal);
        painelSuperior.add(campoFinal);
        painelSuperior.add(Box.createRigidArea(new Dimension(40, 0))); 

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTipo.setForeground(MARROM_ESCURO);
        painelSuperior.add(lblTipo);

        rbVendas = new RoundedRadioButton("Vendas");
        rbProdutos = new RoundedRadioButton("Produtos");
        rbFluxo = new RoundedRadioButton("Fluxo");
        rbFiados = new RoundedRadioButton("Fiados");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbVendas); grupo.add(rbProdutos); grupo.add(rbFluxo); grupo.add(rbFiados);
        rbVendas.setSelected(true);

        painelSuperior.add(rbVendas); painelSuperior.add(rbProdutos);
        painelSuperior.add(rbFluxo); painelSuperior.add(rbFiados);

        painelFiltros.add(painelSuperior, gbc);

        gbc.gridy = 1;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        painelBotoes.setOpaque(false);

        btnVisualizar = new RoundedButton("Visualizar", AZUL_ACAO, Color.WHITE, 160, 45);
        btnBaixar = new RoundedButton("Baixar PDF", VERDE_OLIVA, Color.WHITE, 160, 45);

        painelBotoes.add(btnVisualizar);
        painelBotoes.add(btnBaixar);

        painelFiltros.add(painelBotoes, gbc);

        return painelFiltros;
    }

    private void configurarEstiloCampoData(JTextField campo) {
        campo.setPreferredSize(new Dimension(140, 45));
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        campo.setForeground(MARROM_ESCURO);
        campo.setCaretColor(MARROM_ESCURO);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (campo.getText().equals("__/__/____")) campo.setCaretPosition(0);
                });
                campo.repaint();
            }
            @Override
            public void focusLost(FocusEvent e) { campo.repaint(); }
        });
    }

    private JPanel criarPainelTabela() {
        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setLayout(new BorderLayout());

        tabela = new JTable(new DefaultTableModel());
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        painelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        return painelTabela;
    }

    private void configurarEventos() {
        btnVisualizar.addActionListener(e -> {
            String tipo = rbVendas.isSelected() ? "vendas" : rbProdutos.isSelected() ? "produtos" : rbFluxo.isSelected() ? "fluxo" : "fiados";
            controller.visualizarRelatorio(tipo, campoInicio.getText(), campoFinal.getText(), tabela);
            configurarOrdenacaoManual();
        });
        btnBaixar.addActionListener(e -> controller.baixarPDF(tabela));
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setPreferredSize(new Dimension(0, 55));
        header.setDefaultRenderer(new HeaderButtonRenderer());
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.setReorderingAllowed(false);

        tabela.setDefaultRenderer(Object.class, new PaddedCellRenderer(SwingConstants.CENTER));
    }

    private void configurarOrdenacaoManual() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabela.getModel());
        tabela.setRowSorter(sorter);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            String nomeCol = tabela.getColumnName(i);
            if (nomeCol.equalsIgnoreCase("Data")) {
                sorter.setComparator(i, (String d1, String d2) -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        return sdf.parse(d1).compareTo(sdf.parse(d2));
                    } catch (Exception e) { return 0; }
                });
            }
        }
    }

    // --- COMPONENTES CUSTOMIZADOS ---

    static class HeaderButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            String textoOriginal = value.toString();
            String setaFiltro = "  ⇅"; // Visível por padrão para todas as colunas
            Color corDeFundo = MARROM_MEDIO;

            RowSorter<? extends TableModel> sorter = table.getRowSorter();
            if (sorter != null && !sorter.getSortKeys().isEmpty()) {
                RowSorter.SortKey key = sorter.getSortKeys().get(0);
                if (key.getColumn() == table.convertColumnIndexToModel(col)) {
                    // Se esta for a coluna selecionada para ordenar, muda a seta e escurece o fundo
                    setaFiltro = (key.getSortOrder() == SortOrder.ASCENDING) ? "  ▲" : "  ▼";
                    corDeFundo = MARROM_ESCURO;
                }
            }

            JLabel label = new JLabel(textoOriginal + setaFiltro, SwingConstants.CENTER);
            label.setOpaque(true);
            label.setFont(new Font("Segoe UI", Font.BOLD, 17));
            label.setForeground(Color.WHITE);
            label.setBackground(corDeFundo);
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 1, MARROM_ESCURO),
                new EmptyBorder(0, 10, 0, 10)
            ));

            return label;
        }
    }

    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        public RoundedPanel(int radius) { this.cornerRadius = radius; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
            g2.setColor(MARROM_CLARO);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
            g2.dispose();
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
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(getForeground());
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    static class RoundedTextField extends JFormattedTextField {
        public RoundedTextField(MaskFormatter mascara) { super(mascara); setOpaque(false); setBorder(new EmptyBorder(5, 15, 5, 15)); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.setColor(isFocusOwner() ? MARROM_MEDIO : MARROM_CLARO);
            g2.setStroke(new BasicStroke(isFocusOwner() ? 2 : 1));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedRadioButton extends JRadioButton {
        public RoundedRadioButton(String text) {
            super(text); setOpaque(false); setForeground(MARROM_ESCURO);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
        }
    }

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer(int alignment) { setHorizontalAlignment(alignment); setBorder(new EmptyBorder(5, 15, 5, 15)); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            c.setFont(new Font("Segoe UI", Font.BOLD, 16));
            if (isSelected) { c.setBackground(table.getSelectionBackground()); c.setForeground(table.getSelectionForeground()); }
            else { c.setBackground(table.getBackground()); c.setForeground(table.getForeground()); }
            return c;
        }
    }
}