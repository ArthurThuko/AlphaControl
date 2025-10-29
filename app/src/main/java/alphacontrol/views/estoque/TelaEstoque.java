package alphacontrol.views.estoque;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;

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

        // === Evento para abrir o modal de adicionar produto ===
        btnAdd.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(painelPrincipal);
            ModalAdcProduto modal = new ModalAdcProduto(frame);
            modal.setVisible(true);
        });

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

        // --- Colunas e dados (ATUALIZADO) ---
        String[] colunas = { "Nome", "Qtd.", "Categoria", "Valor Compra (R$)", "Valor Venda (R$)", "Ações" };
        Object[][] dados = {
                { "Produto A", 150, "Eletrônicos", "1250.00", "1899.90", "" },
                { "Produto B", 80, "Acessórios", "45.50", "89.90", "" },
                { "Produto C", 230, "Limpeza", "12.75", "22.00", "" },
                { "Produto D", 50, "Decoração", "99.00", "179.99", "" },
                { "Produto E", 300, "Escritório", "5.25", "11.50", "" }
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas as colunas Qtd. e Ações são "editáveis" para ativar os botões
                return column == 1 || column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class; // Garante a ordenação correta para números
                }
                return String.class;
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
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            if (i != 1 && i != 5) { // Não aplicar nas colunas com botões
                tabela.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
            }
        }
        
        // --- Novas configurações de renderizador e editor ---
        TableColumn colQtd = tabela.getColumn("Qtd.");
        colQtd.setCellRenderer(new QuantityCellRenderer());
        colQtd.setCellEditor(new QuantityCellEditor());

        TableColumn colAcoes = tabela.getColumn("Ações");
        colAcoes.setCellRenderer(new ActionsCellRenderer());
        colAcoes.setCellEditor(new ActionsCellEditor(tabela));

        // --- Ajuste de tamanho das colunas ---
        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(350);
        colModel.getColumn(1).setMinWidth(180);
        colModel.getColumn(1).setMaxWidth(200);
        colModel.getColumn(2).setPreferredWidth(200);
        colModel.getColumn(3).setPreferredWidth(200);
        colModel.getColumn(4).setPreferredWidth(180);
        colModel.getColumn(5).setMinWidth(250);
        colModel.getColumn(5).setMaxWidth(260);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new TelaEstoque().setVisible(true));
    }

    // ==== Classes internas auxiliares (sem alterações) ====

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setVerticalAlignment(SwingConstants.CENTER);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
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
    
    // Antiga classe ButtonRenderer não é mais necessária para as células da tabela

    static class HeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            ((JComponent) c).setBorder(new EmptyBorder(0, 15, 0, 15));
            return c;
        }
    }
    
    // ==== NOVAS CLASSES INTERNAS PARA AS CÉLULAS DA TABELA ====

    /**
     * Botão com estilo para uso dentro das células da tabela.
     */
    static class CellButton extends JButton {
        public CellButton(String text, Color background, Color foreground) {
            super(text);
            setBackground(background);
            setForeground(foreground);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Painel para renderizar e editar a célula de Quantidade.
     */
    static class QuantityPanel extends JPanel {
        public JButton btnMinus = new CellButton("-", VERMELHO_TERROSO, Color.WHITE);
        public JButton btnPlus = new CellButton("+", VERDE_OLIVA, Color.WHITE);
        public JLabel lblQuantity = new JLabel("0", SwingConstants.CENTER);

        public QuantityPanel() {
            super(new BorderLayout(10, 0));
            setOpaque(true);
            
            lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 16));
            
            Dimension btnSize = new Dimension(45, 45);
            btnMinus.setPreferredSize(btnSize);
            btnPlus.setPreferredSize(btnSize);
            
            add(btnMinus, BorderLayout.WEST);
            add(lblQuantity, BorderLayout.CENTER);
            add(btnPlus, BorderLayout.EAST);
        }
    }

    /**
     * Renderizador para a célula de Quantidade.
     */
    static class QuantityCellRenderer extends QuantityPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            lblQuantity.setText(value.toString());
            return this;
        }
    }
    
    /**
     * Editor para a célula de Quantidade, que manipula os cliques nos botões.
     */
    static class QuantityCellEditor extends AbstractCellEditor implements TableCellEditor {
        private QuantityPanel panel = new QuantityPanel();
        private JTable table;
        private int row;

        public QuantityCellEditor() {
            panel.btnMinus.addActionListener(e -> {
                int currentValue = (int) table.getValueAt(row, 1);
                if (currentValue > 0) {
                    table.setValueAt(currentValue - 1, row, 1);
                }
                fireEditingStopped();
            });

            panel.btnPlus.addActionListener(e -> {
                int currentValue = (int) table.getValueAt(row, 1);
                table.setValueAt(currentValue + 1, row, 1);
                fireEditingStopped();
            });
        }
        
        @Override
        public Object getCellEditorValue() {
            return panel.lblQuantity.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            panel.lblQuantity.setText(value.toString());
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            // Permite que o editor seja ativado com um único clique
            return true;
        }
    }


    /**
     * Painel para renderizar e editar a célula de Ações.
     */
    static class ActionsPanel extends JPanel {
        public JButton btnEdit = new CellButton("Editar", DOURADO_SUAVE, MARROM_ESCURO);
        public JButton btnDelete = new CellButton("Excluir", VERMELHO_TERROSO, Color.WHITE);

        public ActionsPanel() {
            super(new FlowLayout(FlowLayout.CENTER, 10, 0));
            setOpaque(true);
            setAlignmentY(Component.CENTER_ALIGNMENT);
            
            Dimension btnSize = new Dimension(100, 40);
            btnEdit.setPreferredSize(btnSize);
            btnDelete.setPreferredSize(btnSize);

            add(btnEdit);
            add(btnDelete);
        }
    }

    /**
     * Renderizador para a célula de Ações.
     */
    static class ActionsCellRenderer extends ActionsPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor para a célula de Ações, que manipula os cliques nos botões.
     */
    static class ActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
        private ActionsPanel panel = new ActionsPanel();
        private JTable table;
        private int row;

        public ActionsCellEditor(JTable table) {
            this.table = table;

            panel.btnEdit.addActionListener(e -> {
                fireEditingStopped(); // Para a edição da célula antes de abrir o modal
                
                // --- Lógica de Edição ---
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(table);
                 ModalEditarProduto modal = new ModalEditarProduto(frame); 
                
                
                // Código para preencher seu modal real
                modal.campos[0].setText(table.getValueAt(this.row, 0).toString());
                modal.campos[1].setText(table.getValueAt(this.row, 1).toString());
                modal.campos[2].setText("0");
                modal.campos[3].setText(table.getValueAt(this.row, 2).toString());
                modal.campos[4].setText(table.getValueAt(this.row, 3).toString());
                modal.campos[5].setText(table.getValueAt(this.row, 4).toString());

                modal.setVisible(true);

                if (modal.salvarConfirmado) {
                    table.setValueAt(modal.campos[0].getText(), this.row, 0);
                    table.setValueAt(Integer.parseInt(modal.campos[1].getText()), this.row, 1);
                    table.setValueAt(modal.campos[3].getText(), this.row, 2);
                    table.setValueAt(modal.campos[4].getText(), this.row, 3);
                    table.setValueAt(modal.campos[5].getText(), this.row, 4);
                }
                
            });

            panel.btnDelete.addActionListener(e -> {
                fireEditingStopped(); // Para a edição para que a linha possa ser removida
                
                // --- Lógica de Exclusão ---
                int resposta = JOptionPane.showConfirmDialog(
                    table,
                    "Tem certeza que deseja excluir o produto '" + table.getValueAt(this.row, 0) + "'?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (resposta == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) table.getModel()).removeRow(this.row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return ""; // O valor não importa, pois as ações são diretas
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}