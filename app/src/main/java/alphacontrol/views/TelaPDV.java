package alphacontrol.views;

import alphacontrol.controllers.ProdutoController;
import alphacontrol.views.components.Navbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TelaPDV extends JFrame {

    // ==== Cores base ====
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_BOTAO = new Color(72, 166, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    public TelaPDV(ProdutoController controller) {
        setTitle("PDV - Ponto de Venda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BEGE_FUNDO);
        setLayout(new BorderLayout());

        // Adiciona a Navbar
        setJMenuBar(new Navbar(this, controller, "PDV"));

        // ==== TOPO ====
        JPanel painelTopo = new JPanel(new BorderLayout(15, 15));
        painelTopo.setBackground(BEGE_FUNDO);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Título centralizado
        JLabel lblTitulo = new JLabel("Ponto de Venda (PDV)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        painelTopo.add(lblTitulo, BorderLayout.CENTER);

        // Alerta à direita
        JLabel lblAlerta = new JLabel("⚠ Estoque baixo: Produto X");
        lblAlerta.setOpaque(true);
        lblAlerta.setBackground(new Color(255, 240, 240));
        lblAlerta.setForeground(new Color(150, 40, 40));
        lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlerta.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        painelTopo.add(lblAlerta, BorderLayout.EAST);
        // lblAlerta.setVisible(false); // Descomente para esconder por padrão

        // Linha de busca à esquerda
        JPanel pnlPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlPesquisa.setBackground(BEGE_FUNDO);
        
        JTextField campoPesquisa = new RoundedTextField("Pesquisar produto...");
        campoPesquisa.setPreferredSize(new Dimension(300, 40));

        JTextField campoCategoria = new RoundedTextField("Filtrar categoria...");
        campoCategoria.setPreferredSize(new Dimension(200, 40));

        RoundedButton btnPesquisar = new RoundedButton("Pesquisar", new Color(60, 150, 80), Color.WHITE, 140, 40);

        pnlPesquisa.add(campoPesquisa);
        pnlPesquisa.add(campoCategoria);
        pnlPesquisa.add(btnPesquisar);
        painelTopo.add(pnlPesquisa, BorderLayout.WEST);

        add(painelTopo, BorderLayout.NORTH);

        // ==== CENTRO ====
        JPanel painelCentro = new JPanel(new GridBagLayout());
        painelCentro.setBackground(BEGE_FUNDO);
        painelCentro.setBorder(new EmptyBorder(0, 30, 30, 30));

        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.fill = GridBagConstraints.BOTH;
        gbcCentro.weighty = 1.0;
        gbcCentro.insets = new Insets(0, 0, 0, 0);

        // === TABELA DE PRODUTOS ===
        String[] colunas = { "Nome", "Qtd.", "Categoria", "Valor Venda", "Ações" };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        JTable tabelaProdutos = new JTable(modelo);
        configurarTabela(tabelaProdutos);

        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.getViewport().setBackground(BEGE_CLARO);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        gbcCentro.gridx = 0;
        gbcCentro.weightx = 0.7;
        gbcCentro.insets = new Insets(0, 0, 0, 15); // Gap à direita
        painelCentro.add(painelTabela, gbcCentro);

        // === CARRINHO ===
        RoundedPanel painelCarrinho = new RoundedPanel(15);
        painelCarrinho.setLayout(new GridBagLayout());
        painelCarrinho.setBackground(BEGE_CLARO);

        GridBagConstraints gbcCarrinho = new GridBagConstraints();
        gbcCarrinho.fill = GridBagConstraints.HORIZONTAL;
        gbcCarrinho.gridx = 0;
        gbcCarrinho.weightx = 1.0;
        gbcCarrinho.insets = new Insets(15, 20, 15, 20);

        JLabel lblCarrinho = new JLabel("Carrinho");
        lblCarrinho.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblCarrinho.setForeground(MARROM_ESCURO);
        painelCarrinho.add(lblCarrinho, gbcCarrinho);

        gbcCarrinho.gridy = 1;
        gbcCarrinho.fill = GridBagConstraints.BOTH;
        gbcCarrinho.weighty = 1.0; // Faz a tabela crescer
        gbcCarrinho.insets = new Insets(0, 20, 0, 20);
        
        String[] colunasCarrinho = { "Produto", "Qtd", "Subtotal" };
        DefaultTableModel modeloCarrinho = new DefaultTableModel(colunasCarrinho, 0);
        JTable tabelaCarrinho = new JTable(modeloCarrinho);
        configurarTabela(tabelaCarrinho);
        tabelaCarrinho.setRowHeight(35);

        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);
        scrollCarrinho.setBorder(BorderFactory.createEmptyBorder());
        painelCarrinho.add(scrollCarrinho, gbcCarrinho);

        // --- Painel de Pagamento Fixo no Fim ---
        gbcCarrinho.gridy = 2;
        gbcCarrinho.fill = GridBagConstraints.HORIZONTAL;
        gbcCarrinho.weighty = 0.0; // Fixa no fim
        gbcCarrinho.insets = new Insets(10, 0, 0, 0);
        
        JPanel pnlPagamento = new JPanel(new GridBagLayout());
        pnlPagamento.setOpaque(false);
        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.fill = GridBagConstraints.HORIZONTAL;
        gbcPay.weightx = 1.0;
        gbcPay.gridx = 0;
        gbcPay.insets = new Insets(8, 20, 8, 20);

        JTextField campoFormaPag = new RoundedTextField("Forma de pagamento...");
        campoFormaPag.setPreferredSize(new Dimension(300, 40));
        pnlPagamento.add(campoFormaPag, gbcPay);

        gbcPay.gridy = 1;
        JTextField campoCliente = new RoundedTextField("Cliente (Opcional)...");
        campoCliente.setPreferredSize(new Dimension(300, 40));
        pnlPagamento.add(campoCliente, gbcPay);
        
        gbcPay.gridy = 2;
        gbcPay.insets = new Insets(20, 20, 10, 20);
        JLabel lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(MARROM_ESCURO);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlPagamento.add(lblTotal, gbcPay);

        gbcPay.gridy = 3;
        gbcPay.insets = new Insets(5, 20, 15, 20);
        JButton btnVender = new RoundedButton("Finalizar Venda", VERDE_BOTAO, Color.WHITE, 220, 50);
        btnVender.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlPagamento.add(btnVender, gbcPay);
        
        painelCarrinho.add(pnlPagamento, gbcCarrinho);

        // Adiciona o carrinho ao painel central
        gbcCentro.gridx = 1;
        gbcCentro.weightx = 0.3;
        gbcCentro.insets = new Insets(0, 15, 0, 0); // Gap à esquerda
        painelCentro.add(painelCarrinho, gbcCentro);

        add(painelCentro, BorderLayout.CENTER);
    }
    
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
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
    }


    // ==== Componentes customizados reutilizados ====

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
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(bgColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(bgColor.brighter());
            } else {
                g2.setColor(bgColor);
            }
            
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
            g2.setColor(Color.WHITE); // Fundo branco para campos de texto
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(MARROM_CLARO);
            if (hasFocus()) {
                 g2.setStroke(new BasicStroke(2));
                 g2.setColor(MARROM_MEDIO); // Borda mais forte ao focar
            } else {
                 g2.setStroke(new BasicStroke(1));
                 g2.setColor(MARROM_CLARO);
            }
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
            repaint(); // Força o redesenho da borda
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isEmpty()) {
                setText(placeholder);
                setForeground(CINZA_PLACEHOLDER);
                showingPlaceholder = true;
            }
            repaint(); // Força o redesenho da borda
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
            
            // Desenha a borda por dentro
            g2.setColor(MARROM_CLARO);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // A Navbar precisa de um controller, mesmo que seja null para esta tela.
            // A TelaPDV agora espera um controller (para ser consistente com a Navbar).
            ProdutoController controller = null; 
            TelaPDV tela = new TelaPDV(controller);
            tela.setVisible(true);
        });
    }
}