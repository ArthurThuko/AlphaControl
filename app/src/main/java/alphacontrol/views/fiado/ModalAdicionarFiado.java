package alphacontrol.views.fiado;

import java.awt.BasicStroke;
import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import alphacontrol.controllers.cliente.ClienteController;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.modais.ModalAdicionarFiadoController;
import alphacontrol.models.Cliente;

public class ModalAdicionarFiado extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);
    private static final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    private JLabel lblClienteNome;
    private JButton btnBuscarCliente, btnSalvar, btnAdicionarCliente;
    private JTextField txtValor;
    private TelaFiado parentView;
    private Cliente clienteSelecionado;

    public ModalAdicionarFiado(TelaFiado parent, FiadoController fiadoController, ClienteController clienteController) {
        super(parent, "Adicionar Fiado", true);
        this.parentView = parent;

        setUndecorated(true);
        setSize(850, 600);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelFundo = new JPanel(new GridBagLayout()) {
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
                super.paintComponent(g);
            }
        };
        painelFundo.setOpaque(false);
        painelFundo.setBorder(new EmptyBorder(50, 60, 50, 60));
        setContentPane(painelFundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Registrar Fiado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        painelFundo.add(lblTitulo, gbc);

        // LINHA DO CLIENTE
        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        painelCliente.setOpaque(false);
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCliente.setForeground(MARROM_ESCURO);
        lblCliente.setPreferredSize(new Dimension(120, 30));
        painelCliente.add(lblCliente);

        lblClienteNome = new JLabel("Nenhum cliente...");
        lblClienteNome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblClienteNome.setForeground(CINZA_PLACEHOLDER);
        lblClienteNome.setPreferredSize(new Dimension(280, 50));
        lblClienteNome.setOpaque(true);
        lblClienteNome.setBackground(new Color(235, 235, 235));
        lblClienteNome.setBorder(new EmptyBorder(10, 15, 10, 15));

        painelCliente.add(lblClienteNome);

        btnBuscarCliente = new RoundedButton("Buscar", MARROM_MEDIO, Color.WHITE, 120, 50);

        btnBuscarCliente.addActionListener(e -> {
            alphacontrol.views.cliente.ModalBuscaCliente modalBusca = new alphacontrol.views.cliente.ModalBuscaCliente(
                    this, clienteController);
            modalBusca.setVisible(true);

            if (modalBusca.getClienteSelecionado() != null) {
                setClienteSelecionado(modalBusca.getClienteSelecionado());
            }
        });

        painelCliente.add(btnBuscarCliente);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        painelFundo.add(painelCliente, gbc);

        // LINHA DO VALOR
        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        painelValor.setOpaque(false);
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(MARROM_ESCURO);
        lblValor.setPreferredSize(new Dimension(120, 30));
        painelValor.add(lblValor);
        txtValor = new RoundedTextField("0,00", 15);
        txtValor.setPreferredSize(new Dimension(425, 50));
        painelValor.add(txtValor);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 60, 0);
        painelFundo.add(painelValor, gbc);

        // BOTÕES
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        painelBotoes.setOpaque(false);
        btnSalvar = new RoundedButton("Salvar", VERDE_OLIVA, Color.WHITE, 160, 50);
        btnAdicionarCliente = new RoundedButton("Novo Cliente", DOURADO_SUAVE, MARROM_ESCURO, 160, 50);
        btnAdicionarCliente.addActionListener(e -> {
            alphacontrol.views.cliente.ModalAdicionarCliente modalAdd = new alphacontrol.views.cliente.ModalAdicionarCliente(
                    parentView);
            new alphacontrol.controllers.modais.ModalAdicionarClienteController(modalAdd, clienteController);
            modalAdd.setVisible(true);
        });
        JButton btnCancelar = new RoundedButton("Cancelar", VERMELHO_TERROSO, Color.WHITE, 160, 50);
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAdicionarCliente);
        painelBotoes.add(btnCancelar);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelFundo.add(painelBotoes, gbc);

        new ModalAdicionarFiadoController(this, fiadoController, clienteController);
    }

    public void setClienteSelecionado(Cliente cliente) {
        this.clienteSelecionado = cliente;
        if (cliente != null) {
            this.lblClienteNome.setText(cliente.getNome());
            this.lblClienteNome.setForeground(MARROM_ESCURO);
            this.lblClienteNome.setBackground(Color.WHITE);
        }
    }

    public JTextField getTxtValor() {
        return txtValor;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    // --- CLASSES INTERNAS PARA CORRIGIR OS ERROS ---

    static class RoundedTextField extends JTextField implements FocusListener {
        private final String placeholder;
        private boolean showingPlaceholder;

        public RoundedTextField(String placeholder, int columns) {
            super(placeholder, columns);
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            addFocusListener(this);
            setForeground(CINZA_PLACEHOLDER);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setOpaque(false);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEditable() ? Color.WHITE : new Color(235, 235, 235));
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
        private final Color backgroundColor;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            this.backgroundColor = bg;
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
            g2.setColor(getModel().isRollover() ? backgroundColor.brighter() : backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }
    }
}