package alphacontrol.views.cliente;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class TelaCliente extends JFrame {
    private JTextField campoPesquisa;
    private JButton botaoPesquisar;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaCliente(){
        setTitle("Tela Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setBackground(new Color(247, 239, 224));

        // ==== Painel Principal ====
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(painelPrincipal);

        // ==== Título ====
        JLabel titulo = new JLabel("Débitos de [Cliente]", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // ==== Painel Busca ====
        JPanel painelBusca = new JPanel();
        campoPesquisa = new JTextField("Busque por data (dd/mm/aaaa)", 25);
        botaoPesquisar = new JButton("Buscar");
        botaoPesquisar.setBackground(new Color(119,140,85));
        botaoPesquisar.setForeground(Color.WHITE);
        botaoPesquisar.setFocusPainted(false);

        painelBusca.add(campoPesquisa);
        painelBusca.add(botaoPesquisar);
        painelPrincipal.add(painelBusca, BorderLayout.BEFORE_FIRST_LINE);

        // ==== Modelo da Tabela ====
        String[] colunas = {"Data", "Produtos", "Valor(R$)", "Ações"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        tabela.getColumn("Ações").setCellRenderer(new BotoesRenderer());
        tabela.getColumn("Ações").setCellEditor(new BotoesEditor(tabela));
    }


    // ==== Botões ====
    private class BotoesRenderer extends JPanel implements TableCellRenderer{
        private final JPanel painel = new JPanel();
        private final JButton btnDetalhes = new JButton("Detalhes");
        private final JButton btnExcluir = new JButton("Excluir");

        public BotoesRenderer(){
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnDetalhes.setBackground(new Color(198, 134, 78));
            btnExcluir.setBackground((new Color(178, 67, 62)));
            add(btnDetalhes);
            add(btnExcluir);

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            return this;
        }
    }

    // ==== Editor de Botões ====
    private class BotoesEditor extends AbstractCellEditor implements TableCellEditor{
        private final JPanel painel = new JPanel();
        private final JButton btnDetalhes = new JButton("Detalhes");
        private final JButton btnExcluir = new JButton("Excluir");

        public BotoesEditor(JTable table){
            painel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            btnDetalhes.setBackground(new Color(198, 134, 78));
            btnExcluir.setBackground((new Color(178, 67, 62)));

            painel.add(btnDetalhes);
            painel.add(btnExcluir);

            btnDetalhes.addActionListener(e -> {
                int row = table.getSelectedRow();
                String data = (String) table.getValueAt(row, 0);
                JOptionPane.showMessageDialog(table, "Detalhes do débito em "+ data);
            });

            btnExcluir.addActionListener(e -> {
                int row = table.getSelectedRow();
                ((DefaultTableModel) table.getModel()).removeRow(row);
            });
        }

        @Override
        public Object getCellEditorValue(){
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCliente().setVisible(true));
    }
}
