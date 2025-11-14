package alphacontrol.views.login;

import javax.swing.*;

import alphacontrol.controllers.login.LoginController;
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
    private JCheckBox cbMostrarSenha;
    private char defaultEcho;

    public TelaLogin() {
        
        setTitle("Login - AlphaControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        PainelGradiente painelPrincipal = new PainelGradiente();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));

        
        ImageIcon iconeOriginal = new ImageIcon(getClass().getResource("/alphacontrol/img/logo_alpha_control.png"));
        Image img = iconeOriginal.getImage();
        Image imgRedimensionada = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon icone = new ImageIcon(imgRedimensionada);
        JLabel lblLogo = new JLabel(icone);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(lblLogo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 8)));

        
        JLabel lblTitulo = new JLabel("Acesso ao AlphaControl", SwingConstants.CENTER);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 70, 50));
        painelPrincipal.add(lblTitulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        
        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new GridBagLayout());
        painelCampos.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; 

        Font fonteMaior = new Font("Georgia", Font.PLAIN, 18); 

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(fonteMaior);
        lblUsuario.setForeground(Estilos.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCampos.add(lblUsuario, gbc);

        txtUsuario = new CampoTextoEstilizado(15);
        txtUsuario.setFont(fonteMaior);
        txtUsuario.setPreferredSize(new Dimension(300, 40)); 
        gbc.gridx = 1;
        painelCampos.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(fonteMaior);
        lblSenha.setForeground(Estilos.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCampos.add(lblSenha, gbc);

        txtSenha = new CampoSenhaEstilizado(15);
        txtSenha.setFont(fonteMaior);
        txtSenha.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 1;
        painelCampos.add(txtSenha, gbc);

        
        cbMostrarSenha = new JCheckBox("Mostrar senha");
        cbMostrarSenha.setOpaque(false);
        cbMostrarSenha.setForeground(new Color(70, 50, 30));
        cbMostrarSenha.setFont(new Font("Georgia", Font.PLAIN, 16));
        cbMostrarSenha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        defaultEcho = txtSenha.getEchoChar();
        txtSenha.setEchoChar(defaultEcho);
        
        cbMostrarSenha.addActionListener(e -> {
            if (cbMostrarSenha.isSelected()) {
                txtSenha.setEchoChar((char) 0);
            } else {
                txtSenha.setEchoChar(defaultEcho);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        painelCampos.add(cbMostrarSenha, gbc);

        painelPrincipal.add(painelCampos);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setOpaque(false);

        btnLimpar = new BotaoEstilizado("Limpar", new Color(220, 190, 160));
        btnLogin = new BotaoEstilizado("Entrar", new Color(195, 160, 130));

        Font fonteBotao = new Font("Georgia", Font.BOLD, 16);
        btnLogin.setFont(fonteBotao);
        btnLimpar.setFont(fonteBotao);

        Dimension tamanhoBotao = new Dimension(140, 45);
        btnLogin.setPreferredSize(tamanhoBotao);
        btnLimpar.setPreferredSize(tamanhoBotao);

        painelBotoes.add(btnLogin);
        painelBotoes.add(btnLimpar);
        painelPrincipal.add(painelBotoes);

        
        btnLimpar.addActionListener(e -> {
            txtUsuario.setText("");
            txtSenha.setText("");
            cbMostrarSenha.setSelected(false);
            txtSenha.setEchoChar(defaultEcho);
        });

        
        controller = new LoginController(this); 

        add(painelPrincipal);
        setVisible(true);
    }
    
    
    public JButton getBtnLogin() {
        return btnLogin;
    }

    public String getTxtUsuario() {
        return txtUsuario.getText();
    }
    
    public String getTxtSenha() {
        return new String(txtSenha.getPassword());
    }
}