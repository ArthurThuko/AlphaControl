package alphacontrol.views.fiado;

import alphacontrol.controllers.FiadoController;
import alphacontrol.models.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

public class ModalQuitarDivida extends JDialog {

    private Point mouseClickPoint;
    private FiadoController fiadoController;
    private Cliente cliente;

    private JTextField txtValorPagar;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);

    public ModalQuitarDivida(JFrame parent, Cliente cliente, FiadoController fiadoController) {
        super(parent, "Quitar Dívida", true);
        this.cliente = cliente;
        this.fiadoController = fiadoController;

        JPanel painel = criarPainelPrincipal();
        add(painel);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(500, 400); // Reduzido a altura
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(0, 0, 0, 0));

        painel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });
        painel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = e.getLocationOnScreen();
                setLocation(p.x - mouseClickPoint.x, p.y - mouseClickPoint.y);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel("Realizar Pagamento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(MARROM_ESCURO);
        gbc.insets = new Insets(10, 10, 10, 10);
        painel.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 0, 10);
        JLabel lblNome = new JLabel("Cliente: " + cliente.getNome(), SwingConstants.CENTER);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNome.setForeground(MARROM_ESCURO);
        painel.add(lblNome, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 10, 15, 10);
        JLabel lblDivida = new JLabel("Dívida Total: " + new DecimalFormat("R$ #,##0.00").format(cliente.getDebito()), SwingConstants.CENTER);
        lblDivida.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblDivida.setForeground(VERMELHO_TERROSO);
        painel.add(lblDivida, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0;
        JLabel lblValor = new JLabel("Valor a Pagar:", SwingConstants.RIGHT);
        lblValor.setForeground(MARROM_ESCURO);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        painel.add(lblValor, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        txtValorPagar = criarCampo();
        painel.add(txtValorPagar, gbc);
        gbc.gridy++;

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);
        
        btnSalvar = criarBotao("Salvar", VERDE_OLIVA, BEGE_CLARO);
        btnCancelar = criarBotao("Cancelar", VERMELHO_TERROSO, BEGE_CLARO);
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        painel.add(painelBotoes, gbc);

        return painel;
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField(10);
        campo.setOpaque(true);
        campo.setBackground(BEGE_CLARO);
        campo.setForeground(MARROM_ESCURO);
        campo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MARROM_CLARO, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return campo;
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? corFundo.darker() : corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public String getTxtValorPagar() {
        return txtValorPagar.getText();
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public FiadoController getFiadoController() {
        return fiadoController;
    }
}