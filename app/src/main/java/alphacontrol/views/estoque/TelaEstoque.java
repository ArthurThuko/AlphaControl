package alphacontrol.views.estoque;

import alphacontrol.controllers.ModalAdicionarProdutoController;
import alphacontrol.controllers.ModalEditarProdutoController;
import alphacontrol.controllers.ProdutoController;
import alphacontrol.models.Produto;
import alphacontrol.views.components.Navbar;
import alphacontrol.dao.ProdutoDAO;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;
import java.util.List;

public class TelaEstoque extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private static final Color VERDE_MUSGO = new Color(119, 140, 85); 
    private static final Color COBRE_SUAVE = new Color(198, 134, 78); 
    private static final Color VERDE_OLIVA = new Color(101, 125, 64); 
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90); 
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62); 

    private final ProdutoController controller;
    private final JTable tabela;
    private final DefaultTableModel modelo;
    private final JTextField txtPesquisa;

    public TelaEstoque(ProdutoController controller) {
        this.controller = controller; 

        setTitle("Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        JFrame estaTela = this;
        Navbar navbar = new Navbar(estaTela, controller, "Estoque");
        setJMenuBar(navbar);
        
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

        txtPesquisa = new RoundedTextField("Pesquise por nome...");
        txtPesquisa.setPreferredSize(new Dimension(350, 45));

        JButton btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 150, 45);
        JButton btnAdd = new RoundedButton("Adicionar Produto", VERDE_OLIVA, Color.WHITE, 220, 45);

        btnAdd.addActionListener(e -> abrirModalAdicionar());
        btnPesquisar.addActionListener(e -> pesquisar());
        
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

        String[] colunas = { "ID", "Nome", "Qtd.", "Categoria", "Valor Compra (R$)", "Valor Venda (R$)", "Ações" };
        
        modelo = new DefaultTableModel(null, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { 
                    return Integer.class; 
                }
                return String.class;
            }
        };

        tabela = new JTable(modelo);
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

        atualizarTabela();
    }
    
    private void atualizarTabela() {
        modelo.setRowCount(0); 
        List<Produto> produtos = controller.listar(); 
        
        for (Produto p : produtos) {
            modelo.addRow(new Object[]{
                p.getProdutoId(),  
                p.getNome(),       
                p.getQntEstoque(), 
                p.getCategoria(),  
                p.getValorCompra(),
                p.getValorVenda(), 
                ""                 
            });
        }
    }
    
    private void pesquisar() {
        modelo.setRowCount(0); 
        List<Produto> produtos = controller.pesquisar(txtPesquisa.getText());
        
        for (Produto p : produtos) {
            modelo.addRow(new Object[]{
                p.getProdutoId(), p.getNome(), p.getQntEstoque(),
                p.getCategoria(), p.getValorCompra(), p.getValorVenda(), ""
            });
        }
    }
    
    private void abrirModalAdicionar() {
        ModalAdicionarProduto modal = new ModalAdicionarProduto(this);
        new ModalAdicionarProdutoController(controller, modal);
        modal.setVisible(true); 
        atualizarTabela(); 
    }
    
    private void abrirModalEditar(int row) {
        Produto produtoParaEditar = getProdutoFromRow(row);
        
        ModalEditarProduto modal = new ModalEditarProduto(this);
        modal.setProduto(produtoParaEditar); 
        
        new ModalEditarProdutoController(controller, modal);
        modal.setVisible(true); 
        atualizarTabela(); 
    }
    
    private void deletarProduto(int row) {
        int id = (int) modelo.getValueAt(row, 0);
        String nome = (String) modelo.getValueAt(row, 1);
        
        try {
            controller.deletar(id, nome);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar: " + e.getMessage());
        }
        
        atualizarTabela();
    }
    
    private Produto getProdutoFromRow(int row) {
        int id = (int) modelo.getValueAt(row, 0); 
        
        return new Produto(
            id,
            (String) modelo.getValueAt(row, 1), 
            (String) modelo.getValueAt(row, 3), 
            (Double) modelo.getValueAt(row, 4), 
            (Double) modelo.getValueAt(row, 5), 
            (Integer) modelo.getValueAt(row, 2) 
        );
    }
    
    private Integer incrementarEstoqueNaLinha(int row) {
        Produto produto = getProdutoFromRow(row);
        try {
            controller.incrementarEstoque(produto); 
            return produto.getQntEstoque();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao incrementar estoque: " + e.getMessage());
            return null;
        }
    }

    private Integer decrementarEstoqueNaLinha(int row) {
        Produto produto = getProdutoFromRow(row);
        try {
            boolean atualizou = controller.decrementarEstoque(produto); 
            if (atualizou) {
                return produto.getQntEstoque();
            }
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao decrementar estoque: " + e.getMessage());
            return null;
        }
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
        for (int i = 1; i < tabela.getColumnCount(); i++) { 
            if (i != 2 && i != 6) { 
                tabela.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
            }
        }
        
        TableColumn colQtd = tabela.getColumn("Qtd.");
        colQtd.setCellRenderer(new QuantityCellRenderer());
        colQtd.setCellEditor(new QuantityCellEditor(this)); 

        TableColumn colAcoes = tabela.getColumn("Ações");
        colAcoes.setCellRenderer(new ActionsCellRenderer());
        colAcoes.setCellEditor(new ActionsCellEditor(tabela, this)); 

        TableColumn colId = tabela.getColumnModel().getColumn(0);
        colId.setMinWidth(0);
        colId.setMaxWidth(0);
        colId.setPreferredWidth(0);

        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(1).setPreferredWidth(350); 
        colModel.getColumn(2).setMinWidth(190);       
        colModel.getColumn(2).setMaxWidth(220);       
        colModel.getColumn(3).setPreferredWidth(200); 
        colModel.getColumn(4).setPreferredWidth(200); 
        colModel.getColumn(5).setPreferredWidth(180); 
        colModel.getColumn(6).setMinWidth(250);       
        colModel.getColumn(6).setMaxWidth(260);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = null; 
                
                ProdutoDAO dao = new ProdutoDAO(connection);
                ProdutoController controller = new ProdutoController(dao);
                new TelaEstoque(controller).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro crítico ao iniciar: " + e.getMessage());
            }
        });
    }

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

    static class QuantityPanel extends JPanel {
        public JButton btnMinus = new CellButton("-", VERMELHO_TERROSO, Color.WHITE);
        public JButton btnPlus = new CellButton("+", VERDE_OLIVA, Color.WHITE);
        public JLabel lblQuantity = new JLabel("0", SwingConstants.CENTER);

        public QuantityPanel() {
            super(new BorderLayout(15, 0)); 
            setOpaque(true);
            
            lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 16));
            
            Dimension btnSize = new Dimension(50, 45); 
            btnMinus.setPreferredSize(btnSize);
            btnPlus.setPreferredSize(btnSize);
            
            add(btnMinus, BorderLayout.WEST);
            add(lblQuantity, BorderLayout.CENTER);
            add(btnPlus, BorderLayout.EAST);
        }
    }

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
    
    static class QuantityCellEditor extends AbstractCellEditor implements TableCellEditor {
        private QuantityPanel panel = new QuantityPanel();
        private JTable table;
        private int row;
        
        private final TelaEstoque telaEstoque; 

        public QuantityCellEditor(TelaEstoque telaEstoque) { 
            this.telaEstoque = telaEstoque;

            panel.btnMinus.addActionListener(e -> {
                Integer novaQtd = telaEstoque.decrementarEstoqueNaLinha(row);
                
                if (novaQtd != null) {
                    panel.lblQuantity.setText(novaQtd.toString());
                    fireEditingStopped(); 
                }
            });

            panel.btnPlus.addActionListener(e -> {
                Integer novaQtd = telaEstoque.incrementarEstoqueNaLinha(row);
                
                if (novaQtd != null) {
                    panel.lblQuantity.setText(novaQtd.toString());
                    fireEditingStopped();
                }
            });
        }
        
        @Override
        public Object getCellEditorValue() {
            try {
                return Integer.parseInt(panel.lblQuantity.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
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
            return true;
        }
    }

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

    static class ActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
        private ActionsPanel panel = new ActionsPanel();
        private int row;
        private final TelaEstoque telaEstoque; 

        public ActionsCellEditor(JTable table, TelaEstoque telaEstoque) {
            this.telaEstoque = telaEstoque;

            panel.btnEdit.addActionListener(e -> {
                fireEditingStopped(); 
                telaEstoque.abrirModalEditar(this.row); 
            });

            panel.btnDelete.addActionListener(e -> {
                fireEditingStopped(); 
                telaEstoque.deletarProduto(this.row); 
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
            return ""; 
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}