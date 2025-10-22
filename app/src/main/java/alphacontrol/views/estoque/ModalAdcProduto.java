package alphacontrol.views.estoque;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalAdcProduto extends JDialog {

    private Point mouseClickPoint;

    public ModalAdcProduto(JFrame parent) {
        super(parent, "Adicionar Produto", true);

        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);

        // Painel principal com cantos arredondados
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

        // Título centralizado
        JLabel titulo = new JLabel("Adicionar Produto", SwingConstants.CENTER);
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
        JTextField[] campos = new JTextField[labels.length];

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

        // Botão Adicionar
        JButton botaoAdicionar = new JButton("Adicionar") {
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
        botaoAdicionar.setForeground(begeClaro);
        botaoAdicionar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botaoAdicionar.setFocusPainted(false);
        botaoAdicionar.setContentAreaFilled(false);
        botaoAdicionar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(botaoAdicionar, gbc);
        gbc.gridy++;

        // Botão Fechar estilizado
        JButton botaoFechar = new JButton("Fechar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? marromClaro.darker() : marromClaro);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(marromEscuro);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
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
        setSize(500, 600);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            ModalAdcProduto modal = new ModalAdcProduto(frame);
            modal.setVisible(true);
        });
    }
}
