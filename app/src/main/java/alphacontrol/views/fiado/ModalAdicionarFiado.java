package alphacontrol.views.fiado;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.modais.ModalAdicionarFiadoController;
import alphacontrol.models.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class ModalAdicionarFiado extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private JTextField txtClienteNome;
    private JButton btnBuscarCliente;
    private JTextField txtValor;
    private JButton btnSalvar;
    private JButton btnAdicionarCliente;
    private TelaFiado parentView;
    private Cliente clienteSelecionado;

    public ModalAdicionarFiado(TelaFiado parent, FiadoController fiadoController, ClienteController clienteController) {
        super(parent, "Adicionar Fiado", true);
        this.parentView = parent;

        setUndecorated(true);
        setSize(600, 380); // Altura ajustada
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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 20, 10);
        painelFundo.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblCliente.setForeground(MARROM_ESCURO);
        painelFundo.add(lblCliente, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        txtClienteNome = new RoundedTextField("Nenhum cliente selecionado", 20);
        txtClienteNome.setEditable(false);
        txtClienteNome.setPreferredSize(new Dimension(200, 45));
        painelFundo.add(txtClienteNome, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        btnBuscarCliente = new RoundedButton("Buscar", MARROM_MEDIO, Color.WHITE, 120, 45);
        painelFundo.add(btnBuscarCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.3;
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblValor.setForeground(MARROM_ESCURO);
        painelFundo.add(lblValor, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        txtValor = new RoundedTextField("0,00", 15);
        txtValor.setPreferredSize(new Dimension(200, 45));
        painelFundo.add(txtValor, gbc);
        
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setOpaque(false);
        
        GridBagConstraints gbcBotoes = new GridBagConstraints();
        gbcBotoes.insets = new Insets(0, 10, 0, 10);

        btnSalvar = new RoundedButton("Salvar", VERDE_OLIVA, Color.WHITE, 140, 45);
        btnAdicionarCliente = new RoundedButton("Novo Cliente", DOURADO_SUAVE, MARROM_ESCURO, 140, 45);
        JButton btnCancelar = new RoundedButton("Cancelar", VERMELHO_TERROSO, Color.WHITE, 140, 45);
        
        btnCancelar.addActionListener(e -> dispose());
        
        gbcBotoes.gridx = 0;
        painelBotoes.add(btnSalvar, gbcBotoes);
        
        gbcBotoes.gridx = 1;
        painelBotoes.add(btnAdicionarCliente, gbcBotoes);
        
        gbcBotoes.gridx = 2;
        painelBotoes.add(btnCancelar, gbcBotoes);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 10, 10, 10);
        painelFundo.add(painelBotoes, gbc);

        new ModalAdicionarFiadoController(this, fiadoController, clienteController);
    }

    public TelaFiado getParentView() {
        return parentView;
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Cliente cliente) {
        this.clienteSelecionado = cliente;
        if (cliente != null) {
            this.txtClienteNome.setText(cliente.getNome());
            this.txtClienteNome.setForeground(MARROM_ESCURO);
        } else {
            this.txtClienteNome.setText("Nenhum cliente selecionado");
            this.txtClienteNome.setForeground(CINZA_PLACEHOLDER);
        }
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
    
    public JButton getBtnBuscarCliente() {
        return btnBuscarCliente;
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
            
            if (isEditable()) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(new Color(235, 235, 235));
            }

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
            return (showingPlaceholder && isEditable()) ? "" : super.getText();
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
}