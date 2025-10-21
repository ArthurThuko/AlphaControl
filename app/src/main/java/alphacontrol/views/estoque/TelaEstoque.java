package alphacontrol.views.estoque;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TelaEstoque extends JFrame {

    public TelaEstoque() {
        setTitle("Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // ==== Cores base ====
        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);
        Font fonte = new Font("Segoe UI", Font.PLAIN, 20);

        // ==== Painel principal com GridBagLayout ====
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(begeFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==== Título ====
        JLabel titulo = new JLabel("Estoque", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(marromEscuro);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        painelPrincipal.add(titulo, gbc);

        // ==== Campo de busca e botões juntos ====
        JTextField txtPesquisa = new JTextField("Pesquise por nome...");
        txtPesquisa.setFont(fonte);
        txtPesquisa.setForeground(Color.GRAY);
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(marromClaro, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JButton btnFiltrar = criarBotao("Filtrar Produto", marromClaro, Color.WHITE, 200, 40);
        JButton btnPesquisar = criarBotao("Pesquisar", new Color(0, 128, 0), Color.WHITE, 140, 40);
        JButton btnAdd = criarBotao("Adicionar", new Color(30, 90, 180), Color.WHITE, 160, 40);

        JPanel topoBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topoBotoes.setBackground(begeFundo);
        topoBotoes.add(txtPesquisa);
        topoBotoes.add(btnFiltrar);
        topoBotoes.add(btnPesquisar);
        topoBotoes.add(btnAdd);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        painelPrincipal.add(topoBotoes, gbc);

        // ==== Tabela ====
        String[] colunas = {"Nome", "Quantidade", "Categoria", "Valor Compra (R$)", "Valor Venda (R$)", "Ações"};
        Object[][] dados = {
                {"Produto 1", "", "Categoria 1", "10.45", "", ""},
                {"Produto 2", "", "Categoria 1", "", "", ""},
                {"Produto 3", "", "Categoria 1", "", "", ""},
                {"Produto 4", "", "Categoria 1", "", "", ""},
                {"Produto 5", "", "Categoria 1", "", "", ""}
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(modelo);
        tabela.setFont(fonte);
        tabela.setRowHeight(70); // altura maior para caber os botões
        tabela.setBackground(begeClaro);
        tabela.setGridColor(marromClaro);
        tabela.setShowVerticalLines(true);
        tabela.setSelectionBackground(new Color(225, 200, 170));
        tabela.setSelectionForeground(marromEscuro);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(marromMedio);
        header.setForeground(Color.WHITE);
        header.setFont(fonte.deriveFont(Font.BOLD, 22f));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));

        // ==== Renderizador para as demais colunas com padding ====
        TableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            private final int padding = 10;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
                cell.setHorizontalAlignment(SwingConstants.LEFT);
                return cell;
            }
        };

        for (int i = 0; i < tabela.getColumnCount() - 1; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(paddingRenderer);
        }

        // ==== Renderização de botões na coluna Ações centralizados ====
        tabela.getColumn("Ações").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JPanel painel = new JPanel(new GridBagLayout());
                painel.setOpaque(true);
                painel.setBackground(new Color(255, 250, 240));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(0, 5, 0, 5);

                JButton btnEditar = criarBotao("Editar", new Color(180, 90, 40), Color.WHITE, 120, 35);
                JButton btnExcluir = criarBotao("Excluir", new Color(255, 100, 100), new Color(77, 51, 30), 120, 35);

                gbc.gridx = 0;
                painel.add(btnEditar, gbc);
                gbc.gridx = 1;
                painel.add(btnExcluir, gbc);

                return painel;
            }
        });

        // ==== Ajuste de largura das colunas ====
        TableColumnModel colModel = tabela.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(350); // Nome
        colModel.getColumn(1).setPreferredWidth(120); // Quantidade
        colModel.getColumn(2).setPreferredWidth(200); // Categoria
        colModel.getColumn(3).setPreferredWidth(200); // Valor Compra
        colModel.getColumn(4).setPreferredWidth(180); // Valor Venda
        colModel.getColumn(5).setPreferredWidth(300); // Ações

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(marromClaro, 2));
        scroll.getViewport().setBackground(begeClaro);
        scroll.setPreferredSize(new Dimension(1600, 600));

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        painelPrincipal.add(scroll, gbc);

        add(painelPrincipal);
    }

    private JButton criarBotao(String texto, Color bg, Color fg, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setBackground(bg);
        botao.setForeground(fg);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        botao.setPreferredSize(new Dimension(largura, altura));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaEstoque tela = new TelaEstoque();
            tela.setVisible(true);
        });
    }
}
