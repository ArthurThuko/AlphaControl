package alphacontrol.views;

import javax.swing.*;
import java.awt.*;
import alphacontrol.views.components.BotaoEstilizado;
import alphacontrol.views.components.PainelGradiente;

public class TelaPrincipal extends JFrame {

    private JButton btnClientes;
    private JButton btnVendas;
    private JButton btnProdutos;
    private JButton btnRelatorios;
    private JButton btnConfiguracoes;

    public TelaPrincipal() {
        setTitle("AlphaControl - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ---------- PAINEL PRINCIPAL ----------
        PainelGradiente painelPrincipal = new PainelGradiente();
        painelPrincipal.setLayout(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // ---------- BOTÕES ----------
        btnClientes = new BotaoEstilizado("Clientes", new Color(70, 130, 180)); // azul
        btnVendas = new BotaoEstilizado("Vendas", new Color(46, 139, 87)); // verde
        btnProdutos = new BotaoEstilizado("Produtos", new Color(218, 165, 32)); // dourado
        btnRelatorios = new BotaoEstilizado("Relatórios", new Color(205, 92, 92)); // vermelho
        btnConfiguracoes = new BotaoEstilizado("Configurações", new Color(123, 104, 238)); // roxo

        // ---------- ADICIONANDO AO PAINEL ----------
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPrincipal.add(btnClientes, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painelPrincipal.add(btnVendas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(btnProdutos, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelPrincipal.add(btnRelatorios, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // ocupa as duas colunas
        painelPrincipal.add(btnConfiguracoes, gbc);

        add(painelPrincipal);
        setVisible(true);
    }
}