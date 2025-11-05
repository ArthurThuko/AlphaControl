package alphacontrol.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TelaPDV extends JFrame {

    // ==== Cores base ====
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_BOTAO = new Color(72, 166, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    public TelaPDV() {
        setTitle("PDV - Ponto de Venda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BEGE_FUNDO);
        setLayout(new BorderLayout(20, 20));

        // ==== TOPO ====
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        painelTopo.setBackground(BEGE_CLARO);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Linha 1: Alerta e título
        JPanel linha1 = new JPanel(new BorderLayout());
        linha1.setBackground(BEGE_CLARO);

        // Alerta à esquerda
        JLabel lblAlerta = new JLabel("⚠ Alerta - Produto X com estoque baixo");
        lblAlerta.setOpaque(true);
        lblAlerta.setBackground(new Color(255, 230, 230)); // leve vermelho de aviso
        lblAlerta.setForeground(new Color(150, 40, 40));
        lblAlerta.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAlerta.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Título centralizado
        JLabel lblTitulo = new JLabel("PDV", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        linha1.add(lblTitulo, BorderLayout.CENTER);

        painelTopo.add(linha1);
        painelTopo.add(Box.createVerticalStrut(15)); // espaçamento entre as linhas

        // Linha 2: Campos de busca
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        linha2.setBackground(BEGE_CLARO);

        JTextField campoPesquisa = new RoundedTextField("Pesquisar produto...");
        campoPesquisa.setPreferredSize(new Dimension(250, 40));

        JTextField campoCategoria = new RoundedTextField("Filtrar categoria...");
        campoCategoria.setPreferredSize(new Dimension(200, 40));

        RoundedButton btnPesquisar = new RoundedButton("Pesquisar", new Color(60, 150, 80), Color.WHITE, 140, 40);

        linha2.add(campoPesquisa);
        linha2.add(campoCategoria);
        linha2.add(btnPesquisar);
        linha2.add(lblAlerta);

        painelTopo.add(linha2);

        add(painelTopo, BorderLayout.NORTH);

        // ==== CENTRO ====
        JPanel painelCentro = new JPanel(new GridBagLayout());
        painelCentro.setBackground(BEGE_FUNDO);
        painelCentro.setBorder(new EmptyBorder(10, 40, 20, 40));

        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.insets = new Insets(10, 10, 10, 10);
        gbcCentro.fill = GridBagConstraints.BOTH;
        gbcCentro.weighty = 1;

        // === TABELA DE PRODUTOS ===
        String[] colunas = { "Nome", "Quantidade", "Categoria", "Valor Venda", "Ações" };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        JTable tabelaProdutos = new JTable(modelo);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabelaProdutos.setRowHeight(35);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaProdutos.getTableHeader().setBackground(MARROM_CLARO);
        tabelaProdutos.getTableHeader().setForeground(Color.WHITE);
        tabelaProdutos.setBackground(BEGE_CLARO);
        tabelaProdutos.setForeground(MARROM_ESCURO);
        tabelaProdutos.setSelectionBackground(MARROM_CLARO.brighter());

        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.getViewport().setBackground(BEGE_CLARO);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());

        RoundedPanel painelTabela = new RoundedPanel(20);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        gbcCentro.gridx = 0;
        gbcCentro.weightx = 0.7;
        painelCentro.add(painelTabela, gbcCentro);

        // === CARRINHO ===
        RoundedPanel painelCarrinho = new RoundedPanel(20);
        painelCarrinho.setLayout(new GridBagLayout());
        painelCarrinho.setBackground(BEGE_CLARO);

        GridBagConstraints gbcCarrinho = new GridBagConstraints();
        gbcCarrinho.insets = new Insets(10, 10, 10, 10);
        gbcCarrinho.fill = GridBagConstraints.HORIZONTAL;
        gbcCarrinho.gridx = 0;
        gbcCarrinho.weightx = 1;

        JLabel lblCarrinho = new JLabel("Carrinho");
        lblCarrinho.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblCarrinho.setForeground(MARROM_ESCURO);
        painelCarrinho.add(lblCarrinho, gbcCarrinho);

        gbcCarrinho.gridy = 1;
        String[] colunasCarrinho = { "Produto", "Qtd", "Subtotal" };
        DefaultTableModel modeloCarrinho = new DefaultTableModel(colunasCarrinho, 0);
        JTable tabelaCarrinho = new JTable(modeloCarrinho);
        tabelaCarrinho.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabelaCarrinho.setRowHeight(35);
        tabelaCarrinho.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaCarrinho.getTableHeader().setBackground(MARROM_CLARO);
        tabelaCarrinho.getTableHeader().setForeground(Color.WHITE);
        tabelaCarrinho.setBackground(BEGE_CLARO);
        tabelaCarrinho.setForeground(MARROM_ESCURO);

        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);
        painelCarrinho.add(scrollCarrinho, gbcCarrinho);

        gbcCarrinho.gridy++;
        JTextField campoFormaPag = new RoundedTextField("Forma de pagamento...");
        campoFormaPag.setPreferredSize(new Dimension(300, 40));
        painelCarrinho.add(campoFormaPag, gbcCarrinho);

        gbcCarrinho.gridy++;
        JTextField campoCliente = new RoundedTextField("Cliente...");
        campoCliente.setPreferredSize(new Dimension(300, 40));
        painelCarrinho.add(campoCliente, gbcCarrinho);

        gbcCarrinho.gridy++;
        JLabel lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(MARROM_ESCURO);
        painelCarrinho.add(lblTotal, gbcCarrinho);

        gbcCarrinho.gridy++;
        JButton btnVender = new RoundedButton("Vender", VERDE_BOTAO, Color.WHITE, 220, 50);
        painelCarrinho.add(btnVender, gbcCarrinho);

        gbcCentro.gridx = 1;
        gbcCentro.weightx = 0.3;
        painelCentro.add(painelCarrinho, gbcCentro);

        add(painelCentro, BorderLayout.CENTER);
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
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
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
            g2.setColor(BEGE_CLARO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
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
            g2.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPDV tela = new TelaPDV();
            tela.setVisible(true);
        });
    }
}