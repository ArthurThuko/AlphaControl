package alphacontrol.views.pdv;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import alphacontrol.controllers.pdv.PdvController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.FormaPagamento;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;
import alphacontrol.models.Venda;
import alphacontrol.views.components.Navbar;
import alphacontrol.views.conexao.Conexao;

public class TelaPDV extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_BOTAO = new Color(72, 166, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private TelaPrincipalController mainController;

    // Integração
    private PdvController pdvController;
    private ProdutoController produtoController;

    private final List<ItemVenda> carrinho = new ArrayList<>();
    private DefaultTableModel modelo;
    private JTable tabelaProdutos;

    private DefaultTableModel modeloCarrinho;
    private JTable tabelaCarrinho;

    private JLabel lblTotal;
    private JButton btnVender;
    private JTextField campoPesquisa;
    private JButton btnPesquisar;
    private JComboBox<FormaPagamento> cbFormaPagamento;

    public TelaPDV(TelaPrincipalController mainController) {
        this.mainController = mainController;

        try {
            Connection conn = Conexao.getConexao();

            ProdutoDAO produtoDAO = new ProdutoDAO(conn);
            this.produtoController = new ProdutoController(produtoDAO);

            this.pdvController = new PdvController(this.produtoController);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage());
        }

        setTitle("PDV - Ponto de Venda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BEGE_FUNDO);
        setLayout(new BorderLayout());

        setJMenuBar(new Navbar(this, this.mainController, "PDV"));

        // ---------------- TOPO ---------------------
        JPanel painelTopo = new JPanel(new BorderLayout(15, 15));
        painelTopo.setBackground(BEGE_FUNDO);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel lblTitulo = new JLabel("Ponto de Venda (PDV)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        painelTopo.add(lblTitulo, BorderLayout.CENTER);

        JPanel pnlPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlPesquisa.setBackground(BEGE_FUNDO);

        campoPesquisa = new RoundedTextField("Pesquisar produto...");
        campoPesquisa.setPreferredSize(new Dimension(300, 40));

        btnPesquisar = new RoundedButton("Pesquisar", new Color(60, 150, 80), Color.WHITE, 140, 40);

        pnlPesquisa.add(campoPesquisa);
        pnlPesquisa.add(btnPesquisar);

        painelTopo.add(pnlPesquisa, BorderLayout.WEST);

        add(painelTopo, BorderLayout.NORTH);

        // ---------------- CENTRO ---------------------
        JPanel painelCentro = new JPanel(new GridBagLayout());
        painelCentro.setBackground(BEGE_FUNDO);
        painelCentro.setBorder(new EmptyBorder(0, 30, 30, 30));

        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.fill = GridBagConstraints.BOTH;
        gbcCentro.weighty = 1.0;

        // tabela produtos
        String[] colunas = { "Nome", "Qtd.", "Categoria", "Valor Venda", "Ações" };
        modelo = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modelo);
        configurarTabela(tabelaProdutos);
        centralizarColunas(tabelaProdutos);

        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.getViewport().setBackground(BEGE_CLARO);

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        gbcCentro.gridx = 0;
        gbcCentro.weightx = 0.7;
        gbcCentro.insets = new Insets(0, 0, 0, 15);
        painelCentro.add(painelTabela, gbcCentro);

        // ----- painel carrinho ------
        RoundedPanel painelCarrinho = new RoundedPanel(15);
        painelCarrinho.setLayout(new GridBagLayout());
        painelCarrinho.setBackground(BEGE_CLARO);

        GridBagConstraints gbcCarrinho = new GridBagConstraints();
        gbcCarrinho.fill = GridBagConstraints.HORIZONTAL;
        gbcCarrinho.gridx = 0;
        gbcCarrinho.weightx = 1.0;

        JLabel lblCarrinho = new JLabel("Carrinho");
        lblCarrinho.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblCarrinho.setForeground(MARROM_ESCURO);
        painelCarrinho.add(lblCarrinho, gbcCarrinho);

        gbcCarrinho.gridy = 1;
        gbcCarrinho.fill = GridBagConstraints.BOTH;
        gbcCarrinho.weighty = 1.0;

        // ADDED: coluna Remover
        String[] colunasCarrinho = { "Produto", "Qtd", "Subtotal", "Remover" };

        // cria modelo com apenas a coluna "Remover" editável
        modeloCarrinho = new DefaultTableModel(colunasCarrinho, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // somente coluna Remover clicável
            }
        };

        tabelaCarrinho = new JTable(modeloCarrinho);

        // configurar renderer/editor para a coluna Remover (índice 3)
        // deve ser feito após criar a tabela
        tabelaCarrinho.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabelaCarrinho.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(modeloCarrinho, carrinho));

        configurarTabela(tabelaCarrinho);
        tabelaCarrinho.setRowHeight(35);

        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);
        painelCarrinho.add(scrollCarrinho, gbcCarrinho);

        gbcCarrinho.gridy = 2;
        gbcCarrinho.fill = GridBagConstraints.HORIZONTAL;
        gbcCarrinho.weighty = 0.0;

        JPanel pnlPagamento = new JPanel(new GridBagLayout());
        pnlPagamento.setOpaque(false);

        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.fill = GridBagConstraints.HORIZONTAL;
        gbcPay.weightx = 1.0;
        gbcPay.gridx = 0;

        cbFormaPagamento = new JComboBox<>();
        cbFormaPagamento.setPreferredSize(new Dimension(200, 35));

        pnlPagamento.add(cbFormaPagamento, gbcPay);

        gbcPay.gridy = 1;
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(MARROM_ESCURO);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlPagamento.add(lblTotal, gbcPay);

        gbcPay.gridy = 2;
        btnVender = new RoundedButton("Finalizar Venda", VERDE_BOTAO, Color.WHITE, 220, 50);
        btnVender.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlPagamento.add(btnVender, gbcPay);

        painelCarrinho.add(pnlPagamento, gbcCarrinho);

        gbcCentro.gridx = 1;
        gbcCentro.weightx = 0.3;
        painelCentro.add(painelCarrinho, gbcCentro);

        add(painelCentro, BorderLayout.CENTER);

        inicializarIntegracao();
    }

    // ===========================================================
    // LÓGICA
    // ===========================================================
    private void inicializarIntegracao() {

        cbFormaPagamento.addItem(new FormaPagamento("PIX"));
        cbFormaPagamento.addItem(new FormaPagamento("Dinheiro"));
        cbFormaPagamento.addItem(new FormaPagamento("Cartão"));

        btnVender.addActionListener(e -> finalizarVenda());

        btnPesquisar.addActionListener(e -> carregarProdutos(campoPesquisa.getText().trim()));

        campoPesquisa.addActionListener(e -> carregarProdutos(campoPesquisa.getText().trim()));

        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabelaProdutos.rowAtPoint(evt.getPoint());
                int col = tabelaProdutos.columnAtPoint(evt.getPoint());
                if (col == 4) {
                    adicionarProdutoAoCarrinho(row);
                }
            }
        });

        carregarProdutos();
    }

    // --- Carregar produtos com / sem filtro ---
    private void carregarProdutos() {
        carregarProdutos("");
    }

    private void carregarProdutos(String filtro) {
        modelo.setRowCount(0);

        List<Produto> produtos;
        if (filtro == null || filtro.isBlank()) {
            produtos = produtoController.listar();
        } else {
            produtos = produtoController.pesquisar(filtro);
        }

        for (Produto p : produtos) {
            modelo.addRow(new Object[] {
                    p.getNome(),
                    p.getQntEstoque(),
                    p.getCategoria(),
                    p.getValorVenda(),
                    "Adicionar"
            });
        }
    }

    private void centralizarColunas(JTable tabela) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void adicionarProdutoAoCarrinho(int row) {
        String nome = (String) modelo.getValueAt(row, 0);
        Produto produto = produtoController.buscarPorNome(nome);

        if (produto == null)
            return;

        String qtdStr = JOptionPane.showInputDialog(this, "Digite a quantidade:");
        if (qtdStr == null || qtdStr.isBlank())
            return;

        int quantidade = Integer.parseInt(qtdStr);

        if (quantidade > produto.getQntEstoque()) {
            JOptionPane.showMessageDialog(this, "Estoque insuficiente!");
            return;
        }

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setProdutoId(produto.getProdutoId());
        item.setQuantidade(quantidade);
        item.setValorUnitario(produto.getValorVenda());

        carrinho.add(item);

        atualizarCarrinho();
    }

    private void atualizarCarrinho() {
        modeloCarrinho.setRowCount(0);

        double total = 0;

        for (ItemVenda item : carrinho) {
            double subtotal = item.getQuantidade() * item.getValorUnitario();
            total += subtotal;

            // ADDED: coluna Remover com texto (o renderer/editor mostrará o botão)
            modeloCarrinho.addRow(new Object[] {
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    subtotal,
                    "Remover"
            });
        }

        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio!");
            return;
        }

        // ADDED: verificação final de estoque antes de registrar venda
        for (ItemVenda item : carrinho) {
            Produto p = item.getProduto();
            if (item.getQuantidade() > p.getQntEstoque()) {
                JOptionPane.showMessageDialog(this,
                        "O produto \"" + p.getNome() + "\" possui apenas "
                                + p.getQntEstoque() + " unidades no estoque.\n" +
                                "A quantidade no carrinho é: " + item.getQuantidade());
                return;
            }
        }

        Venda venda = new Venda();
        venda.setTotal(carrinho.stream().mapToDouble(i -> i.getQuantidade() * i.getValorUnitario()).sum());

        FormaPagamento forma = (FormaPagamento) cbFormaPagamento.getSelectedItem();
        venda.setFormaPagamento(forma);
        venda.setDataVenda(new Date());

        pdvController.finalizarVenda(venda, carrinho);

        carrinho.clear();
        atualizarCarrinho();
        carregarProdutos();
    }

    // ========================== ESTILOS ===============================
    private void configurarTabela(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(40);
        table.setBackground(BEGE_CLARO);
        table.setForeground(MARROM_ESCURO);
        table.setSelectionBackground(MARROM_CLARO.brighter());
        table.setSelectionForeground(MARROM_ESCURO);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
    }

    // ---- botoes e campos arredondados (seus originais) -----
    static class RoundedButton extends JButton {
        private final Color bgColor;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            this.bgColor = bg;
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed())
                g2.setColor(bgColor.darker());
            else if (getModel().isRollover())
                g2.setColor(bgColor.brighter());
            else
                g2.setColor(bgColor);

            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            g2.dispose();

            super.paintComponent(g);
        }
    }

    static class RoundedTextField extends JTextField implements FocusListener {
        private final String placeholder;
        private boolean showingPlaceholder = true;

        public RoundedTextField(String placeholder) {
            super(placeholder);
            this.placeholder = placeholder;
            setForeground(CINZA_PLACEHOLDER);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setOpaque(false);
            setBorder(new EmptyBorder(5, 15, 5, 15));
            addFocusListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(MARROM_CLARO);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
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

    static class RoundedPanel extends JPanel {
        private final int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(MARROM_CLARO);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==== RENDERIZA O BOTÃO NA TABELA ====
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setText("Remover");
            setForeground(Color.WHITE);
            setBackground(new Color(200, 70, 70));
            setOpaque(true);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ==== EDITOR DO BOTÃO (AÇÃO DE REMOVER) ====
    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private boolean clicked;
        private int row;
        private List<ItemVenda> carrinhoRef;

        public ButtonEditor(DefaultTableModel modelo, List<ItemVenda> carrinho) {
            super(new JTextField());
            this.carrinhoRef = carrinho;

            button = new JButton("Remover");
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(200, 70, 70));
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));

            // Ao clicar, paramos a edição (getCellEditorValue será chamado)
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object obj,
                boolean selected, int row, int col) {
            this.row = row;
            this.clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                // remove item da lista real
                if (row >= 0 && row < carrinhoRef.size()) {
                    carrinhoRef.remove(row);
                    // atualiza a tabela e total (reconstrói o modelo a partir da lista)
                    atualizarCarrinho();
                }
            }
            clicked = false;
            return "Remover";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}