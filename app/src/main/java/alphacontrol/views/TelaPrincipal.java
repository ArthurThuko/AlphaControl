package alphacontrol.views;

//CONTROLLERS
import alphacontrol.controllers.TelaPrincipalController;

//COMPONENTES
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.Estilos;
import alphacontrol.views.components.PainelGradiente;

import java.awt.*;
import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private JButton btnEstoque;
    private JButton btnPdv;
    private JButton btnFiados;
    private JButton btnRelatorios;
    private JButton btnFluxoCaixa;
    private JButton btnSair;

    public TelaPrincipal() {
        TelaPrincipalController controller = new TelaPrincipalController(this);

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

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Seja Bem-vindo ao AlphaControl", SwingConstants.CENTER);
        lblTitulo.setFont(Estilos.FONTE_TITULO.deriveFont(Font.BOLD, 32));
        lblTitulo.setForeground(COR_TEXTO_TITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        painelPrincipal.add(lblTitulo, gbc);

        // --- BOTÕES PRINCIPAIS ---
        btnEstoque = new BotaoEstilizado("Estoque", COR_BOTAO_FUNDO);
        btnPdv = new BotaoEstilizado("PDV", COR_BOTAO_FUNDO);
        btnFiados = new BotaoEstilizado("Fiados", COR_BOTAO_FUNDO);
        btnRelatorios = new BotaoEstilizado("Relatórios", COR_BOTAO_FUNDO);
        btnFluxoCaixa = new BotaoEstilizado("Fluxo de Caixa", COR_BOTAO_FUNDO);

        // ---------- AÇÕES DOS BOTÕES PRINCIPAIS ----------
        btnEstoque.addActionListener(e -> controller.abrirTelaEstoque());
        btnPdv.addActionListener(e -> controller.abrirTelaPDV());
        btnFiados.addActionListener(e -> controller.abrirTelaFiados());
        btnRelatorios.addActionListener(e -> controller.abrirTelaRelatorios());
        btnFluxoCaixa.addActionListener(e -> controller.abrirTelaFluxoCaixa());

        for (JButton botao : new JButton[] { btnEstoque, btnPdv, btnFiados, btnRelatorios, btnFluxoCaixa }) {
            botao.setFont(Estilos.FONTE_LABEL.deriveFont(Font.BOLD, 22)); // Aumentei um pouco a fonte também
        }

        JPanel painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setOpaque(false);
        painelBotoes.setPreferredSize(new Dimension(800, 450));

        GridBagConstraints gbcBotoes = new GridBagConstraints();
        gbcBotoes.fill = GridBagConstraints.BOTH;
        gbcBotoes.weightx = 1;
        gbcBotoes.weighty = 1;
        gbcBotoes.insets = new Insets(20, 20, 20, 20); 
        gbcBotoes.gridx = 0;
        gbcBotoes.gridy = 0;
        painelBotoes.add(btnEstoque, gbcBotoes);

        gbcBotoes.gridx = 1;
        gbcBotoes.gridy = 0;
        painelBotoes.add(btnPdv, gbcBotoes);

        gbcBotoes.gridx = 0;
        gbcBotoes.gridy = 1;
        painelBotoes.add(btnFiados, gbcBotoes);

        gbcBotoes.gridx = 1;
        gbcBotoes.gridy = 1;
        painelBotoes.add(btnRelatorios, gbcBotoes);

        gbcBotoes.gridx = 0;
        gbcBotoes.gridy = 2;
        gbcBotoes.gridwidth = 2;
        painelBotoes.add(btnFluxoCaixa, gbcBotoes);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(painelBotoes, gbc);

        // --- PAINEL INFERIOR COM BOTÃO SAIR ---
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelInferior.setOpaque(false);

        btnSair = new BotaoEstilizado("Sair", COR_BOTAO_SAIR_FUNDO);
        btnSair.setPreferredSize(new Dimension(130, 45));
        btnSair.setFont(Estilos.FONTE_PADRAO.deriveFont(Font.BOLD, 14));
        btnSair.addActionListener(e -> controller.logout());
        painelInferior.add(btnSair);

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        painelPrincipal.add(painelInferior, gbc);

        add(painelPrincipal);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(TelaPrincipal::new);
    }
}