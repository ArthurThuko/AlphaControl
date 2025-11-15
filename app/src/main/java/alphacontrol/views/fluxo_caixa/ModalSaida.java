package alphacontrol.views.fluxo_caixa;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.models.MovimentacaoCaixa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalSaida extends JDialog {

    private Point mouseClickPoint;
    private FluxoCaixaController controller = new FluxoCaixaController();
    private JTextField txtNome, txtData, txtValor;
    private MovimentacaoCaixa movimentacaoEditando;

    private final Color begeFundo = new Color(242, 245, 233);
    private final Color vermelhoEscuro = new Color(120, 40, 40);
    private final Color vermelhoMedio = new Color(180, 60, 60);
    private final Color vermelhoClaro = new Color(230, 100, 100);
    private final Color begeClaro = new Color(253, 250, 240);
    private final Color begeCampo = new Color(255, 250, 245);

    public ModalSaida(JFrame parent) {
        this(parent, null);
    }

    public ModalSaida(JFrame parent, MovimentacaoCaixa mov) {
        super(parent, true);
        this.movimentacaoEditando = mov;
        setTitle(mov == null ? "Adicionar Saída" : "Editar Saída");

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
            txtValor.setText(String.format("%.2f", mov.getValor()).replace(".", ","));
        }
    }

    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(begeFundo);
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

        JLabel titulo = new JLabel(movimentacaoEditando == null ? "Adicionar Saída" : "Editar Saída", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(vermelhoEscuro);
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        txtNome = criarCampo("Nome:", painel, gbc, begeCampo, vermelhoClaro, vermelhoEscuro);
        txtData = criarCampo("Data:", painel, gbc, begeCampo, vermelhoClaro, vermelhoEscuro);
        txtValor = criarCampo("Valor (R$):", painel, gbc, begeCampo, vermelhoClaro, vermelhoEscuro);

        JButton btnSalvar = criarBotao("Salvar", vermelhoMedio, vermelhoEscuro, begeClaro);
        btnSalvar.addActionListener(e -> salvarSaida());
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton btnFechar = criarBotao("Fechar", vermelhoClaro, vermelhoMedio, vermelhoEscuro);
        btnFechar.addActionListener(e -> dispose());
        btnFechar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painel.add(btnFechar, gbc);

        return painel;
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc, Color fundo, Color borda, Color texto) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
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
        campo.setCaretColor(texto);

        painel.add(campo, gbc);
        gbc.gridy++;
        return campo;
    }

    private JButton criarBotao(String texto, Color cor, Color corPressed, Color corTexto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? corPressed : cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void salvarSaida() {
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
                controller.adicionarSaida(nome, valor, data);
                JOptionPane.showMessageDialog(this, "Saída adicionada com sucesso!");
            } else {
                controller.atualizarMovimentacao(
                        movimentacaoEditando.getId(),
                        "saida",
                        nome,
                        valor,
                        data);
                JOptionPane.showMessageDialog(this, "Saída atualizada com sucesso!");
            }

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Use 00,00.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}