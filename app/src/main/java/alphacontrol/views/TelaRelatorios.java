package alphacontrol.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TelaRelatorios extends JFrame {

    public TelaRelatorios() {
        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setLocationRelativeTo(null);

        // === PALETA DE CORES ===
        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);
        Color verdeBotao = new Color(72, 166, 90);
        Color azulBotao = new Color(70, 130, 180);

        // === PAINEL PRINCIPAL ===
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(begeFundo);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(begeFundo);
        painelPrincipal.setBorder(new EmptyBorder(60, 80, 60, 80));

        // === TÍTULO ===
        JLabel titulo = new JLabel("Relatórios", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 42));
        titulo.setForeground(marromEscuro);
        titulo.setBorder(new EmptyBorder(0, 0, 40, 0));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // === PAINEL DE CONTEÚDO ===
        JPanel painelConteudo = new JPanel(new GridBagLayout());
        painelConteudo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Tipo de relatório
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTipo = new JLabel("Tipo de Relatório:");
        lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblTipo.setForeground(marromEscuro);
        painelConteudo.add(lblTipo, gbc);

        JRadioButton rbVendas = new JRadioButton("Vendas");
        JRadioButton rbProdutos = new JRadioButton("Produtos + vendidos");
        JRadioButton rbFluxo = new JRadioButton("Fluxo de Caixa");
        JRadioButton rbFiados = new JRadioButton("Fiados");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbVendas);
        grupo.add(rbProdutos);
        grupo.add(rbFluxo);
        grupo.add(rbFiados);

        JRadioButton[] radios = { rbVendas, rbProdutos, rbFluxo, rbFiados };
        for (JRadioButton rb : radios) {
            rb.setOpaque(false);
            rb.setForeground(marromEscuro);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 17));
        }

        // painel com os rádios bem alinhados
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 0));
        painelRadios.setOpaque(false);
        painelRadios.add(rbVendas);
        painelRadios.add(rbProdutos);
        painelRadios.add(rbFluxo);
        painelRadios.add(rbFiados);
        painelConteudo.add(painelRadios, gbc);
        gbc.gridwidth = 1;

        // === CAMPOS DE DATA E BOTÕES NA MESMA LINHA ===
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblInicio = new JLabel("Data Início:");
        lblInicio.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblInicio.setForeground(marromEscuro);
        painelConteudo.add(lblInicio, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        JTextField campoInicio = criarCampo(begeClaro, marromClaro, marromEscuro);
        campoInicio.setPreferredSize(new Dimension(220, 35));
        campoInicio.setText("dd/mm/aaaa");
        painelConteudo.add(campoInicio, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblFinal = new JLabel("Data Final:");
        lblFinal.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblFinal.setForeground(marromEscuro);
        painelConteudo.add(lblFinal, gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        JTextField campoFinal = criarCampo(begeClaro, marromClaro, marromEscuro);
        campoFinal.setPreferredSize(new Dimension(220, 35));
        campoFinal.setText("dd/mm/aaaa");
        painelConteudo.add(campoFinal, gbc);

        // Botões alinhados na mesma linha
        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelBotoes.setOpaque(false);
        JButton btnBaixar = criarBotao("Baixar", verdeBotao, Color.WHITE);
        JButton btnVisualizar = criarBotao("Visualizar", azulBotao, Color.WHITE);
        painelBotoes.add(btnBaixar);
        painelBotoes.add(btnVisualizar);
        painelConteudo.add(painelBotoes, gbc);

        // === TABELA ABAIXO ===
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] colunas = { "Código", "Descrição", "Data", "Valor", "Status" };
        Object[][] dados = {
                { "001", "Venda balcão", "10/10/2025", "R$ 120,00", "Concluído" },
                { "002", "Venda online", "12/10/2025", "R$ 80,00", "Pendente" },
                { "003", "Venda cartão", "15/10/2025", "R$ 200,00", "Concluído" }
        };

        DefaultTableModel modeloTabela = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabela.getTableHeader().setBackground(marromClaro);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setBackground(begeClaro);
        tabela.setGridColor(marromClaro);
        tabela.setSelectionBackground(new Color(220, 210, 200));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(marromClaro, 2));
        scroll.getViewport().setBackground(begeClaro);

        painelConteudo.add(scroll, gbc);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
        add(painelPrincipal);
        setVisible(true);
    }

    private JTextField criarCampo(Color fundo, Color borda, Color texto) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setForeground(texto);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return campo;
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? corFundo.darker() : corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        botao.setForeground(corTexto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaRelatorios::new);
    }
}