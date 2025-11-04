package alphacontrol.views.fiado;

import alphacontrol.controllers.ClienteController;
import alphacontrol.controllers.FiadoController;
import alphacontrol.controllers.ModalAdicionarFiadoController;
import alphacontrol.models.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class ModalAdicionarFiado extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);


    private JComboBox<Cliente> cmbClientes;
    private JTextField txtValor;
    private JButton btnSalvar;
    private JButton btnAdicionarCliente;
    private TelaFiado parentView;

    public ModalAdicionarFiado(TelaFiado parent, FiadoController fiadoController, ClienteController clienteController) {
        super(parent, "Adicionar Fiado", true);
        this.parentView = parent;

        setUndecorated(true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelFundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(MARROM_CLARO);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        painelFundo.setOpaque(false);
        painelFundo.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(painelFundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Registrar Fiado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(MARROM_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelFundo.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.1;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCliente.setForeground(MARROM_ESCURO);
        painelFundo.add(lblCliente, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9;
        cmbClientes = new JComboBox<>();
        cmbClientes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cmbClientes.setRenderer(new ClienteComboBoxRenderer());
        cmbClientes.setUI(new CustomComboBoxUI());
        cmbClientes.setPreferredSize(new Dimension(200, 45));
        painelFundo.add(cmbClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.1;
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValor.setForeground(MARROM_ESCURO);
        painelFundo.add(lblValor, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9;
        txtValor = new RoundedTextField("0,00", 15);
        txtValor.setPreferredSize(new Dimension(200, 45));
        painelFundo.add(txtValor, gbc);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);

        btnSalvar = new RoundedButton("Salvar", VERDE_OLIVA, Color.WHITE, 120, 40);
        btnAdicionarCliente = new RoundedButton("Novo Cliente", DOURADO_SUAVE, MARROM_ESCURO, 120, 40);
        JButton btnCancelar = new RoundedButton("Cancelar", VERMELHO_TERROSO, Color.WHITE, 120, 40);
        
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAdicionarCliente);
        painelBotoes.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        painelFundo.add(painelBotoes, gbc);

        new ModalAdicionarFiadoController(this, fiadoController, clienteController);
    }

    public TelaFiado getParentView() {
        return parentView;
    }

    public JComboBox<Cliente> getCmbClientes() {
        return cmbClientes;
    }

    public JTextField getTxtValor() {
        return txtValor;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnAdicionarCliente() {
        return btnAdicionarCliente;
    }

    static class ClienteComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Cliente) {
                Cliente cliente = (Cliente) value;
                setText(cliente.getNome());
            }

            if (isSelected) {
                setBackground(MARROM_CLARO.brighter());
                setForeground(MARROM_ESCURO);
            } else {
                setBackground(Color.WHITE);
                setForeground(MARROM_ESCURO);
            }
            return this;
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
            setFont(new Font("Segoe UI", Font.BOLD, 14));
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

    static class CustomComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton("▼");
            button.setBackground(MARROM_MEDIO);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder());
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(Color.WHITE);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = (BasicComboPopup) super.createPopup();
            popup.setBorder(BorderFactory.createLineBorder(MARROM_CLARO, 1));
            return popup;
        }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 15, 15);
            g2.setColor(MARROM_CLARO);
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 15, 15);
            g2.dispose();
            
            Rectangle r = rectangleForCurrentValue();
            paintCurrentValue(g, r, hasFocus);
        }
    }
}