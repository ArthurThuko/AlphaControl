package alphacontrol.views.fluxo_caixa;

import alphacontrol.controllers.FluxoCaixaController;
import alphacontrol.models.MovimentacaoCaixa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModalSaida extends JDialog {

    private Point mouseClickPoint;
    private FluxoCaixaController controller = new FluxoCaixaController();
    private JTextField txtNome, txtData, txtValor;

    // Cores
    private final Color begeFundo = new Color(246, 232, 232);
    private final Color vermelhoEscuro = new Color(138, 41, 41);
    private final Color vermelhoMedio = new Color(197, 80, 80);
    private final Color vermelhoClaro = new Color(214, 160, 160);
    private final Color begeClaro = new Color(253, 250, 240);

    // ===== Construtor para adicionar =====
    public ModalSaida(JFrame parent) {
        this(parent, null);
    }

    // ===== Construtor para editar =====
    public ModalSaida(JFrame parent, MovimentacaoCaixa mov) {
        super(parent, true);
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

        JLabel titulo = new JLabel("Saída", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(vermelhoEscuro);
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        txtNome = criarCampo("Nome:", painel, gbc);
        txtData = criarCampo("Data:", painel, gbc);
        txtValor = criarCampo("Valor:", painel, gbc);

        JButton btn = criarBotao("Salvar", vermelhoMedio, e -> salvarSaida());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btn, gbc);
        gbc.gridy++;

        JButton btnFechar = criarBotao("Fechar", vermelhoClaro, e -> dispose());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnFechar, gbc);

        return painel;
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(vermelhoEscuro);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField campo = new JTextField();
        campo.setOpaque(false);
        campo.setForeground(vermelhoEscuro);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        painel.add(campo, gbc);
        gbc.gridy++;
        return campo;
    }

    private JButton criarBotao(String texto, Color cor, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setForeground(begeClaro);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addActionListener(action);

        btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? vermelhoEscuro : cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(begeClaro);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.addActionListener(action); // <-- adiciona o ActionListener aqui também
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
            controller.adicionarSaida(nome, valor, data);
            JOptionPane.showMessageDialog(this, "Saída salva com sucesso!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}