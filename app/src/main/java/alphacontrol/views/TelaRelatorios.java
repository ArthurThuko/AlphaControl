package alphacontrol.views.relatorios;

import javax.swing.*;
import javax.swing.border.Border; // Importar a classe Border
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// O nome da classe foi mudado para TelaRelatorios (como no seu último código)
public class TelaRelatorios extends JFrame {

    public TelaRelatorios() {
        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // === PALETA DE CORES (baseada na tela de estoque) ===
        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);
        Color verdeBotao = new Color(72, 166, 90);
        Color azulBotao = new Color(70, 130, 180);

        // === PAINEL PRINCIPAL ===
        JPanel painelPrincipal = new JPanel(new BorderLayout()) {
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
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(new Color(0, 0, 0, 0));

        // === TÍTULO ===
        JLabel titulo = new JLabel("Relatórios", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 28));
        titulo.setForeground(marromEscuro);
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // === PAINEL DE FILTROS ===
        JPanel painelFiltros = new JPanel(new GridBagLayout());
        painelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Tipo de relatório
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFiltros.add(new JLabel("Tipo de Relatório:"), gbc);

        JRadioButton rbVendas = new JRadioButton("Vendas");
        JRadioButton rbProdutos = new JRadioButton("Produtos + vendidos");
        JRadioButton rbFluxo = new JRadioButton("Fluxo de Caixa");
        JRadioButton rbFiados = new JRadioButton("Fiados");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbVendas);
        grupo.add(rbProdutos);
        grupo.add(rbFluxo);
        grupo.add(rbFiados);

        JRadioButton[] radios = {rbVendas, rbProdutos, rbFluxo, rbFiados};
        for (JRadioButton rb : radios) {
            rb.setOpaque(false);
            rb.setForeground(marromEscuro);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 15));
        }

        gbc.gridx = 1;
        painelFiltros.add(rbVendas, gbc);
        gbc.gridx++;
        painelFiltros.add(rbProdutos, gbc);
        gbc.gridx++;
        painelFiltros.add(rbFluxo, gbc);
        gbc.gridx++;
        painelFiltros.add(rbFiados, gbc);

        // Campos de data
        gbc.gridy++;
        gbc.gridx = 0;
        painelFiltros.add(new JLabel("Data Início"), gbc);

        gbc.gridx = 1;
        JTextField campoInicio = criarCampo(begeClaro, marromClaro, marromEscuro);
        campoInicio.setPreferredSize(new Dimension(130, 30));
        campoInicio.setText("dd/mm/aaaa");
        painelFiltros.add(campoInicio, gbc);

        gbc.gridx = 2;
        painelFiltros.add(new JLabel("Data Final"), gbc);

        gbc.gridx = 3;
        JTextField campoFinal = criarCampo(begeClaro, marromClaro, marromEscuro);
        campoFinal.setPreferredSize(new Dimension(130, 30));
        campoFinal.setText("dd/mm/aaaa");
        painelFiltros.add(campoFinal, gbc);

        // Botões
        gbc.gridx = 4;
        JButton btnBaixar = criarBotao("Baixar", verdeBotao, begeClaro);
        painelFiltros.add(btnBaixar, gbc);

        gbc.gridx = 5;
        JButton btnVisualizar = criarBotao("Visualizar", azulBotao, begeClaro);
        painelFiltros.add(btnVisualizar, gbc);

        painelPrincipal.add(painelFiltros, BorderLayout.CENTER);

        // === TABELA ===
        String[] colunas = {"Coluna 1", "Coluna 2", "Coluna 3", "Coluna 4"};
        Object[][] dados = new Object[8][colunas.length];
        JTable tabela = new JTable(new DefaultTableModel(dados, colunas));
        tabela.setRowHeight(30);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(marromClaro);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setGridColor(marromClaro);
        tabela.setBackground(begeClaro);
        tabela.setForeground(marromEscuro);

        JScrollPane scroll = new JScrollPane(tabela);

        // --- MODIFICAÇÃO AQUI ---
        // Criamos uma borda composta:
        // 1. Uma borda de linha (MatteBorder) de 1 pixel no topo, na cor 'marromClaro'
        // 2. Uma borda de espaço (EmptyBorder) para o preenchimento (padding)
        Border bordaLinha = BorderFactory.createMatteBorder(1, 0, 0, 0, marromClaro);
        Border bordaEspaco = BorderFactory.createEmptyBorder(19, 50, 10, 50); // (19 + 1 da linha = 20 de espaço no topo)

        scroll.setBorder(BorderFactory.createCompoundBorder(bordaLinha, bordaEspaco));
        // --- FIM DA MODIFICAÇÃO ---

        scroll.setBackground(begeClaro);

        painelPrincipal.add(scroll, BorderLayout.SOUTH);

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
        campo.setFont(new Font("SansSerif", Font.PLAIN, 15));
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
        botao.setFont(new Font("SansSerif", Font.BOLD, 15));
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaRelatorios::new);
    }
}