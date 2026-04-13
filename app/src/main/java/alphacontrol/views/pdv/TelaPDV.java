package alphacontrol.views.pdv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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

import alphacontrol.conexao.Conexao;
import alphacontrol.controllers.pdv.PdvController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.Cliente;
import alphacontrol.models.FormaPagamento;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;
import alphacontrol.models.Venda;
import alphacontrol.views.components.Navbar;

public class TelaPDV extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106); 
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64); 
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62); 
    private static final Color HEADER_PRODUTOS = new Color(143, 97, 54); 

    private PdvController pdvController;
    private ProdutoController produtoController;
    private ClienteDAO clienteDAO;
    private final List<ItemVenda> carrinho = new ArrayList<>();
    private DefaultTableModel modeloProdutos;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloCarrinho;
    private JTable tabelaCarrinho;

    private JLabel lblTotal;
    private JLabel lblClienteSelecionado;
    private JButton btnAdicionarCliente;
    private JTextField campoPesquisa;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private Cliente clienteSelecionado = null; 

    public TelaPDV(TelaPrincipalController mainController) {
        configurarIntegracao();
        setTitle("PDV - AlphaControl");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(BEGE_FUNDO);
        setLayout(new BorderLayout());
        setJMenuBar(new Navbar(this, mainController, "PDV"));

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("Ponto de Venda", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42)); 
        titulo.setForeground(MARROM_ESCURO);
        content.add(titulo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 40, 0)); 
        painelCentral.setOpaque(false);
        painelCentral.add(criarPainelCatalogo());
        painelCentral.add(criarPainelCheckout());

        content.add(painelCentral, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
        inicializarDados();
    }

    private void configurarIntegracao() {
        try {
            Connection conn = Conexao.getConexao();
            this.clienteDAO = new ClienteDAO(conn);
            this.produtoController = new ProdutoController(new ProdutoDAO(conn));
            this.pdvController = new PdvController(this.produtoController);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro de conexão: " + e.getMessage());
        }
    }

    private JPanel criarPainelCatalogo() {
        JPanel painel = new RoundedPanel(20);
        painel.setBackground(BEGE_CLARO);
        painel.setLayout(new BorderLayout(15, 15));
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblSub = new JLabel("Catálogo de Produtos");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblSub.setForeground(MARROM_ESCURO);

        JPanel pnlBusca = new JPanel(new BorderLayout(10, 0));
        pnlBusca.setOpaque(false);
        campoPesquisa = new RoundedTextField("Pesquise por nome...");
        campoPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        JButton btnBusca = new RoundedButton("Buscar", HEADER_PRODUTOS, Color.WHITE, 120, 45);
        btnBusca.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnBusca.addActionListener(e -> carregarProdutos(campoPesquisa.getText()));
        pnlBusca.add(campoPesquisa, BorderLayout.CENTER);
        pnlBusca.add(btnBusca, BorderLayout.EAST);
        
        JPanel topWrapper = new JPanel(new BorderLayout(0, 15));
        topWrapper.setOpaque(false);
        topWrapper.add(lblSub, BorderLayout.NORTH);
        topWrapper.add(pnlBusca, BorderLayout.CENTER);
        painel.add(topWrapper, BorderLayout.NORTH);

        modeloProdutos = new DefaultTableModel(new String[]{"Produto", "Estoque", "Preço", "Ação"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaProdutos = new JTable(modeloProdutos);
        configurarEstiloTabela(tabelaProdutos, HEADER_PRODUTOS);

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);
        
        // Encapsulando no painel arredondado para estilo idêntico ao da TelaEstoque
        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(1, 1, 1, 1));
        painelTabela.add(scroll, BorderLayout.CENTER);

        painel.add(painelTabela, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelCheckout() {
        JPanel painel = new RoundedPanel(20);
        painel.setBackground(BEGE_CLARO);
        painel.setLayout(new BorderLayout(15, 15));
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel sub = new JLabel("Carrinho de Compras", SwingConstants.LEFT);
        sub.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sub.setForeground(MARROM_ESCURO);
        painel.add(sub, BorderLayout.NORTH);

        modeloCarrinho = new DefaultTableModel(new String[]{"Item", "Quantidade", "Total", "Remover"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1 || c == 3; }
        };
        tabelaCarrinho = new JTable(modeloCarrinho);
        configurarEstiloTabela(tabelaCarrinho, MARROM_MEDIO); // Padronizado para MARROM_MEDIO
        
        tabelaCarrinho.getColumnModel().getColumn(1).setCellRenderer(new QtdRenderer());
        tabelaCarrinho.getColumnModel().getColumn(1).setCellEditor(new QtdEditor(new JCheckBox()));

        tabelaCarrinho.getColumnModel().getColumn(3).setCellRenderer(new ButtonRendererRed());
        tabelaCarrinho.getColumnModel().getColumn(3).setCellEditor(new ButtonEditorRed(new JCheckBox()));

        tabelaCarrinho.getColumnModel().getColumn(1).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabelaCarrinho);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);
        
        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(1, 1, 1, 1));
        painelTabela.add(scroll, BorderLayout.CENTER);

        painel.add(painelTabela, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridBagLayout());
        footer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JPanel painelSplit = new JPanel(new GridLayout(1, 2, 20, 0));
        painelSplit.setOpaque(false);

        JPanel pnlPagamento = new JPanel(new BorderLayout(0, 5));
        pnlPagamento.setOpaque(false);
        JLabel lblPag = new JLabel("Forma de Pagamento:"); 
        lblPag.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cbFormaPagamento = new JComboBox<>();
        estilizarCombo(cbFormaPagamento);
        pnlPagamento.add(lblPag, BorderLayout.NORTH);
        pnlPagamento.add(cbFormaPagamento, BorderLayout.CENTER);

        JPanel pnlCliente = new JPanel(new BorderLayout(0, 5));
        pnlCliente.setOpaque(false);
        lblClienteSelecionado = new JLabel("Cliente (Obrigatório no Fiado): Nenhum", SwingConstants.LEFT);
        lblClienteSelecionado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdicionarCliente = new RoundedButton("SELECIONAR CLIENTE", MARROM_MEDIO, Color.WHITE, 0, 45);
        btnAdicionarCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdicionarCliente.addActionListener(e -> abrirModalSelecionarCliente());
        btnAdicionarCliente.setEnabled(false); 
        pnlCliente.add(lblClienteSelecionado, BorderLayout.NORTH);
        pnlCliente.add(btnAdicionarCliente, BorderLayout.CENTER);

        painelSplit.add(pnlPagamento);
        painelSplit.add(pnlCliente);

        lblTotal = new JLabel("TOTAL: R$ 0,00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 36)); 
        lblTotal.setForeground(MARROM_ESCURO);

        JButton btnFinalizar = new RoundedButton("FINALIZAR VENDA", VERDE_OLIVA, Color.WHITE, 0, 65);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnFinalizar.addActionListener(e -> finalizarVenda());

        gbc.gridy = 0; gbc.insets = new Insets(10, 0, 20, 0); footer.add(painelSplit, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(10, 0, 15, 0); footer.add(lblTotal, gbc);
        gbc.gridy = 2; footer.add(btnFinalizar, gbc);

        painel.add(footer, BorderLayout.SOUTH);
        return painel;
    }

    private void configurarEstiloTabela(JTable tabela, Color headerColor) {
        tabela.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        tabela.setRowHeight(60); 
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setGridColor(new Color(223, 214, 198)); 
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);
        
        JTableHeader header = tabela.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBackground(headerColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));

        PaddedCellRenderer paddedRenderer = new PaddedCellRenderer();

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(paddedRenderer);
        }
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(0, 45));
        combo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        combo.setBackground(Color.WHITE);
    }

    private void inicializarDados() {
        carregarProdutos("");
        cbFormaPagamento.addItem(new FormaPagamento("Dinheiro"));
        cbFormaPagamento.addItem(new FormaPagamento("Cartão"));
        cbFormaPagamento.addItem(new FormaPagamento("PIX"));
        cbFormaPagamento.addItem(new FormaPagamento("Fiado"));
        
        cbFormaPagamento.addActionListener(e -> {
            FormaPagamento selecionada = (FormaPagamento) cbFormaPagamento.getSelectedItem();
            btnAdicionarCliente.setEnabled(selecionada != null && "Fiado".equalsIgnoreCase(selecionada.getNome()));
            if(!btnAdicionarCliente.isEnabled()) setClienteSelecionado(null); 
        });
        
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (tabelaProdutos.getSelectedRow() != -1 && tabelaProdutos.getSelectedColumn() == 3) {
                     adicionarAoCarrinho();
                }
            }
        });
        tabelaProdutos.getColumnModel().getColumn(3).setCellRenderer(new ButtonRendererGreen());
    }

    private void carregarProdutos(String filtro) {
        modeloProdutos.setRowCount(0);
        List<Produto> lista = filtro.isEmpty() ? produtoController.listar() : produtoController.pesquisar(filtro);
        for (Produto p : lista) {
            modeloProdutos.addRow(new Object[]{p.getNome(), p.getQntEstoque(), String.format("R$ %.2f", p.getValorVenda()).replace(",", "."), "ADD"});
        }
    }

    public void setClienteSelecionado(Cliente c) {
        this.clienteSelecionado = c;
        lblClienteSelecionado.setText(c != null ? "Cliente: " + c.getNome() : "Cliente (Obrigatório no Fiado): Nenhum");
    }

    private void abrirModalSelecionarCliente() {
        new TelaAdicionarCliente(this, clienteDAO).setVisible(true);
    }

    private void adicionarAoCarrinho() {
        int row = tabelaProdutos.getSelectedRow();
        Produto p = produtoController.buscarPorNome((String) modeloProdutos.getValueAt(row, 0));
        String qtdS = JOptionPane.showInputDialog(this, "Quantidade:", "1");
        if (qtdS != null && !qtdS.trim().isEmpty()) {
            try {
                int qtdDesejada = Integer.parseInt(qtdS);
                if (qtdDesejada <= 0) return;
                
                ItemVenda itemExistente = carrinho.stream().filter(i -> i.getProduto().getNome().equals(p.getNome())).findFirst().orElse(null);
                int qtdTotal = qtdDesejada + (itemExistente != null ? itemExistente.getQuantidade() : 0);
                if (qtdTotal > p.getQntEstoque()) {
                    JOptionPane.showMessageDialog(this, "Quantidade insuficiente!");
                    return; 
                }

                if (itemExistente != null) {
                    itemExistente.setQuantidade(qtdTotal);
                    itemExistente.setValorTotal(qtdTotal * p.getValorVenda());
                } else {
                    ItemVenda novoItem = new ItemVenda();
                    novoItem.setProduto(p); novoItem.setQuantidade(qtdDesejada);
                    novoItem.setValorUnitario(p.getValorVenda()); novoItem.setValorTotal(qtdDesejada * p.getValorVenda());
                    carrinho.add(novoItem);
                }
                atualizarCarrinho();
            } catch (NumberFormatException e) { }
        }
    }

    private void atualizarCarrinho() {
        modeloCarrinho.setRowCount(0);
        double total = carrinho.stream().mapToDouble(ItemVenda::getValorTotal).sum();
        for (ItemVenda item : carrinho) {
            modeloCarrinho.addRow(new Object[]{item.getProduto().getNome(), item.getQuantidade(), String.format("R$ %.2f", item.getValorTotal()).replace(",", "."), "REMOVER"});
        }
        lblTotal.setText(String.format("TOTAL: R$ %.2f", total).replace(",", "."));
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) return;
        FormaPagamento fp = (FormaPagamento) cbFormaPagamento.getSelectedItem();
        if ("Fiado".equalsIgnoreCase(fp.getNome()) && clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione o cliente para Fiado!");
            return;
        }
        Venda v = new Venda();
        v.setTotal(carrinho.stream().mapToDouble(ItemVenda::getValorTotal).sum());
        v.setFormaPagamento(fp);
        pdvController.finalizarVenda(v, carrinho, clienteSelecionado != null ? clienteSelecionado.getId() : null);
        carrinho.clear(); atualizarCarrinho(); carregarProdutos("");
        cbFormaPagamento.setSelectedIndex(0); setClienteSelecionado(null);
    }

    // --- COMPONENTES DA TABELA CUSTOMIZADOS ---

    class QtdPanel extends JPanel {
        JLabel lbl = new JLabel();
        JButton btnMinus = new JButton("-");

        public QtdPanel() {
            setLayout(new GridBagLayout());
            setOpaque(true); 
            
            btnMinus.setPreferredSize(new Dimension(32, 32)); 
            btnMinus.setMinimumSize(new Dimension(32, 32));
            btnMinus.setBackground(VERMELHO_TERROSO);
            btnMinus.setForeground(Color.WHITE);
            btnMinus.setFocusable(false);
            btnMinus.setBorderPainted(false);
            btnMinus.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMinus.setFont(new Font("Arial", Font.BOLD, 20));
            btnMinus.setMargin(new Insets(0, 0, 0, 0));

            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setForeground(MARROM_ESCURO);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 15); 
            gbc.anchor = GridBagConstraints.CENTER;
            add(btnMinus, gbc);

            gbc.gridx = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            add(lbl, gbc);
        }
    }

    class QtdRenderer extends QtdPanel implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean h, int r, int c) {
            lbl.setText(v.toString());
            if (s) {
                setBackground(t.getSelectionBackground());
                lbl.setForeground(t.getSelectionForeground());
            } else {
                setBackground(t.getBackground());
                lbl.setForeground(MARROM_ESCURO);
            }
            return this;
        }
    }

    class QtdEditor extends DefaultCellEditor {
        QtdPanel panel = new QtdPanel();

        public QtdEditor(JCheckBox checkBox) {
            super(checkBox);
            panel.btnMinus.addActionListener(e -> {
                int row = tabelaCarrinho.getSelectedRow();
                if (row != -1) {
                    ItemVenda item = carrinho.get(row);
                    if (item.getQuantidade() > 1) {
                        item.setQuantidade(item.getQuantidade() - 1);
                        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
                    } else {
                        carrinho.remove(row);
                    }
                    fireEditingStopped(); 
                    atualizarCarrinho();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            panel.lbl.setText(v.toString());
            panel.setBackground(t.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // --- COMPONENTES VISUAIS AUXILIARES ---

    static class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int radius) { this.radius = radius; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(MARROM_CLARO); 
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    static class RoundedButton extends JButton {
        private Color bg;
        public RoundedButton(String t, Color bg, Color fg, int w, int h) {
            super(t); this.bg = bg; setForeground(fg); setPreferredSize(new Dimension(w, h));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        protected void paintComponent(Graphics g) {
            if (!isEnabled()) { g.setColor(new Color(200, 200, 200)); }
            else { g.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg); }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    static class RoundedTextField extends JTextField {
        public RoundedTextField(String p) { setOpaque(false); setBorder(new EmptyBorder(5, 15, 5, 15)); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(MARROM_CLARO);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class ButtonRendererGreen extends JButton implements TableCellRenderer {
        public ButtonRendererGreen() { setOpaque(false); setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 12)); setContentAreaFilled(false); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean h, int r, int c) { 
            setText("Adicionar"); 
            setFont(new Font("Segoe UI", Font.BOLD, 12)); 
            if(s) setBackground(t.getSelectionBackground()); else setBackground(t.getBackground());
            return this; 
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(VERDE_OLIVA); g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, 10, 10);
            g2.setColor(Color.WHITE); FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()-fm.getHeight())/2 + fm.getAscent());
            g2.dispose();
        }
    }

    class ButtonRendererRed extends JButton implements TableCellRenderer {
        public ButtonRendererRed() { setOpaque(false); setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 12)); setContentAreaFilled(false); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean h, int r, int c) { 
            setText("REMOVER"); 
            if(s) setBackground(t.getSelectionBackground()); else setBackground(t.getBackground());
            return this; 
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(VERMELHO_TERROSO); g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, 10, 10);
            g2.setColor(Color.WHITE); FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()-fm.getHeight())/2 + fm.getAscent());
            g2.dispose();
        }
    }

    class ButtonEditorRed extends DefaultCellEditor {
        public ButtonEditorRed(JCheckBox cb) {
            super(cb);
            JButton b = new JButton();
            b.addActionListener(e -> { carrinho.remove(tabelaCarrinho.getSelectedRow()); stopCellEditing(); atualizarCarrinho(); });
            editorComponent = b;
        }
    }

    // --- RENDERERS DE ESTILO COMPARTILHADO ---
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

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setVerticalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 16)); 
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
}