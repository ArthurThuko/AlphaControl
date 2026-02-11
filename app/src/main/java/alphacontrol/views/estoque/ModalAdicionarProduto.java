package alphacontrol.views.estoque;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import alphacontrol.models.Produto;

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
                g2.dispose();
            }
        };
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(0, 0, 0, 0));

        // Arrastar Modal
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

        // Campos Normais
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

        // Campos com Máscara de Valor
        gbc.gridx = 0;
        painel.add(criarLabel("Preço de Custo (R$):", marromEscuro), gbc);
        gbc.gridx = 1;
        txtCompra = criarCampo(begeClaro, marromClaro, marromEscuro);
        aplicarMascaraDecimal(txtCompra);
        painel.add(txtCompra, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("Valor Venda (R$):", marromEscuro), gbc);
        gbc.gridx = 1;
        txtVenda = criarCampo(begeClaro, marromClaro, marromEscuro);
        aplicarMascaraDecimal(txtVenda);
        painel.add(txtVenda, gbc);
        gbc.gridy++;

        // Campos Numéricos Inteiros
        gbc.gridx = 0;
        painel.add(criarLabel("Quantidade:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtQnt = criarCampo(begeClaro, marromClaro, marromEscuro);
        txtQnt.setText("0");
        painel.add(txtQnt, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("Alerta Estoque Min.:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtQntMinima = criarCampo(begeClaro, marromClaro, marromEscuro);
        txtQntMinima.setText("0");
        painel.add(txtQntMinima, gbc);
        gbc.gridy++;

        // Botões
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
        setSize(500, 680);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    /**
     * Aplica um filtro que aceita apenas números e um único ponto decimal,
     * limitando a duas casas após o ponto.
     */
    private void aplicarMascaraDecimal(JTextField campo) {
        campo.setText("0.00");
        
        // Seleciona tudo ao ganhar foco para facilitar edição
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(campo::selectAll);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText("0.00");
                } else {
                    try {
                        double valor = Double.parseDouble(campo.getText());
                        campo.setText(String.format("%.2f", valor).replace(",", "."));
                    } catch (NumberFormatException ex) {
                        campo.setText("0.00");
                    }
                }
            }
        });

        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String futureText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                
                // Regex: Apenas números e no máximo um ponto, com até duas casas decimais
                if (futureText.matches("\\d*(\\.\\d{0,2})?")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
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
            Double.parseDouble(txtCompra.getText().isEmpty() ? "0" : txtCompra.getText()),
            Double.parseDouble(txtVenda.getText().isEmpty() ? "0" : txtVenda.getText()),
            Integer.parseInt(txtQnt.getText().isEmpty() ? "0" : txtQnt.getText()),
            Integer.parseInt(txtQntMinima.getText().isEmpty() ? "0" : txtQntMinima.getText())
        );
    }

    public void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}