package alphacontrol.views.fluxo_caixa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import java.util.List;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.views.components.Navbar; 

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

public class TelaFluxoCaixa extends JFrame {
    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color VERDE_CLARO = new Color(202, 219, 183);
    private static final Color VERDE_BORDA = new Color(139, 160, 118);
    private static final Color VERDE_BOTAO = new Color(101, 125, 64);
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62);
    private static final Color AZUL_ACAO = new Color(0, 100, 200);

    private static final Color BEGE_PAINEL_TOTAIS = new Color(252, 250, 245);
    private static final Color BEGE_BORDA_TOTAIS = new Color(222, 214, 196);
    
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color BEGE_GRID = new Color(223, 214, 198);

    private JTable tabelaEntradas;
    private JTable tabelaSaidas;
    private JLabel lblTotalEntradas = new JLabel("R$ 0,00");
    private JLabel lblTotalSaidas = new JLabel("R$ 0,00");
    private JLabel lblSaldo = new JLabel("R$ 0,00");
    private FluxoCaixaController controller;
    private TelaPrincipalController mainController;

    public TelaFluxoCaixa(TelaPrincipalController mainController) {
        this.mainController = mainController;
        controller = new FluxoCaixaController();
        setTitle("Fluxo de Caixa = AlphaControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BEGE_FUNDO);

        setJMenuBar(new Navbar(this, mainController, "Fluxo de Caixa"));

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(BEGE_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Fluxo de Caixa", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(titulo, gbc);

        JPanel painelTotais = criarPainelTotais();
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(painelTotais, gbc);

        JPanel painelTabelas = new JPanel(new GridLayout(1, 2, 30, 0)); 
        painelTabelas.setOpaque(false);
        
        JPanel painelEntradas = criarPainelEntradas();
        JPanel painelSaidas = criarPainelSaidas();
        
        painelTabelas.add(painelEntradas);
        painelTabelas.add(painelSaidas);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1; 
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(painelTabelas, gbc);

        add(painelPrincipal);

        atualizarSaldo();
    }

    private JPanel criarPainelTotais() {
        JPanel painel = new RoundedPanel(25, BEGE_PAINEL_TOTAIS, BEGE_BORDA_TOTAIS);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.33;
        gbc.gridy = 0;

        gbc.gridx = 0;
        painel.add(criarPainelTotalUnitario("Total Entradas", lblTotalEntradas, VERDE_BOTAO), gbc);

        gbc.gridx = 1;
        painel.add(criarPainelTotalUnitario("Total Saídas", lblTotalSaidas, VERMELHO_TERROSO), gbc);

        gbc.gridx = 2;
        painel.add(criarPainelTotalUnitario("Saldo Atual", lblSaldo, null), gbc);

        return painel;
    }

    private JPanel criarPainelTotalUnitario(String titulo, JLabel labelValor, Color corValor) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        painel.add(lblTitulo, gbc);

        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        if (corValor != null) {
            labelValor.setForeground(corValor);
        } else {
            labelValor.setForeground(new Color(30, 60, 110)); 
        }
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        painel.add(labelValor, gbc);
        
        return painel;
    }


    private JPanel criarPainelEntradas() {
        JPanel painel = new RoundedPanel(15, BEGE_CLARO, MARROM_CLARO);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Entradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblTitulo, gbc);

        DefaultTableModel modelo = new DefaultTableModel(new String[] {
                "Nome", "Valor (R$)", "Data", "Editar", "Excluir"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col >= 3;
            }
        };

        tabelaEntradas = new JTable(modelo);

        tabelaEntradas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int linha = tabelaEntradas.rowAtPoint(evt.getPoint());
                int coluna = tabelaEntradas.columnAtPoint(evt.getPoint());
                if (linha < 0) return;

                if (coluna == 3) {
                    MovimentacaoCaixa mov = controller.listarEntradas().get(linha);

                    ModalEntrada modal = new ModalEntrada(TelaFluxoCaixa.this, mov);
                    modal.setVisible(true);

                    recarregarTabelaEntradas();
                } else if (coluna == 4) {
                    int confirm = JOptionPane.showConfirmDialog(
                            TelaFluxoCaixa.this,
                            "Tem certeza que deseja excluir esta entrada?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        MovimentacaoCaixa mov = controller.listarEntradas().get(linha);
                        controller.removerMovimentacao(mov.getId());
                        recarregarTabelaEntradas();
                    }
                }
            }
        });

        configurarTabela(tabelaEntradas);
        recarregarTabelaEntradas();
        
        JScrollPane scroll = new JScrollPane(tabelaEntradas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO); 

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Entrada", VERDE_BOTAO, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalEntrada modal = new ModalEntrada(this);
            modal.setVisible(true);
            recarregarTabelaEntradas();
        });
        gbc.gridy = 2; 
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0); 
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaidas() {
        Color VERMELHO_BORDA = new Color(178, 67, 62);
        Color VERMELHO_BOTAO = new Color(178, 67, 62);

        JPanel painel = new RoundedPanel(15, BEGE_CLARO, MARROM_CLARO);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Saídas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblTitulo, gbc);

        DefaultTableModel modelo = new DefaultTableModel(new String[] {
                "Nome", "Valor (R$)", "Data", "Editar", "Excluir"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col >= 3;
            }
        };

        tabelaSaidas = new JTable(modelo);

        tabelaSaidas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int linha = tabelaSaidas.rowAtPoint(evt.getPoint());
                int coluna = tabelaSaidas.columnAtPoint(evt.getPoint());
                if (linha < 0) return;

                if (coluna == 3) {
                    MovimentacaoCaixa mov = controller.listarSaidas().get(linha);

                    ModalSaida modal = new ModalSaida(TelaFluxoCaixa.this, mov);
                    modal.setVisible(true);

                    recarregarTabelaSaidas();
                } else if (coluna == 4) {
                    int confirm = JOptionPane.showConfirmDialog(
                            TelaFluxoCaixa.this,
                            "Tem certeza que deseja excluir esta saída?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        MovimentacaoCaixa mov = controller.listarSaidas().get(linha);
                        controller.removerMovimentacao(mov.getId());
                        recarregarTabelaSaidas();
                    }
                }
            }
        });

        configurarTabelaSaidas(tabelaSaidas);
        recarregarTabelaSaidas();

        JScrollPane scroll = new JScrollPane(tabelaSaidas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO); 

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Saída", VERMELHO_BOTAO, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalSaida modal = new ModalSaida(this);
            modal.setVisible(true);
            recarregarTabelaSaidas();
        });
        gbc.gridy = 2; 
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0); 
        painel.add(btnAdd, gbc);

        return painel;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(BEGE_GRID);
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));
        
        PaddedCellRenderer textRenderer = new PaddedCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        PaddedCellRenderer valueRenderer = new PaddedCellRenderer();
        valueRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        PaddedCellRenderer dateRenderer = new PaddedCellRenderer();
        dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        ActionCellRenderer actionRenderer = new ActionCellRenderer();

        tabela.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabela.getColumnModel().getColumn(1).setCellRenderer(valueRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setMaxWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setMaxWidth(100);
    }

    private void configurarTabelaSaidas(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(BEGE_GRID);
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(MARROM_MEDIO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));
        
        PaddedCellRenderer textRenderer = new PaddedCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        PaddedCellRenderer valueRenderer = new PaddedCellRenderer();
        valueRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        PaddedCellRenderer dateRenderer = new PaddedCellRenderer();
        dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        ActionCellRenderer actionRenderer = new ActionCellRenderer();

        tabela.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabela.getColumnModel().getColumn(1).setCellRenderer(valueRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setMaxWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setMaxWidth(100);
    }

    private String calcularTotal(DefaultTableModel modelo) {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += Double.parseDouble(modelo.getValueAt(i, 1).toString().replace(",", "."));
        }
        return new DecimalFormat("#,##0.00").format(total);
    }

    private void atualizarSaldo() {
        double totalEntradas = 0;
        double totalSaidas = 0;

        try {
            if (lblTotalEntradas != null) {
                String textoEntradas = lblTotalEntradas.getText()
                    .replace("R$", "")
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".");
                if (!textoEntradas.isEmpty()) {
                    totalEntradas = Double.parseDouble(textoEntradas);
                }
            }
             if (lblTotalSaidas != null) {
                 String textoSaidas = lblTotalSaidas.getText()
                    .replace("R$", "")
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".");
                if (!textoSaidas.isEmpty()) {
                    totalSaidas = Double.parseDouble(textoSaidas);
                }
            }
        } catch (NumberFormatException e) {
            totalEntradas = 0;
            totalSaidas = 0;
             try {
                totalEntradas = Double.parseDouble(calcularTotal((DefaultTableModel) tabelaEntradas.getModel()).replace(".", "").replace(",", "."));
             } catch (Exception ex) { totalEntradas = 0; }
             try {
                totalSaidas = Double.parseDouble(calcularTotal((DefaultTableModel) tabelaSaidas.getModel()).replace(".", "").replace(",", "."));
             } catch (Exception ex) { totalSaidas = 0; }
        }

        double saldo = totalEntradas - totalSaidas;
        lblSaldo.setText(new DecimalFormat("R$ #,##0.00").format(saldo));
        
        if (saldo < 0) {
            lblSaldo.setForeground(VERMELHO_TERROSO);
        } else if (saldo > 0) {
            lblSaldo.setForeground(VERDE_BOTAO);
        } else {
            lblSaldo.setForeground(new Color(30, 60, 110));
        }
    }

    private void recarregarTabelaEntradas() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaEntradas.getModel();
        modelo.setRowCount(0);

        List<MovimentacaoCaixa> entradas = controller.listarEntradas();
        for (MovimentacaoCaixa m : entradas) {
            modelo.addRow(new Object[] {
                    m.getNome(),
                    String.format("%.2f", m.getValor()),
                    m.getData(),
                    "Editar",
                    "Excluir"
            });
        }

        lblTotalEntradas.setText("R$ " + calcularTotal(modelo));
        atualizarSaldo();
    }

    private void recarregarTabelaSaidas() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaSaidas.getModel();
        modelo.setRowCount(0);

        List<MovimentacaoCaixa> saidas = controller.listarSaidas();
        for (MovimentacaoCaixa m : saidas) {
            modelo.addRow(new Object[] {
                    m.getNome(),
                    String.format("%.2f", m.getValor()),
                    m.getData(),
                    "Editar",
                    "Excluir"
            });
        }

        lblTotalSaidas.setText("R$ " + calcularTotal(modelo));
        atualizarSaldo();
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g.create(); 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    static class RoundedButton extends JButton {
        private final Color bg, hover;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            this.bg = bg;
            this.hover = bg.brighter();
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g);
        }
    }



    static class PaddedCellRenderer extends DefaultTableCellRenderer {
        public PaddedCellRenderer() {
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setHorizontalAlignment(SwingConstants.CENTER);
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
    }

    static class ActionCellRenderer extends DefaultTableCellRenderer {
        
        public ActionCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(new EmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            c.setFont(new Font("Segoe UI", Font.BOLD, 16));

            if (value.equals("Editar")) {
                c.setForeground(AZUL_ACAO);
            } else if (value.equals("Excluir")) {
                c.setForeground(VERMELHO_TERROSO);
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
            } else {
                c.setBackground(table.getBackground());
            }
            return c;
        }
    }
    
    static class HeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            ((JComponent) c).setBorder(new EmptyBorder(0, 15, 0, 15));
            return c;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                TelaFluxoCaixa tela = new TelaFluxoCaixa(null);
                tela.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}