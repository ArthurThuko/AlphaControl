package alphacontrol.views.fiado;

import alphacontrol.controllers.fiado.FiadoController;
import alphacontrol.controllers.produto.ProdutoController;
import alphacontrol.models.Cliente;
import alphacontrol.models.Fiado;
import alphacontrol.models.FiadoItem;
import alphacontrol.models.Produto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaDetalheFiado extends JDialog {

    private DefaultTableModel modelo;
    private JTable tabela;
    private Cliente cliente;
    private FiadoController fiadoController;

    private ProdutoController produtoController;

    public TelaDetalheFiado(
            Cliente cliente,
            FiadoController fiadoController,
            ProdutoController produtoController,
            JFrame parent) {
        super(parent, "Débitos do Cliente", true);
        this.cliente = cliente;
        this.fiadoController = fiadoController;
        this.produtoController = produtoController;

        setSize(750, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(247, 239, 224));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel("Débitos de " + cliente.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[] { "ID", "Data", "Valor", "Status" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(40);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        centralizarColunas();

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirFiadoSelecionado();
                }
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton fechar = new JButton("Fechar");
        fechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel();
        rodape.add(fechar);
        add(rodape, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        try {
            modelo.setRowCount(0);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            DecimalFormat dinheiro = new DecimalFormat("#,##0.00");

            List<Fiado> fiados = fiadoController.listarPorCliente(cliente.getId());

            for (Fiado f : fiados) {
                modelo.addRow(new Object[] {
                        f.getId(),
                        f.getData().format(df),
                        "R$ " + dinheiro.format(f.getValor()),
                        f.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar fiados");
        }
    }

    private void abrirFiadoSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0)
            return;

        String status = modelo.getValueAt(row, 3).toString();
        if (!status.equals("PENDENTE"))
            return;

        int fiadoId = (int) modelo.getValueAt(row, 0);

        Fiado fiado = new Fiado();
        fiado.setId(fiadoId);
        fiado.setStatus(status);

        abrirModalFiado(fiado);
    }

    /* ================= MODAL DETALHE ================= */

    private void abrirModalFiado(Fiado fiado) {
        JDialog dialog = new JDialog(this, "Detalhes do Fiado", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Itens do Fiado", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(new EmptyBorder(15, 0, 10, 0));
        dialog.add(titulo, BorderLayout.NORTH);

        DefaultTableModel modelItens = new DefaultTableModel(
                new Object[] { "Nome", "Qtd", "Valor Unit.", "Subtotal" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tabelaItens = new JTable(modelItens);
        tabelaItens.setRowHeight(30);
        tabelaItens.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DecimalFormat df = new DecimalFormat("#,##0.00");
        double total = 0;

        List<FiadoItem> itens = fiadoController.listarItens(fiado.getId());
        for (FiadoItem item : itens) {
            Produto produto = produtoController.buscarPorId(item.getProdutoId());
            String nomeProduto = produto != null ? produto.getNome() : "Produto removido";

            modelItens.addRow(new Object[] {
                    nomeProduto,
                    item.getQuantidade(),
                    "R$ " + df.format(item.getValorUnitario()),
                    "R$ " + df.format(item.getSubtotal())
            });

            total += item.getSubtotal();
        }

        dialog.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        JLabel lblTotal = new JLabel("Total: R$ " + df.format(total));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton fechar = new JButton("Fechar");
        fechar.addActionListener(e -> dialog.dispose());

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.add(lblTotal, BorderLayout.WEST);
        rodape.add(fechar, BorderLayout.EAST);

        dialog.add(rodape, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void centralizarColunas() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}