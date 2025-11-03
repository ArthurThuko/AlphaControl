package alphacontrol.views.estoque;

import alphacontrol.models.Produto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalEditarProduto extends JDialog {

    private Point mouseClickPoint;
    public JTextField[] campos;
    
    private final JButton botaoSalvar; // Botão Salvar agora é um campo
    private int produtoId; // Guarda o ID do produto sendo editado

    public ModalEditarProduto(JFrame parent) {
        super(parent, "Editar Produto", true);

        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);

        // Painel principal
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

        // Permitir arrastar
        painel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseClickPoint = e.getPoint(); }
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

        // Título centralizado
        JLabel titulo = new JLabel("Editar Produto", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(marromEscuro);
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Campos
        String[] labels = {
                "Nome:", "Quantidade:", "Alerta Estoque Min.:",
                "Categoria:", "Valor Compra (R$):", "Valor Venda (R$):"
        };
        campos = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            JLabel lbl = new JLabel(labels[i], SwingConstants.CENTER);
            lbl.setForeground(marromEscuro);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            painel.add(lbl, gbc);

            gbc.gridx = 1;
            campos[i] = criarCampo(begeClaro, marromClaro, marromEscuro);
            gbc.weightx = 1;
            painel.add(campos[i], gbc);
            gbc.gridy++;
        }

        // Botão Salvar Alterações
        botaoSalvar = new JButton("Salvar Alterações") {
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
        botaoSalvar.setForeground(begeClaro);
        botaoSalvar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botaoSalvar.setFocusPainted(false);
        botaoSalvar.setContentAreaFilled(false);
        botaoSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // O ActionListener foi REMOVIDO daqui

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(botaoSalvar, gbc);
        gbc.gridy++;

        // Botão Fechar estilizado
        JButton botaoFechar = new JButton("Fechar") {
             // ... (código de paintComponent do botão fechar) ...
        };
        botaoFechar.setForeground(begeClaro);
        botaoFechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setContentAreaFilled(false);
        botaoFechar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoFechar, gbc);

        // Configurações do modal
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
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return campo;
    }
    
    // --- Métodos Getters/Setters para o Controller ---

    public JButton getBtnSalvar() {
        return botaoSalvar;
    }

    /**
     * Preenche os campos do modal com os dados de um produto existente.
     */
    public void setProduto(Produto p) {
        this.produtoId = p.getProdutoId(); // Guarda o ID
        
        campos[0].setText(p.getNome());
        campos[1].setText(String.valueOf(p.getQntEstoque()));
        campos[2].setText("0"); // Campo "Alerta Estoque Min."
        campos[3].setText(p.getCategoria());
        campos[4].setText(String.valueOf(p.getValorCompra()));
        campos[5].setText(String.valueOf(p.getValorVenda()));
    }

    /**
     * Pega os dados dos campos e cria um objeto Produto.
     */
    public Produto getProdutoFromFields() {
        Produto p = new Produto(
            campos[0].getText(), // Nome
            campos[3].getText(), // Categoria
            Double.parseDouble(campos[4].getText()), // Compra
            Double.parseDouble(campos[5].getText()), // Venda
            Integer.parseInt(campos[1].getText())  // Qnt
        );
        p.setProdutoId(this.produtoId); // Define o ID que guardamos
        return p;
    }
    
    public void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}