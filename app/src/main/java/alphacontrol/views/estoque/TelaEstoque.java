package alphacontrol.views.estoque;

import alphacontrol.views.estoque.ModalEditarProduto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class TelaEstoque extends JFrame {

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

    public TelaEstoque() {
        setTitle("Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_FUNDO);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Estoque");
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

        JButton btnFiltrar = new RoundedButton("Filtrar", COBRE_SUAVE, Color.WHITE, 150, 45);
        JButton btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 150, 45);
        JButton btnAdd = new RoundedButton("Adicionar Produto", VERDE_OLIVA, Color.WHITE, 220, 45);

        painelBusca.add(txtPesquisa);
        painelBusca.add(btnFiltrar);
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

        String[] colunas = { "Nome", "Qtd.", "Categoria", "Valor Compra (R$)", "Valor Venda (R$)", "Ações" };
        Object[][] dados = {
                { "Produto A", "150", "Eletrônicos", "1250.00", "1899.90", "" },
                { "Produto B", "80", "Acessórios", "45.50", "89.90", "" },
                { "Produto C", "230", "Limpeza", "12.75", "22.00", "" },
                { "Produto D", "50", "Decoração", "99.00", "179.99", "" },
                { "Produto E", "300", "Escritório", "5.25", "11.50", "" }
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
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
        for (int i = 0; i < tabela.getColumnCount() - 1; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
        }

        tabela.getColumn("Ações").setCellRenderer(new AcoesCellRenderer());
        tabela.getColumn("Ações").setCellEditor(new AcoesCellEditor(new JCheckBox()));

        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(350);
        colModel.getColumn(1).setPreferredWidth(80);
        colModel.getColumn(2).setPreferredWidth(200);
        colModel.getColumn(3).setPreferredWidth(200);
        colModel.getColumn(4).setPreferredWidth(180);
        colModel.getColumn(5).setMinWidth(280);
        colModel.getColumn(5).setMaxWidth(300);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TelaEstoque tela = new TelaEstoque();
            tela.setVisible(true);
        });
    }

    // =========================================================================
    // Componentes Customizados (Inner Classes)
    // =========================================================================

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            super();
            // Define o padding: 5px em cima/baixo, 15px nas laterais
            setBorder(new EmptyBorder(5, 15, 5, 15));
            // [AJUSTE 2]: Centralizando o conteúdo das células
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
            super();
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
        private final Color backgroundColor;
        private final Color hoverColor;

        public RoundedButton(String text, Color background, Color foreground, int width, int height) {
            super(text);
            this.backgroundColor = background;
            this.hoverColor = background.brighter();
            setForeground(foreground);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setPreferredSize(new Dimension(width, height));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(backgroundColor);
                }
            });
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
            super.addFocusListener(this);
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
            if (this.showingPlaceholder) {
                super.setText("");
                setForeground(MARROM_ESCURO);
                this.showingPlaceholder = false;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (super.getText().isEmpty()) {
                super.setText(placeholder);
                setForeground(CINZA_PLACEHOLDER);
                this.showingPlaceholder = true;
            }
        }

        @Override
        public String getText() {
            return showingPlaceholder ? "" : super.getText();
        }
    }

    static class AcoesCellRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar = new RoundedButton("Editar", DOURADO_SUAVE, MARROM_ESCURO, 100, 35);
        private final JButton btnExcluir = new RoundedButton("Excluir", VERMELHO_TERROSO, Color.WHITE, 100, 35);

        public AcoesCellRenderer() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 5, 0, 5);
            add(btnEditar, gbc);
            add(btnExcluir, gbc);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground()
                    : (row % 2 == 0 ? BEGE_CLARO : new Color(250, 245, 235)));
            return this;
        }
    }

    static class AcoesCellEditor extends DefaultCellEditor {
        private final JPanel panel;
        protected int currentRow;

        public AcoesCellEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new GridBagLayout());
            JButton btnEditar = new RoundedButton("Editar", DOURADO_SUAVE, MARROM_ESCURO, 100, 35);
            JButton btnExcluir = new RoundedButton("Excluir", VERMELHO_TERROSO, Color.WHITE, 100, 35);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 5, 0, 5);
            panel.add(btnEditar, gbc);
            panel.add(btnExcluir, gbc);

            btnEditar.addActionListener(e -> {
                fireEditingStopped();

                // Obtém os dados da linha selecionada
                JTable tabela = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, panel);
                if (tabela != null) {
                    int row = currentRow;

                    // Cria e mostra o modal
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(tabela);
                    ModalEditarProduto modal = new ModalEditarProduto(frame);

                    // Preenche os campos com os valores da linha
                    modal.campos[0].setText(tabela.getValueAt(row, 0).toString()); // Nome
                    modal.campos[1].setText(tabela.getValueAt(row, 1).toString()); // Quantidade
                    modal.campos[2].setText("0"); // Alerta Estoque (ajuste se houver coluna)
                    modal.campos[3].setText(tabela.getValueAt(row, 2).toString()); // Categoria
                    modal.campos[4].setText(tabela.getValueAt(row, 3).toString()); // Valor Compra
                    modal.campos[5].setText(tabela.getValueAt(row, 4).toString()); // Valor Venda

                    modal.setVisible(true);
                }
            });

            btnExcluir.addActionListener(e -> {
                fireEditingStopped();
                JOptionPane.showMessageDialog(null, "Ação: Excluir linha " + (currentRow + 1));
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.currentRow = row;
            panel.setBackground(
                    table.getSelectionBackground() != null ? table.getSelectionBackground() : table.getBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    static class HeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            // [AJUSTE 1]: Centralizando o texto do header
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