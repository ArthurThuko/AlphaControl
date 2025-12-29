package alphacontrol.views.fiado;

import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.models.Cliente;
import alphacontrol.models.Produto;
import alphacontrol.models.Fiado;
import alphacontrol.models.FiadoItem;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ModalAdicionarFiado extends JDialog {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);

    private final Cliente cliente;
    private final FiadoController fiadoController;
    private final ProdutoController produtoController;

    private JComboBox<Produto> cbProdutos;
    private JSpinner spQuantidade;
    private JTable tabelaItens;
    private DefaultTableModel modelItens;

    public ModalAdicionarFiado(
            JFrame parent,
            FiadoController fiadoController,
            ProdutoController produtoController,
            Cliente cliente) {
        super(parent, "Adicionar Fiado", true);
        this.fiadoController = fiadoController;
        this.produtoController = produtoController;
        this.cliente = cliente;

        setUndecorated(true);
        setSize(750, 520);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelFundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(MARROM_CLARO);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        painelFundo.setOpaque(false);
        painelFundo.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(painelFundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registrar Fiado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(MARROM_ESCURO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        painelFundo.add(lblTitulo, gbc);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblCliente.setForeground(MARROM_ESCURO);

        JLabel lblNomeCliente = new JLabel(cliente.getNome());
        lblNomeCliente.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNomeCliente.setForeground(VERDE_OLIVA);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        painelFundo.add(lblCliente, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        painelFundo.add(lblNomeCliente, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        painelFundo.add(new JLabel("Produto:"), gbc);

        cbProdutos = new JComboBox<>();
        for (Produto p : produtoController.listar()) {
            cbProdutos.addItem(p);
        }

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        painelFundo.add(cbProdutos, gbc);

        gbc.gridx = 3;
        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spQuantidade.setPreferredSize(new Dimension(80, 40));
        painelFundo.add(spQuantidade, gbc);

        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.gridwidth = 2;

        JButton btnAdicionar = new RoundedButton("Adicionar Item", DOURADO_SUAVE, Color.WHITE);
        painelFundo.add(btnAdicionar, gbc);

        modelItens = new DefaultTableModel(
                new Object[] { "Produto", "Qtd", "Valor Unit.", "Subtotal" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabelaItens = new JTable(modelItens);
        tabelaItens.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setPreferredSize(new Dimension(650, 180));

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        painelFundo.add(scroll, gbc);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);

        JButton btnSalvar = new RoundedButton("Salvar", VERDE_OLIVA, Color.WHITE);
        JButton btnCancelar = new RoundedButton("Cancelar", VERMELHO_TERROSO, Color.WHITE);

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        gbc.gridy = 5;
        painelFundo.add(painelBotoes, gbc);

        btnAdicionar.addActionListener(e -> adicionarItem());
        btnSalvar.addActionListener(e -> salvarFiado());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void adicionarItem() {
        Produto produto = (Produto) cbProdutos.getSelectedItem();
        int qtd = (int) spQuantidade.getValue();

        for (int i = 0; i < modelItens.getRowCount(); i++) {
            Produto pTabela = (Produto) modelItens.getValueAt(i, 0);
            if (pTabela.getProdutoId() == produto.getProdutoId()) {
                int qtdAtual = (int) modelItens.getValueAt(i, 1);
                int novaQtd = qtdAtual + qtd;
                double subtotal = novaQtd * produto.getPreco();

                modelItens.setValueAt(novaQtd, i, 1);
                modelItens.setValueAt(subtotal, i, 3);
                return;
            }
        }

        modelItens.addRow(new Object[] {
                produto,
                qtd,
                produto.getPreco(),
                produto.getPreco() * qtd
        });
    }
    
    private void salvarFiado() {

    if (modelItens.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this,
                "Adicione ao menos um produto.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // 1️⃣ Criar o FIADO
        Fiado fiado = new Fiado();
        fiado.setClienteId(cliente.getId());
        fiado.setValor(calcularTotal());
        fiado.setData(LocalDateTime.now());
        fiado.setStatus("PENDENTE");

        // 2️⃣ Criar LISTA DE ITENS
        List<FiadoItem> itens = new ArrayList<>();

        for (int i = 0; i < modelItens.getRowCount(); i++) {
            Produto p = (Produto) modelItens.getValueAt(i, 0);

            FiadoItem item = new FiadoItem();
            item.setProdutoId(p.getProdutoId());
            item.setQuantidade((int) modelItens.getValueAt(i, 1));
            item.setValorUnitario((double) modelItens.getValueAt(i, 2));

            itens.add(item);
        }

        // 3️⃣ SALVAR TUDO DE UMA VEZ
        fiadoController.inserirFiadoComItens(fiado, itens);

        JOptionPane.showMessageDialog(this,
                "Fiado registrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro ao salvar fiado:\n" + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}

    private double calcularTotal() {
        double total = 0;
        for (int i = 0; i < modelItens.getRowCount(); i++) {
            total += (double) modelItens.getValueAt(i, 3); // subtotal
        }
        return total;
    }

    static class RoundedButton extends JButton {
        private final Color bg;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(160, 45));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}