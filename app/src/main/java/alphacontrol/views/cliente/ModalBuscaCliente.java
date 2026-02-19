package alphacontrol.views.cliente;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
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
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.modais.ModalBuscaClienteController;
import alphacontrol.models.Cliente;

public class ModalBuscaCliente extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_MUSGO = new Color(119, 140, 85);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtPesquisa;
    private JButton btnPesquisar, btnSelecionar;
    private Cliente clienteSelecionado = null;

    public ModalBuscaCliente(Dialog owner, ClienteController clienteController) {
        super(owner, "Buscar Cliente", true); // O 'true' faz o código pai esperar aqui

        setSize(1000, 800); 
        setLocationRelativeTo(owner);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                g2.setColor(MARROM_CLARO);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, 25, 25));
                g2.dispose();
            }
        };
        painelPrincipal.setOpaque(false);
        painelPrincipal.setBorder(new EmptyBorder(60, 70, 60, 70)); 
        setContentPane(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Buscar Cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(MARROM_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        painelPrincipal.add(lblTitulo, gbc);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBusca.setOpaque(false);
        txtPesquisa = new RoundedTextField("Digite o nome...", 25);
        txtPesquisa.setPreferredSize(new Dimension(500, 50)); 
        btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 160, 50);
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(painelBusca, gbc);

        String[] colunas = { "ID", "Nome", "CPF", "Telefone" };
        modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        configurarTabela();

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(5, 5, 5, 5));
        painelTabela.add(scroll, BorderLayout.CENTER);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 25, 0);
        painelPrincipal.add(painelTabela, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBotoes.setOpaque(false);
        btnSelecionar = new RoundedButton("Selecionar", VERDE_OLIVA, Color.WHITE, 160, 50);
        JButton btnFechar = new RoundedButton("Cancelar", VERMELHO_TERROSO, Color.WHITE, 160, 50);
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnSelecionar);
        painelBotoes.add(btnFechar);
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(painelBotoes, gbc);

        new ModalBuscaClienteController(this, clienteController);
    }

    // MÉTODO CHAVE: Usado pelo Controller para fechar devolvendo o cliente
    public void finalizarSelecao(Cliente cliente) {
        this.clienteSelecionado = cliente;
        this.dispose(); 
    }

    private void configurarTabela() {
        tabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabela.setRowHeight(50);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = tabela.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
        
        TableColumn colId = tabela.getColumnModel().getColumn(0);
        colId.setMinWidth(0); colId.setMaxWidth(0); colId.setPreferredWidth(0);
    }

    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTextField getTxtPesquisa() { return txtPesquisa; }
    public JButton getBtnPesquisar() { return btnPesquisar; }
    public JButton getBtnSelecionar() { return btnSelecionar; }
    public Cliente getClienteSelecionado() { return this.clienteSelecionado; }

    // Classes Internas (RoundedPanel, RoundedButton, RoundedTextField...)
    // [Mantidas conforme sua versão anterior]
    static class RoundedPanel extends JPanel {
        private int r; public RoundedPanel(int radius) { this.r = radius; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r); g2.dispose();
        }
    }
    static class RoundedButton extends JButton {
        private Color bg; public RoundedButton(String t, Color bg, Color fg, int w, int h) {
            super(t); this.bg = bg; setForeground(fg); setPreferredSize(new Dimension(w,h));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(getForeground()); g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()-fm.getHeight())/2 + fm.getAscent());
            g2.dispose();
        }
    }
    static class RoundedTextField extends JTextField {
        public RoundedTextField(String p, int c) { super(p, c); setOpaque(false); setBorder(new EmptyBorder(5,15,5,15)); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,15,15); g2.dispose();
            super.paintComponent(g);
        }
    }
}