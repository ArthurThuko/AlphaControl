package alphacontrol.views.fluxo_caixa;

import alphacontrol.controllers.FluxoCaixaController;
import alphacontrol.models.MovimentacaoCaixa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalEntrada extends JDialog {

    private Point mouseClickPoint;
    private FluxoCaixaController controller = new FluxoCaixaController();
    private JTextField txtNome, txtData, txtValor;
    private MovimentacaoCaixa movimentacaoEditando; // 👈 novo campo

    private final Color begeFundo = new Color(242, 245, 233);
    private final Color verdeEscuro = new Color(48, 94, 64);
    private final Color verdeMedio = new Color(86, 130, 89);
    private final Color verdeClaro = new Color(142, 181, 145);
    private final Color begeClaro = new Color(253, 250, 240);

    public ModalEntrada(JFrame parent) {
        this(parent, null);
    }

    public ModalEntrada(JFrame parent, MovimentacaoCaixa mov) {
        super(parent, true);
        this.movimentacaoEditando = mov; // 👈 guarda referência
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
            txtValor.setText(String.valueOf(mov.getValor()));
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

        JLabel titulo = new JLabel("Entrada", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(verdeEscuro);
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        txtNome = criarCampo("Nome:", painel, gbc);
        txtData = criarCampo("Data:", painel, gbc);
        txtValor = criarCampo("Valor:", painel, gbc);

        JButton btnSalvar = criarBotao("Salvar", verdeMedio, e -> salvarEntrada());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton btnFechar = criarBotao("Fechar", verdeClaro, e -> dispose());
        painel.add(btnFechar, gbc);

        return painel;
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(verdeEscuro);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField campo = new JTextField();
        campo.setOpaque(true);
        campo.setBackground(new Color(245, 250, 245));
        campo.setForeground(verdeEscuro);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(verdeClaro, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        painel.add(campo, gbc);
        gbc.gridy++;
        return campo;
    }

    private JButton criarBotao(String texto, Color cor, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? verdeEscuro : cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(begeClaro);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
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
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}