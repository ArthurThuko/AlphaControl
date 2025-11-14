package alphacontrol.views;

import alphacontrol.controllers.ProdutoController;
import alphacontrol.controllers.TelaPrincipalController;
import alphacontrol.views.components.Navbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TelaRelatorios extends JFrame {
    
    private TelaPrincipalController mainController;

    public TelaRelatorios(TelaPrincipalController mainController) {
        this.mainController = mainController;
        
        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setJMenuBar(new Navbar(this, this.mainController, "Relatório"));

        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);
        Color verdeBotao = new Color(72, 166, 90);
        Color azulBotao = new Color(70, 130, 180);

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

        JLabel titulo = new JLabel("Relatórios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(marromEscuro);
        titulo.setBorder(new EmptyBorder(0, 0, 40, 0));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel(new GridBagLayout());
        painelConteudo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTipo = new JLabel("Tipo de Relatório:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
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
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

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

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblInicio = new JLabel("Data Início:");
        lblInicio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
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
        lblFinal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblFinal.setForeground(marromEscuro);
        painelConteudo.add(lblFinal, gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        JTextField campoFinal = criarCampo(begeClaro, marromClaro, marromEscuro);
        campoFinal.setPreferredSize(new Dimension(220, 35));
        campoFinal.setText("dd/mm/aaaa");
        painelConteudo.add(campoFinal, gbc);

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
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabela.setRowHeight(30);
        tabela.setBackground(begeClaro);
        tabela.setGridColor(marromClaro);
        tabela.setSelectionBackground(new Color(220, 210, 200));
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(marromEscuro);
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        header.setReorderingAllowed(false);

        PaddedCellRenderer.apply(tabela, 0, SwingConstants.CENTER);
        PaddedCellRenderer.apply(tabela, 1, SwingConstants.LEFT);
        PaddedCellRenderer.apply(tabela, 2, SwingConstants.CENTER);
        PaddedCellRenderer.apply(tabela, 3, SwingConstants.RIGHT);
        PaddedCellRenderer.apply(tabela, 4, SwingConstants.CENTER);

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
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return campo;
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? corFundo.brighter() : (getModel().isPressed() ? corFundo.darker() : corFundo));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        botao.setForeground(corTexto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }

    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer(int alignment) {
            setHorizontalAlignment(alignment);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                c.setBackground(table.getBackground());
                c.setForeground(table.getForeground());
            }
            return c;
        }

        public static void apply(JTable table, int col, int alignment) {
            table.getColumnModel().getColumn(col).setCellRenderer(new PaddedCellRenderer(alignment));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaRelatorios(null).setVisible(true);
        });
    }
}