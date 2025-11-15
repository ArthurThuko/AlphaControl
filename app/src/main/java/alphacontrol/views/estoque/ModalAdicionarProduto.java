package alphacontrol.views.estoque;

import alphacontrol.models.Produto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalAdicionarProduto extends JDialog {
    
    private final JTextField txtNome;
    private final JTextField txtCategoria;
    private final JTextField txtCompra;
    private final JTextField txtVenda;
    private final JTextField txtQnt;
    private final JTextField txtQntMinima; 
    
    private final JButton btnSalvar;

    private Point mouseClickPoint;

    public ModalAdicionarProduto(JFrame parent) {
        super(parent, "Adicionar Produto", true);

        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);

        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(begeFundo);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        JLabel titulo = new JLabel("Adicionar Produto", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(marromEscuro);
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        painel.add(criarLabel("Nome:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtNome = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtNome, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("Categoria:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtCategoria = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtCategoria, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Preço de Custo (R$):", marromEscuro), gbc);
        gbc.gridx = 1;
        txtCompra = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtCompra, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("Valor Venda (R$):", marromEscuro), gbc);
        gbc.gridx = 1;
        txtVenda = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtVenda, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Quantidade:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtQnt = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtQnt, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Alerta Estoque Min.:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtQntMinima = criarCampo(begeClaro, marromClaro, marromEscuro);
        painel.add(txtQntMinima, gbc);
        gbc.gridy++;

        btnSalvar = new JButton("Salvar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? marromEscuro : marromMedio);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSalvar.setForeground(begeClaro);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton botaoFechar = new JButton("Fechar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? marromClaro.darker() : marromClaro);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(marromEscuro);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        botaoFechar.setForeground(begeClaro);
        botaoFechar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setContentAreaFilled(false);
        botaoFechar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoFechar, gbc);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        add(painel);
        setSize(500, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private JTextField criarCampo(Color fundo, Color borda, Color texto) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setForeground(texto);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return campo;
    }
    
    private JLabel criarLabel(String texto, Color cor) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setForeground(cor);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }

    public JButton getBtnSalvar() { 
        return btnSalvar; 
    }

    public Produto getProdutoFromFields() {
        return new Produto(
            txtNome.getText(),
            txtCategoria.getText(),
            Double.parseDouble(txtCompra.getText()),
            Double.parseDouble(txtVenda.getText()),
            Integer.parseInt(txtQnt.getText()),
            Integer.parseInt(txtQntMinima.getText())
        );
    }

    public void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}