package alphacontrol.views.fluxo_caixa;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.models.MovimentacaoCaixa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalEntrada extends JDialog {

    private Point mouseClickPoint;
    private FluxoCaixaController controller;
    private JTextField txtNome, txtData, txtValor;
    private MovimentacaoCaixa movimentacaoEditando;

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);

    public ModalEntrada(JFrame parent, FluxoCaixaController controller) {
        this(parent, null, controller);
    }

    public ModalEntrada(JFrame parent, MovimentacaoCaixa mov, FluxoCaixaController controller) {
        super(parent, true);
        this.movimentacaoEditando = mov;
        this.controller = controller;
        setTitle(mov == null ? "Adicionar Entrada" : "Editar Entrada");

        JPanel painel = criarPainelPrincipal();
        add(painel);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        if (mov != null) {
            txtNome.setText(mov.getNome());
            txtData.setText(mov.getData());
            txtValor.setText(String.format("%.2f", mov.getValor()).replace(",", "."));
        }
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel(movimentacaoEditando == null ? "Adicionar Entrada" : "Editar Entrada",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(MARROM_ESCURO);
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        txtNome = criarCampo("Nome:", painel, gbc);
        txtData = criarCampo("Data (dd/mm/aaaa):", painel, gbc);
        txtValor = criarCampo("Valor (R$):", painel, gbc);

        JButton btnSalvar = criarBotao("Salvar", VERDE_OLIVA, Color.WHITE, e -> salvarEntrada());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton btnFechar = criarBotao("Fechar", MARROM_MEDIO, Color.WHITE, e -> dispose());
        painel.add(btnFechar, gbc);

        return painel;
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc) {
        return criarCampo(label, painel, gbc, BEGE_CLARO, MARROM_CLARO, MARROM_ESCURO);
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc, Color fundo, Color borda,
            Color texto) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label, SwingConstants.RIGHT);
        lbl.setForeground(MARROM_ESCURO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_CLARO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.setColor(MARROM_CLARO);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setForeground(MARROM_ESCURO);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        painel.add(campo, gbc);
        gbc.gridy++;
        return campo;
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? corFundo.brighter() : corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private void salvarEntrada() {
        String nome = txtNome.getText().trim();
        String data = txtData.getText().trim();
        String valorStr = txtValor.getText().trim();

        if (nome.isEmpty() || data.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr.replace(",", "."));

            if (movimentacaoEditando == null) {
                controller.adicionarEntrada(nome, valor, data);
                JOptionPane.showMessageDialog(this, "Entrada adicionada com sucesso!");
            } else {
                controller.atualizarMovimentacao(
                        movimentacaoEditando.getId(),
                        "entrada",
                        nome,
                        valor,
                        data);
                JOptionPane.showMessageDialog(this, "Entrada atualizada com sucesso!");
            }

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Use ponto para decimais (ex: 50.25).", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}