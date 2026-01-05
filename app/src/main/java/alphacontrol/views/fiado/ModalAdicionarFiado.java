package alphacontrol.views.fiado;

import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModalAdicionarFiado extends JDialog {

    private final Cliente cliente;
    private final FiadoController fiadoController;
    private final ProdutoController produtoController;

    private JTextField txtBuscarProduto;
    private JList<Produto> listaProdutos;
    private DefaultListModel<Produto> listModel;

    private JSpinner spQuantidade;
    private JTable tabelaItens;
    private DefaultTableModel modelItens;
    private JLabel lblTotal;

    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);

    private List<Produto> produtos;

    public ModalAdicionarFiado(
            JFrame parent,
            FiadoController fiadoController,
            ProdutoController produtoController,
            Cliente cliente) {

        super(parent, "Adicionar Fiado", true);
        this.fiadoController = fiadoController;
        this.produtoController = produtoController;
        this.cliente = cliente;
        this.produtos = produtoController.listar();

        setUndecorated(true);
        setSize(820, 580);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = criarPainelArredondado();
        root.setLayout(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(root);

        root.add(criarCabecalho(), BorderLayout.NORTH);
        root.add(criarCentro(), BorderLayout.CENTER);
        root.add(criarRodape(), BorderLayout.SOUTH);
    }

    /* ===================== CABEÇALHO ===================== */

    private JPanel criarCabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel titulo = new JLabel("Registrar Fiado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel clienteLbl = new JLabel("Cliente: " + cliente.getNome());
        clienteLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        p.add(titulo, BorderLayout.NORTH);
        p.add(clienteLbl, BorderLayout.SOUTH);
        return p;
    }

    /* ===================== CENTRO ===================== */

    private JPanel criarCentro() {
        JPanel centro = new JPanel(new GridLayout(1, 2, 15, 0));
        centro.setOpaque(false);

        centro.add(criarPainelProdutos());
        centro.add(criarPainelResumo());

        return centro;
    }

    private JPanel criarPainelProdutos() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setOpaque(false);

        txtBuscarProduto = new JTextField();
        txtBuscarProduto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscarProduto.setBorder(BorderFactory.createTitledBorder("Pesquisar produto"));

        listModel = new DefaultListModel<>();
        listaProdutos = new JList<>(listModel);
        listaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProdutos.setCellRenderer(new ProdutoListRenderer());

        atualizarLista("");

        txtBuscarProduto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void filtrar() {
                atualizarLista(txtBuscarProduto.getText());
            }
        });

        JScrollPane scroll = new JScrollPane(listaProdutos);

        JPanel acao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acao.setOpaque(false);

        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        JButton btnAdd = new JButton("Adicionar");

        btnAdd.addActionListener(e -> adicionarItem());

        acao.add(new JLabel("Qtd:"));
        acao.add(spQuantidade);
        acao.add(btnAdd);

        p.add(txtBuscarProduto, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(acao, BorderLayout.SOUTH);

        return p;
    }

    /* ===================== RESUMO ===================== */

    private JPanel criarPainelResumo() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setOpaque(false);

        modelItens = new DefaultTableModel(
                new Object[] { "Produto", "Qtd", "Unit.", "Subtotal" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabelaItens = new JTable(modelItens);
        tabelaItens.setRowHeight(26);

        JScrollPane scroll = new JScrollPane(tabelaItens);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setOpaque(false);

        JButton btnRemover = new RoundedButton("Remover Item", VERMELHO_TERROSO, Color.WHITE);
        btnRemover.addActionListener(e -> removerItemSelecionado());

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        painelInferior.add(btnRemover, BorderLayout.WEST);
        painelInferior.add(lblTotal, BorderLayout.EAST);

        p.add(scroll, BorderLayout.CENTER);
        p.add(painelInferior, BorderLayout.SOUTH);

        return p;
    }

    /* ===================== RODAPÉ ===================== */

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);

        JButton cancelar = new JButton("Cancelar");
        JButton salvar = new JButton("Salvar");

        cancelar.addActionListener(e -> dispose());
        salvar.addActionListener(e -> salvarFiado());

        p.add(cancelar);
        p.add(salvar);
        return p;
    }

    /* ===================== LÓGICA ===================== */

    private void atualizarLista(String filtro) {
        listModel.clear();
        produtos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(filtro.toLowerCase()))
                .forEach(listModel::addElement);
    }

    private void adicionarItem() {
        Produto produto = listaProdutos.getSelectedValue();
        int qtd = (int) spQuantidade.getValue();

        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        // ❌ Bloqueia produto repetido
        for (int i = 0; i < modelItens.getRowCount(); i++) {
            Produto pTabela = (Produto) modelItens.getValueAt(i, 0);
            if (pTabela.getProdutoId() == produto.getProdutoId()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Este produto já foi adicionado.\nRemova o item para alterar a quantidade.",
                        "Produto duplicado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        modelItens.addRow(new Object[] {
                produto,
                qtd,
                produto.getPreco(),
                produto.getPreco() * qtd
        });

        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = 0;
        for (int i = 0; i < modelItens.getRowCount(); i++) {
            total += (double) modelItens.getValueAt(i, 3);
        }
        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void salvarFiado() {
        if (modelItens.getRowCount() == 0)
            return;

        Fiado fiado = new Fiado();
        fiado.setClienteId(cliente.getId());
        fiado.setData(LocalDateTime.now());
        fiado.setValor(calcularTotal());
        fiado.setStatus("PENDENTE");

        List<FiadoItem> itens = new ArrayList<>();

        for (int i = 0; i < modelItens.getRowCount(); i++) {
            Produto p = (Produto) modelItens.getValueAt(i, 0);

            FiadoItem item = new FiadoItem();
            item.setProdutoId(p.getProdutoId());
            item.setQuantidade((int) modelItens.getValueAt(i, 1));
            item.setValorUnitario((double) modelItens.getValueAt(i, 2));

            itens.add(item);
        }

        fiadoController.inserirFiadoComItens(fiado, itens);
        dispose();
    }

    private double calcularTotal() {
        double total = 0;
        for (int i = 0; i < modelItens.getRowCount(); i++) {
            total += (double) modelItens.getValueAt(i, 3);
        }
        return total;
    }

    private void removerItemSelecionado() {
        int linha = tabelaItens.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelItens.removeRow(linha);
        atualizarTotal();
    }

    /* ===================== UI HELPERS ===================== */

    private JPanel criarPainelArredondado() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(247, 239, 224));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
    }

    static class ProdutoListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            Produto p = (Produto) value;
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            lbl.setText(p.getNome() + "  —  R$ " + p.getPreco());
            lbl.setBorder(new EmptyBorder(5, 8, 5, 8));
            return lbl;
        }
    }

    // coloque isto dentro da classe ModalAdicionarFiado, antes da última chave de
    // fechamento
    static class RoundedButton extends JButton {
        private final Color bg;
        private final Color hover;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            this.hover = bg.brighter();
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(140, 38));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            // opcional: efeito hover
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill = getModel().isRollover() ? hover : bg;
            if (!isEnabled()) {
                fill = fill.darker();
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            // borda sutil
            g2.setColor(fill.darker());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.dispose();

            super.paintComponent(g);
        }

        // ajustar o posicionamento do texto porque pintamos o background manualmente
        @Override
        public void setText(String text) {
            super.setText(text);
        }

        @Override
        public Insets getInsets() {
            return new Insets(6, 12, 6, 12);
        }
    }
}