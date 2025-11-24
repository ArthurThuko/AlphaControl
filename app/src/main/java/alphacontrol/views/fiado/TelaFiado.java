package alphacontrol.views.fiado;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.modais.ModalAdicionarClienteController;
import alphacontrol.controllers.modais.ModalEditarFiadoController;
import alphacontrol.controllers.modais.ModalQuitarDividaController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.models.Cliente;
import alphacontrol.views.cliente.ModalAdicionarCliente;
import alphacontrol.views.components.Navbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.EventObject;
import java.util.List;

public class TelaFiado extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private static final Color VERDE_MUSGO = new Color(119, 140, 85);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);

    private DefaultTableModel modelo;
    private JTable tabela;
    private ClienteController clienteController;
    private FiadoController fiadoController;
    private JTextField txtPesquisa;

    public TelaFiado(TelaPrincipalController mainController) {
        this.clienteController = mainController.getClienteController();
        this.fiadoController = mainController.getFiadoController();

        setTitle("Fiados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);

        setJMenuBar(new Navbar(this, mainController, "Fiado"));

        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(BEGE_FUNDO);
        add(painelFundo, BorderLayout.CENTER);

        RoundedPanel painelCentral = new RoundedPanel(15);
        painelCentral.setBackground(BEGE_CLARO);
        painelCentral.setLayout(new GridBagLayout());
        painelCentral.setPreferredSize(null); // permite redimensionar automaticamente

        GridBagConstraints gbcFundo = new GridBagConstraints();
        gbcFundo.fill = GridBagConstraints.BOTH;
        gbcFundo.weightx = 1.0;
        gbcFundo.weighty = 1.0;
        painelFundo.add(painelCentral, gbcFundo);

        // Configuração principal do painel interno
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 25, 15, 25);

        JLabel titulo = new JLabel("Controle de Fiados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 25, 30, 25);
        painelCentral.add(titulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 25, 20, 25);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelBusca.setOpaque(false);

        txtPesquisa = new RoundedTextField("Pesquise por nome...", 25);
        txtPesquisa.setPreferredSize(new Dimension(350, 45));

        JButton btnPesquisar = new RoundedButton("Pesquisar", VERDE_MUSGO, Color.WHITE, 150, 45);
        btnPesquisar.addActionListener(e -> atualizarTabela());

        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);
        painelCentral.add(painelBusca, gbc);

        JPanel painelAdd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelAdd.setOpaque(false);
        JButton btnCriarFiado = new RoundedButton("Criar Fiado", VERDE_OLIVA, Color.WHITE, 220, 45);
        btnCriarFiado.addActionListener(e -> abrirModalAdicionarFiado());

        painelAdd.add(btnCriarFiado);

        gbc.gridx = 1;
        painelCentral.add(painelAdd, gbc);

        String[] colunas = { "ID", "Nome", "Endereço", "Telefone", "Débito", "Ações" };

        modelo = new DefaultTableModel(null, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tabela = new JTable(modelo);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setOpaque(false);
        painelTabela.add(scroll, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 25, 20, 25);
        painelCentral.add(painelTabela, gbc);

        atualizarTabela();
    }

    public void abrirModalAdicionarCliente() {
        ModalAdicionarCliente modal = new ModalAdicionarCliente(this);
        new ModalAdicionarClienteController(modal, this.clienteController);
        modal.setVisible(true);
        atualizarTabela();
    }

    private void abrirModalAdicionarFiado() {
        ModalAdicionarFiado modal = new ModalAdicionarFiado(
                this, 
                this.fiadoController, 
                this.clienteController
        );
        modal.setVisible(true);
        atualizarTabela();
    }

    private void abrirTelaDetalheFiado(int clienteId) {
        Cliente cliente = clienteController.buscarPorId(clienteId);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
            return;
        }

        TelaDetalheFiado telaDetalhe = new TelaDetalheFiado(cliente, this.fiadoController, this);
        telaDetalhe.setVisible(true);
    }

    public void atualizarTabela() {
        modelo.setRowCount(0);
        List<Cliente> clientes = clienteController.pesquisar(txtPesquisa.getText());
        for (Cliente c : clientes) {
            if (c.getDebito() > 0) {
                modelo.addRow(new Object[]{
                        c.getId(),
                        c.getNome(),
                        c.getEnderecoCompleto(),
                        c.getTelefone(),
                        c.getDebito(),
                        ""
                });
            }
        }
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));

        PaddedCellRenderer centerRenderer = new PaddedCellRenderer(SwingConstants.CENTER);
        PaddedCellRenderer leftRenderer = new PaddedCellRenderer(SwingConstants.LEFT);

        tabela.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer());

        tabela.getColumn("Ações").setCellRenderer(new ActionsCellRenderer());
        tabela.getColumn("Ações")
                .setCellEditor(new ActionsCellEditor(this, tabela, clienteController, fiadoController));

        TableColumn colId = tabela.getColumnModel().getColumn(0);
        colId.setMinWidth(0);
        colId.setMaxWidth(0);
        colId.setPreferredWidth(0);

        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(1).setPreferredWidth(350);
        colModel.getColumn(2).setPreferredWidth(250);
        colModel.getColumn(3).setPreferredWidth(150);
        colModel.getColumn(4).setPreferredWidth(120);
        colModel.getColumn(5).setMinWidth(350);
        colModel.getColumn(5).setMaxWidth(360);
    }

    static class CurrencyRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat FORMATTER = new DecimalFormat("R$ #,##0.00");

        public CurrencyRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                setText(FORMATTER.format(value));
            } else {
                super.setValue(value);
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected)
                c.setBackground(table.getBackground());

            if (value instanceof Number && ((Number) value).doubleValue() > 0) {
                c.setForeground(VERMELHO_TERROSO);
                c.setFont(new Font("Segoe UI", Font.BOLD, 16));
            } else {
                c.setForeground(MARROM_ESCURO);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            }
            return c;
        }
    }

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer(int alignment) {
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setHorizontalAlignment(alignment);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setVerticalAlignment(SwingConstants.CENTER);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
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
            g2.setColor(getModel().isRollover() ? hoverColor : backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedTextField extends JTextField implements FocusListener {
        private final String placeholder;
        private boolean showingPlaceholder;

        public RoundedTextField(String placeholder, int columns) {
            super(placeholder, columns);
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
            g2.setColor(Color.WHITE);
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
            g2.setColor(getModel().isRollover() ? getBackground().brighter() : getBackground());
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ActionsPanel extends JPanel {
        public JButton btnVer = new CellButton("Ver Débitos", VERDE_MUSGO, Color.WHITE);
        public JButton btnEditar = new CellButton("Editar", DOURADO_SUAVE, MARROM_ESCURO);
        public JButton btnPagar = new CellButton("Quitar", VERDE_OLIVA, Color.WHITE);

        public ActionsPanel() {
            super(new FlowLayout(FlowLayout.CENTER, 10, 0));
            setOpaque(true);
            setAlignmentY(Component.CENTER_ALIGNMENT);

            Dimension btnSize = new Dimension(100, 40);
            btnVer.setPreferredSize(btnSize);
            btnEditar.setPreferredSize(btnSize);
            btnPagar.setPreferredSize(btnSize);

            add(btnVer);
            add(btnEditar);
            add(btnPagar);
        }
    }

    static class ActionsCellRenderer extends ActionsPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
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
        private TelaFiado telaFiado;
        private ClienteController clienteController;
        private FiadoController fiadoController;

        public ActionsCellEditor(TelaFiado parentFrame, JTable table, ClienteController cController,
                FiadoController fController) {
            this.telaFiado = parentFrame;
            this.clienteController = cController;
            this.fiadoController = fController;

            panel.btnVer.addActionListener(e -> {
                int id = (int) table.getValueAt(row, 0);
                telaFiado.abrirTelaDetalheFiado(id);
                fireEditingStopped();
            });

            panel.btnEditar.addActionListener(e -> {
                int id = (int) table.getValueAt(row, 0);
                Cliente cliente = clienteController.buscarPorId(id);

                if (cliente == null) {
                    JOptionPane.showMessageDialog(table, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    fireEditingStopped();
                    return;
                }

                ModalEditarFiado modal = new ModalEditarFiado(this.telaFiado, cliente);
                new ModalEditarFiadoController(modal, clienteController);
                modal.setVisible(true);
                this.telaFiado.atualizarTabela();
                fireEditingStopped();
            });

            panel.btnPagar.addActionListener(e -> {
                int id = (int) table.getValueAt(row, 0);
                double valor = (double) table.getValueAt(row, 4);

                if (valor == 0) {
                    JOptionPane.showMessageDialog(table, "Cliente não possui débitos.");
                    fireEditingStopped();
                    return;
                }

                Cliente cliente = clienteController.buscarPorId(id);
                if (cliente == null) {
                    JOptionPane.showMessageDialog(table, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    fireEditingStopped();
                    return;
                }

                ModalQuitarDivida modal = new ModalQuitarDivida(this.telaFiado, cliente, this.fiadoController);
                new ModalQuitarDividaController(modal);
                modal.setVisible(true);
                
                this.telaFiado.atualizarTabela();
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.row = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
    }

    static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer(JTable table) {
            JTableHeader header = table.getTableHeader();
            setOpaque(true);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
            setBorder(new EmptyBorder(0, 15, 0, 15));
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            if (column == 3 || column == 4) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            return this;
        }
    }
}