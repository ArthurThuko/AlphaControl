package alphacontrol.views;

import javax.swing.*;
import java.awt.*;
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.PainelGradiente;
import alphacontrol.views.components.Estilos;

public class TelaPrincipal extends JFrame {

    private JButton btnClientes;
    private JButton btnVendas;
    private JButton btnProdutos;
    private JButton btnRelatorios;
    private JButton btnConfiguracoes;
    private JButton btnSair;

    public TelaPrincipal() {
        setTitle("AlphaControl - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        PainelGradiente painelPrincipal = new PainelGradiente();
        painelPrincipal.setLayout(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JLabel lblTitulo = new JLabel("Seja Bem-vindo ao AlphaControl", SwingConstants.CENTER);
        lblTitulo.setFont(Estilos.FONTE_TITULO.deriveFont(Font.BOLD, 30));
        lblTitulo.setForeground(Estilos.COR_TEXTO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        painelPrincipal.add(lblTitulo, gbc);

        btnClientes = new BotaoEstilizado("Estoque", new Color(70, 130, 180)); // azul
        btnVendas = new BotaoEstilizado("PDV", new Color(46, 139, 87)); // verde
        btnProdutos = new BotaoEstilizado("Fiados", new Color(218, 165, 32)); // dourado
        btnRelatorios = new BotaoEstilizado("Relatórios", new Color(205, 92, 92)); // vermelho
        btnConfiguracoes = new BotaoEstilizado("Fluxo de Caixa", new Color(123, 104, 238)); // roxo

        for (JButton botao : new JButton[] { btnClientes, btnVendas, btnProdutos, btnRelatorios, btnConfiguracoes }) {
            botao.setFont(Estilos.FONTE_LABEL.deriveFont(Font.BOLD, 18));
        }

        gbc.gridwidth = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(btnClientes, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelPrincipal.add(btnVendas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelPrincipal.add(btnProdutos, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        painelPrincipal.add(btnRelatorios, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        painelPrincipal.add(btnConfiguracoes, gbc);

        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        painelInferior.setOpaque(false);

        btnSair = new BotaoEstilizado("Sair", new Color(178, 34, 34)); // vermelho escuro
        btnSair.setPreferredSize(new Dimension(120, 40));
        btnSair.setFont(Estilos.FONTE_PADRAO.deriveFont(Font.BOLD, 14));

        painelInferior.add(btnSair);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        painelPrincipal.add(painelInferior, gbc);

        // ---------- AÇÃO DO BOTÃO SAIR ----------
        btnSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new TelaLogin();
            }
        });

        add(painelPrincipal);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaPrincipal::new);
    }
}