package alphacontrol.views;

import javax.swing.*;

import alphacontrol.controllers.LoginController;
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.CampoSenhaEstilizado;
import alphacontrol.views.components.CampoTextoEstilizado;
import alphacontrol.views.components.Estilos;
import alphacontrol.views.components.PainelGradiente;

import java.awt.*;

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

        PainelGradiente painelPrincipal = new PainelGradiente();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));

        // ---------- LOGO ----------
        ImageIcon iconeOriginal = new ImageIcon(getClass().getResource("/alphacontrol/img/logo_alpha_control.png"));
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
        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new GridBagLayout());
        painelCampos.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(Estilos.FONTE_LABEL);
        lblUsuario.setForeground(Estilos.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCampos.add(lblUsuario, gbc);

        txtUsuario = new CampoTextoEstilizado(15);
        gbc.gridx = 1;
        painelCampos.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(Estilos.FONTE_LABEL);
        lblSenha.setForeground(Estilos.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCampos.add(lblSenha, gbc);

        txtSenha = new CampoSenhaEstilizado(15);
        txtSenha.setFont(Estilos.FONTE_PADRAO);
        gbc.gridx = 1;
        painelCampos.add(txtSenha, gbc);

        // ---------- CHECKBOX MOSTRAR SENHA ----------
        JCheckBox cbMostrarSenha = new JCheckBox("Mostrar senha");
        cbMostrarSenha.setOpaque(false);
        cbMostrarSenha.setForeground(new Color(70, 50, 30));
        cbMostrarSenha.setFont(new Font("Georgia", Font.PLAIN, 12));
        cbMostrarSenha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cbMostrarSenha.addActionListener(e -> {
            if (cbMostrarSenha.isSelected()) {
                txtSenha.setEchoChar((char) 0);
            } else {
                txtSenha.setEchoChar('•');
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        painelCampos.add(cbMostrarSenha, gbc);

        painelPrincipal.add(painelCampos);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // ---------- BOTÕES ----------
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);

        btnLimpar = new BotaoEstilizado("Limpar", new Color(220, 190, 160));
        btnLogin = new BotaoEstilizado("Entrar", new Color(195, 160, 130));

        painelBotoes.add(btnLogin);
        painelBotoes.add(btnLimpar);
        painelPrincipal.add(painelBotoes);

        // ---------- AÇÕES ----------
        btnLogin.addActionListener(e -> {
            controller.fazerLogin(txtUsuario.getText(), new String(txtSenha.getPassword()), this);
        });

        btnLimpar.addActionListener(e -> {
            txtUsuario.setText("");
            txtSenha.setText("");
            cbMostrarSenha.setSelected(false);
            txtSenha.setEchoChar('•');
        });

        add(painelPrincipal);

        setVisible(true);
    }
}