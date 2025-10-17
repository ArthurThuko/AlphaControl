package alphacontrol.view;

import javax.swing.*;

import alphacontrol.controller.LoginController;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnLimpar;
    private LoginController controller;

    public TelaLogin() {
        controller = new LoginController();

        setTitle("Login - AlphaControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel principal com gradiente
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradiente = new GradientPaint(
                        0, 0, new Color(255, 250, 245),
                        0, getHeight(), new Color(240, 225, 200)
                );
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));

        // ---------- LOGO ----------
        ImageIcon iconeOriginal = new ImageIcon("img/logo_alpha_control_melhorada.png");
        Image img = iconeOriginal.getImage();
        Image imgRedimensionada = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon icone = new ImageIcon(imgRedimensionada);
        JLabel lblLogo = new JLabel(icone);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(lblLogo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 8)));

        // ---------- TÍTULO ----------
        JLabel lblTitulo = new JLabel("Acesso ao AlphaControl", SwingConstants.CENTER);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 70, 50));
        painelPrincipal.add(lblTitulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        // ---------- CAMPOS ----------
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Georgia", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(90, 70, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCampos.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        estilizarCampo(txtUsuario);
        gbc.gridx = 1;
        painelCampos.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Georgia", Font.BOLD, 14));
        lblSenha.setForeground(new Color(90, 70, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCampos.add(lblSenha, gbc);

        txtSenha = new JPasswordField(15);
        estilizarCampo(txtSenha);
        gbc.gridx = 1;
        painelCampos.add(txtSenha, gbc);

        // ---------- CHECKBOX MOSTRAR SENHA ----------
        JCheckBox cbMostrarSenha = new JCheckBox("Mostrar senha");
        cbMostrarSenha.setOpaque(false);
        cbMostrarSenha.setForeground(new Color(70, 50, 30));
        cbMostrarSenha.setFont(new Font("Georgia", Font.PLAIN, 12));
        cbMostrarSenha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover no checkbox
        cbMostrarSenha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cbMostrarSenha.setForeground(new Color(90, 70, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cbMostrarSenha.setForeground(new Color(70, 50, 30));
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        painelCampos.add(cbMostrarSenha, gbc);

        cbMostrarSenha.addActionListener(e -> {
            if (cbMostrarSenha.isSelected()) {
                txtSenha.setEchoChar((char) 0);
            } else {
                txtSenha.setEchoChar('•');
            }
        });

        painelPrincipal.add(painelCampos);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // ---------- BOTÕES ----------
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);

        btnLogin = criarBotao("Entrar", new Color(195, 160, 130));
        btnLimpar = criarBotao("Limpar", new Color(220, 190, 160));

        painelBotoes.add(btnLogin);
        painelBotoes.add(btnLimpar);
        painelPrincipal.add(painelBotoes);

        add(painelPrincipal);

        // ---------- AÇÕES ----------
        btnLogin.addActionListener(e -> {
            controller.login(txtUsuario.getText(), new String(txtSenha.getPassword()), this);
        });

        btnLimpar.addActionListener(e -> {
            txtUsuario.setText("");
            txtSenha.setText("");
            cbMostrarSenha.setSelected(false);
            txtSenha.setEchoChar('•');
        });

        setVisible(true);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Georgia", Font.PLAIN, 13));
        campo.setBackground(new Color(255, 250, 245));
        campo.setForeground(new Color(70, 50, 30));
        campo.setCaretColor(new Color(120, 90, 60));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 200, 170), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 140, 100), 2, true),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 200, 170), 1, true),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            }
        });
    }

    private JButton criarBotao(String texto, Color corBase) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Georgia", Font.BOLD, 13));
        botao.setForeground(new Color(70, 50, 30));
        botao.setBackground(corBase);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(clarearCor(corBase, 0.15f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(corBase);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                botao.setBackground(corBase.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                botao.setBackground(clarearCor(corBase, 0.15f));
            }
        });

        return botao;
    }

    private Color clarearCor(Color cor, float fator) {
        int r = cor.getRed();
        int g = cor.getGreen();
        int b = cor.getBlue();

        r += (int) ((255 - r) * fator);
        g += (int) ((255 - g) * fator);
        b += (int) ((255 - b) * fator);

        return new Color(r, g, b);
    }
}