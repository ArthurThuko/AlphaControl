package alphacontrol.views.fiado;

import alphacontrol.conexao.Conexao;
import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.dao.ItemVendaDAO;
import alphacontrol.dao.ProdutoDAO;
import alphacontrol.models.Fiado;
import alphacontrol.models.ItemVenda;
import alphacontrol.models.Produto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

public class TelaProdutosFiado extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_MUSGO = new Color(119, 140, 85);

    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel labelTotal;

    private Fiado fiado;
    private ItemVendaDAO itemVendaDAO;
    private ProdutoDAO produtoDAO;

    public TelaProdutosFiado(Fiado fiado, FiadoController controller, Window parent) throws Exception {

        super(parent, "Produtos do Fiado", ModalityType.APPLICATION_MODAL);

        this.fiado = fiado;

        Connection conn = Conexao.getConexao();
        itemVendaDAO = new ItemVendaDAO(conn);
        produtoDAO = new ProdutoDAO(conn);

        setSize(700, 480);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BEGE_FUNDO);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Produtos do Fiado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(MARROM_ESCURO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        painelPrincipal.add(titulo, gbc);

        String[] colunas = { "Produto", "Qtd", "Valor Unitário", "Total" };

        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        configurarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        RoundedPanel painelTabela = new RoundedPanel(15);
        painelTabela.setLayout(new BorderLayout());
        painelTabela.setBackground(BEGE_CLARO);
        painelTabela.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelTabela.add(scroll, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        painelPrincipal.add(painelTabela, gbc);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(BEGE_FUNDO);

        labelTotal = new JLabel("Total: R$ 0,00");
        labelTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTotal.setForeground(MARROM_ESCURO);

        rodape.add(labelTotal, BorderLayout.WEST);

        JButton fechar = new RoundedButton("Fechar", VERDE_MUSGO, Color.WHITE, 120, 40);
        fechar.addActionListener(e -> dispose());

        rodape.add(fechar, BorderLayout.EAST);

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        painelPrincipal.add(rodape, gbc);

        add(painelPrincipal);

        carregarDados();
    }

    private void carregarDados() {

        if (fiado.getVendaId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Este registro não possui venda vinculada.");
            return;
        }

        try {

            List<ItemVenda> itens = itemVendaDAO.listarItensPorVenda(fiado.getVendaId());

            modelo.setRowCount(0);

            double total = 0;

            for (ItemVenda item : itens) {

                Produto produto = produtoDAO.buscarPorId(item.getProdutoId());

                modelo.addRow(new Object[] {
                        produto.getNome(),
                        item.getQuantidade(),
                        item.getValorUnitario(),
                        item.getValorTotal()
                });

                total += item.getValorTotal();
            }

            DecimalFormat df = new DecimalFormat("R$ #,##0.00");
            labelTotal.setText("Total: " + df.format(total));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTabela(JTable tabela) {

        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(42);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.setBackground(MARROM_CLARO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 42));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        tabela.getColumnModel().getColumn(1).setCellRenderer(center);
        tabela.getColumnModel().getColumn(2).setCellRenderer(right);
        tabela.getColumnModel().getColumn(3).setCellRenderer(right);
    }

    static class RoundedPanel extends JPanel {

        private final int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius);

            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {

        private final Color background;
        private final Color hover;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {

            super(text);

            background = bg;
            hover = bg.brighter();

            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(
                    getModel().isRollover() ? hover : background);

            g2.fill(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    15,
                    15));

            g2.dispose();

            super.paintComponent(g);
        }
    }
}