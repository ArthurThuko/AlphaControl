package alphacontrol.views.pdv;

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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
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
import javax.swing.table.TableColumnModel;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.modais.ModalAdicionarClienteController;
import alphacontrol.dao.ClienteDAO;
import alphacontrol.models.Cliente;
import alphacontrol.views.cliente.ModalAdicionarCliente;

public class TelaAdicionarCliente extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERDE_MUSGO = new Color(119, 140, 85);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private DefaultTableModel modelo;
    private ClienteDAO clienteDAO;
    private TelaPDV telaPDV;
    private List<Cliente> listaClientes;
    private JTextField txtPesquisa;

    public TelaAdicionarCliente(TelaPDV telaPDV, ClienteDAO clienteDAO) {
        super(telaPDV, "Selecionar Cliente para Fiado", true);
        this.telaPDV = telaPDV;
        this.clienteDAO = clienteDAO;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 850); 
        setLocationRelativeTo(telaPDV);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                g2.setColor(MARROM_CLARO);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 25, 25));
                g2.dispose();
            }
        };
        painelPrincipal.setOpaque(false);
        painelPrincipal.setBorder(new EmptyBorder(40, 50, 40, 50)); 
        setContentPane(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Selecionar Cliente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.weighty = 0; // Altura fixa, não expande
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(titulo, gbc);

        // --- BARRA DE PESQUISA (CORRIGIDA) ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBusca.setOpaque(false);

        txtPesquisa = new RoundedTextField("Digite o nome ou CPF...", 25);
        txtPesquisa.setPreferredSize(new Dimension(500, 55)); // Altura um pouco maior para não cortar o texto
        
        JButton btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 160, 55);
        btnPesquisar.addActionListener(e -> filtrarTabela(txtPesquisa.getText().trim()));
        
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        gbc.gridy = 1;
        gbc.weighty = 0; // OBRIGATÓRIO: Impede que a tabela "coma" a barra de pesquisa
        gbc.fill = GridBagConstraints.NONE; // Impede deformação vertical
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(painelBusca, gbc);

        // --- TABELA ---
        String[] colunas = { "ID", "Nome", "CPF", "Telefone", "Ação" };
        modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 4; }
        };

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);
        scroll.setPreferredSize(new Dimension(900, 400)); // Define um tamanho base para o cálculo do layout

        RoundedPanel containerTabela = new RoundedPanel(15);
        containerTabela.setLayout(new BorderLayout());
        containerTabela.setBackground(BEGE_CLARO);
        containerTabela.setBorder(new EmptyBorder(15, 15, 15, 15));
        containerTabela.add(scroll, BorderLayout.CENTER);

        gbc.gridy = 2;
        gbc.weighty = 1.0; // A tabela é a única que pode expandir
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(containerTabela, gbc);

        // --- BOTÕES DE AÇÃO ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        painelBotoes.setOpaque(false);

        JButton btnNovoCliente = new RoundedButton("Novo Cliente", DOURADO_SUAVE, MARROM_ESCURO, 180, 55);
        JButton btnFechar = new RoundedButton("Fechar", VERMELHO_TERROSO, Color.WHITE, 180, 55);
        
        btnFechar.addActionListener(e -> dispose());
        btnNovoCliente.addActionListener(e -> {
            ModalAdicionarCliente modal = new ModalAdicionarCliente(telaPDV);
            new ModalAdicionarClienteController(modal, new ClienteController(clienteDAO));
            modal.setVisible(true);
            carregarDadosIniciais(); 
        });

        painelBotoes.add(btnNovoCliente);
        painelBotoes.add(btnFechar);

        gbc.gridy = 3;
        gbc.weighty = 0; // Altura fixa
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 10, 0);
        painelPrincipal.add(painelBotoes, gbc);
        
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        try {
            listaClientes = clienteDAO.listarClientes();
            filtrarTabela(""); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void filtrarTabela(String termo) {
        modelo.setRowCount(0); 
        if (listaClientes == null) return;
        String t = termo.toLowerCase();
        for (Cliente c : listaClientes) {
            if (c.getNome() != null && !c.getNome().equalsIgnoreCase("Venda Avulsa")) {
                if (t.isEmpty() || c.getNome().toLowerCase().contains(t) || (c.getCpf() != null && c.getCpf().contains(t))) {
                    modelo.addRow(new Object[]{ c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), "SELECIONAR" });
                }
            }
        }
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowVerticalLines(false);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(MARROM_CLARO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(center);
        tabela.getColumnModel().getColumn(2).setCellRenderer(center);
        tabela.getColumnModel().getColumn(3).setCellRenderer(center);
        
        tabela.getColumnModel().getColumn(4).setCellRenderer(new ButtonRendererGreen());
        tabela.getColumnModel().getColumn(4).setCellEditor(new ButtonEditorGreen(new JCheckBox()));

        TableColumnModel col = tabela.getColumnModel();
        col.getColumn(0).setPreferredWidth(60);
        col.getColumn(1).setPreferredWidth(350);
        col.getColumn(2).setPreferredWidth(180);
        col.getColumn(3).setPreferredWidth(180);
        col.getColumn(4).setPreferredWidth(150);
    }

    // --- CLASSES INTERNAS CORRIGIDAS ---

    static class RoundedTextField extends JTextField implements FocusListener {
        private String placeholder;
        private boolean showPlaceholder = true;
        public RoundedTextField(String p, int columns) {
            super(p, columns); this.placeholder = p;
            addFocusListener(this); setOpaque(false); setForeground(CINZA_PLACEHOLDER);
            setBorder(new EmptyBorder(5, 20, 5, 20)); setFont(new Font("Segoe UI", Font.BOLD, 18));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.setColor(MARROM_CLARO);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }
        public void focusGained(FocusEvent e) { if(showPlaceholder) { setText(""); setForeground(MARROM_ESCURO); showPlaceholder = false; } }
        public void focusLost(FocusEvent e) { if(getText().isEmpty()) { setText(placeholder); setForeground(CINZA_PLACEHOLDER); showPlaceholder = true; } }
        @Override public String getText() { return showPlaceholder ? "" : super.getText(); }
    }

    static class RoundedButton extends JButton {
        private Color bg;
        public RoundedButton(String t, Color bg, Color fg, int w, int h) {
            super(t); this.bg = bg;
            setForeground(fg); setPreferredSize(new Dimension(w, h));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(getForeground());
            g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()-fm.getHeight())/2 + fm.getAscent());
            g2.dispose();
        }
    }

    static class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int r) { this.radius = r; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(MARROM_CLARO);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    class ButtonRendererGreen extends JButton implements TableCellRenderer {
        public ButtonRendererGreen() { setOpaque(false); setBorderPainted(false); setContentAreaFilled(false); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean h, int r, int c) { return this; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(VERDE_OLIVA);
            g2.fill(new RoundRectangle2D.Double(8, 8, getWidth()-16, getHeight()-16, 12, 12));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("SELECIONAR", (getWidth()-fm.stringWidth("SELECIONAR"))/2, (getHeight()-fm.getHeight())/2 + fm.getAscent());
            g2.dispose();
        }
    }

    class ButtonEditorGreen extends DefaultCellEditor {
        private JButton btn;
        public ButtonEditorGreen(JCheckBox cb) {
            super(cb); btn = new JButton();
            btn.addActionListener(e -> {
                int row = ((JTable)btn.getParent()).getSelectedRow();
                if(row != -1) {
                    int id = (int) modelo.getValueAt(row, 0);
                    Cliente selecionado = listaClientes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
                    telaPDV.setClienteSelecionado(selecionado);
                    stopCellEditing();
                    dispose();
                }
            });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { return btn; }
    }
}