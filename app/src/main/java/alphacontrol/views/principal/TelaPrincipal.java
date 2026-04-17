package alphacontrol.views.principal;

import java.awt.*;
import javax.swing.*;

import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.Estilos;
import alphacontrol.views.components.PainelGradiente;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(TelaPrincipalController controller) {
        setTitle("Tela Principal - AlphaControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);

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

        JButton btnEstoque = new BotaoEstilizado("Estoque", COR_BOTAO_FUNDO);
        JButton btnPdv = new BotaoEstilizado("PDV", COR_BOTAO_FUNDO);
        JButton btnFiados = new BotaoEstilizado("Fiados/Clientes", COR_BOTAO_FUNDO);
        JButton btnRelatorios = new BotaoEstilizado("Relatórios", COR_BOTAO_FUNDO);
        JButton btnFluxoCaixa = new BotaoEstilizado("Fluxo de Caixa", COR_BOTAO_FUNDO);
        JButton btnSair = new BotaoEstilizado("Sair", COR_BOTAO_SAIR_FUNDO);

        configurarBotaoComIcone(btnEstoque, "/alphacontrol/img/icons/Icon_Estoque.png");
        configurarBotaoComIcone(btnPdv, "/alphacontrol/img/icons/Icon_PDV.png");
        configurarBotaoComIcone(btnFiados, "/alphacontrol/img/icons/Icon_Fiado.png");
        configurarBotaoComIcone(btnRelatorios, "/alphacontrol/img/icons/Icon_Relatorio.png");
        configurarBotaoComIcone(btnFluxoCaixa, "/alphacontrol/img/icons/Icon_FluxoCaixa.png");
        configurarBotaoComIcone(btnSair, "/alphacontrol/img/icons/Icon_Sair.png");

        if (controller != null) {
            btnEstoque.addActionListener(e -> controller.abrirTelaEstoque());
            btnPdv.addActionListener(e -> controller.abrirTelaPDV());
            btnFiados.addActionListener(e -> controller.abrirTelaFiado());
            btnRelatorios.addActionListener(e -> controller.abrirTelaRelatorios());
            btnFluxoCaixa.addActionListener(e -> controller.abrirTelaFluxoCaixa());
            btnSair.addActionListener(e -> controller.logout());
        }

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 40, 40));
        painelBotoes.setOpaque(false);
        painelBotoes.setPreferredSize(new Dimension(900, 700));

        painelBotoes.add(btnEstoque);
        painelBotoes.add(btnPdv);
        painelBotoes.add(btnFiados);
        painelBotoes.add(btnFluxoCaixa);
        painelBotoes.add(btnRelatorios);
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
            Image imgRedimensionada = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(imgRedimensionada));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + iconPath);
        }

        botao.setFont(Estilos.FONTE_LABEL.deriveFont(Font.BOLD, 25));
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(15);
    }
}