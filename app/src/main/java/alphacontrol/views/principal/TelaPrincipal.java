package alphacontrol.views.principal;

import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.Estilos;
import alphacontrol.views.components.PainelGradiente;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame {

    private JButton btnEstoque;
    private JButton btnPdv;
    private JButton btnFiados;
    private JButton btnRelatorios;
    private JButton btnFluxoCaixa;
    private JButton btnSair;

    public TelaPrincipal(TelaPrincipalController controller) {
        setTitle("Tela Principal - AlphaControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        Color COR_BOTAO_FUNDO = new Color(205, 170, 125);
        Color COR_BOTAO_SAIR_FUNDO = new Color(170, 125, 95);
        Color COR_TEXTO_TITULO = new Color(85, 60, 40);

        PainelGradiente painelPrincipal = new PainelGradiente();
        painelPrincipal.setLayout(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lblTitulo = new JLabel("Seja Bem-vindo ao AlphaControl", SwingConstants.CENTER);
        lblTitulo.setFont(Estilos.FONTE_TITULO.deriveFont(Font.BOLD, 36));
        lblTitulo.setForeground(COR_TEXTO_TITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        painelPrincipal.add(lblTitulo, gbc);

        btnEstoque = new BotaoEstilizado("Estoque", COR_BOTAO_FUNDO);
        btnFiados = new BotaoEstilizado("Fiados", COR_BOTAO_FUNDO);
        btnSair = new BotaoEstilizado("Sair", COR_BOTAO_SAIR_FUNDO);

        configurarBotaoComIcone(btnEstoque, "/alphacontrol/img/icons/Icon_Estoque.png");
        configurarBotaoComIcone(btnFiados, "/alphacontrol/img/icons/Icon_Fiado.png");
        configurarBotaoComIcone(btnSair, "/alphacontrol/img/icons/Icon_Sair.png");

        if (controller != null) {
            btnEstoque.addActionListener(e -> controller.abrirTelaEstoque());
            btnFiados.addActionListener(e -> controller.abrirTelaFiado());
            btnSair.addActionListener(e -> controller.logout());
        }

        JPanel painelBotoes = new JPanel(new GridLayout(2, 2, 40, 40));
        painelBotoes.setOpaque(false);
        painelBotoes.setPreferredSize(new Dimension(1200, 700));

        painelBotoes.add(btnEstoque);
        painelBotoes.add(btnFiados);
        painelBotoes.add(btnSair);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal);
        setLocationRelativeTo(null);
    }

    private void configurarBotaoComIcone(JButton botao, String iconPath) {
        try {
            ImageIcon iconeOriginal = new ImageIcon(getClass().getResource(iconPath));
            Image img = iconeOriginal.getImage();
            Image imgRedimensionada = img.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            ImageIcon icone = new ImageIcon(imgRedimensionada);

            botao.setIcon(icone);

        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + iconPath);
        }

        botao.setFont(Estilos.FONTE_LABEL.deriveFont(Font.BOLD, 25));
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(15);
    }

    public void setController(TelaPrincipalController controller) {
        for (ActionListener al : btnEstoque.getActionListeners())
            btnEstoque.removeActionListener(al);
        for (ActionListener al : btnPdv.getActionListeners())
            btnPdv.removeActionListener(al);
        for (ActionListener al : btnFiados.getActionListeners())
            btnFiados.removeActionListener(al);
        for (ActionListener al : btnRelatorios.getActionListeners())
            btnRelatorios.removeActionListener(al);
        for (ActionListener al : btnFluxoCaixa.getActionListeners())
            btnFluxoCaixa.removeActionListener(al);
        for (ActionListener al : btnSair.getActionListeners())
            btnSair.removeActionListener(al);

        btnEstoque.addActionListener(e -> controller.abrirTelaEstoque());
        btnFiados.addActionListener(e -> controller.abrirTelaFiado());
        btnSair.addActionListener(e -> controller.logout());
    }
}