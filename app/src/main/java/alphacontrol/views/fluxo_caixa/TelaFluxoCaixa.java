package alphacontrol.views.fluxo_caixa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import java.util.List;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.views.components.Navbar; // Import adicionado

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

        // LINHA DA NAVBAR ADICIONADA AQUI
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
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(titulo, gbc);

        JPanel painelLateral = new JPanel(new GridBagLayout());
        painelLateral.setOpaque(false);

        JPanel painelEntradas = criarPainelEntradas();
        JPanel painelSaidas = criarPainelSaidas();

        JPanel painelDireita = new JPanel(new GridBagLayout());
        painelDireita.setOpaque(false);

        GridBagConstraints gbcDir = new GridBagConstraints();
        gbcDir.gridx = 0;
        gbcDir.fill = GridBagConstraints.BOTH;
        gbcDir.weightx = 1;
        gbcDir.insets = new Insets(0, 0, 20, 0);

        gbcDir.gridy = 0;
        gbcDir.weighty = 1;
        painelDireita.add(criarPainelSaldo(), gbcDir);

        GridBagConstraints gbcPainel = new GridBagConstraints();
        gbcPainel.gridy = 0;
        gbcPainel.fill = GridBagConstraints.BOTH;
        gbcPainel.weighty = 1;
        gbcPainel.insets = new Insets(0, 15, 0, 15);

        gbcPainel.gridx = 0;
        gbcPainel.weightx = 0.4;
        painelLateral.add(painelEntradas, gbcPainel);

        gbcPainel.gridx = 1;
        gbcPainel.weightx = 0.4;
        painelLateral.add(painelSaidas, gbcPainel);

        gbcPainel.gridx = 2;
        gbcPainel.weightx = 0.2;
        painelLateral.add(painelDireita, gbcPainel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        painelPrincipal.add(painelLateral, gbc);

        add(painelPrincipal);

        atualizarSaldo();
    }

    private JPanel criarPainelEntradas() {
        JPanel painel = new RoundedPanel(25, VERDE_CLARO, VERDE_BORDA);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 10, 30, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Entradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
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
        scroll.setBorder(BorderFactory.createLineBorder(VERDE_BORDA));
        scroll.getViewport().setBackground(VERDE_CLARO);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        lblTotalEntradas = new JLabel("Total: R$ " + calcularTotal(modelo));
        lblTotalEntradas.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalEntradas.setForeground(MARROM_ESCURO);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblTotalEntradas, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Entrada", VERDE_BOTAO, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalEntrada modal = new ModalEntrada(this);
            modal.setVisible(true);
            recarregarTabelaEntradas();
        });
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaidas() {
        Color VERMELHO_CLARO = new Color(236, 204, 200);
        Color VERMELHO_BORDA = new Color(178, 67, 62);
        Color VERMELHO_BOTAO = new Color(178, 67, 62);

        JPanel painel = new RoundedPanel(25, VERMELHO_CLARO, VERMELHO_BORDA);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 10, 30, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Saídas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(MARROM_ESCURO);
        gbc.gridy = 0;
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
        scroll.setBorder(BorderFactory.createLineBorder(VERMELHO_BORDA));
        scroll.getViewport().setBackground(VERMELHO_CLARO);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        lblTotalSaidas = new JLabel("Total: R$ " + calcularTotal(modelo));
        lblTotalSaidas.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalSaidas.setForeground(MARROM_ESCURO);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblTotalSaidas, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Saída", VERMELHO_BOTAO, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalSaida modal = new ModalSaida(this);
            modal.setVisible(true);
            recarregarTabelaSaidas();
        });
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaldo() {
        JPanel painel = new RoundedPanel(25, new Color(210, 225, 245), new Color(85, 120, 170));
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Saldo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(40, 60, 90));
        gbc.gridy = 0;
        painel.add(lblTitulo, gbc);

        lblSaldo = new JLabel("R$ 0,00", SwingConstants.CENTER);
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblSaldo.setForeground(new Color(30, 60, 110));
        gbc.gridy = 1;
        painel.add(lblSaldo, gbc);

        return painel;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(45);
        tabela.setBackground(VERDE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(VERDE_BORDA);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 5));
        tabela.setSelectionBackground(VERDE_BORDA.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(VERDE_BORDA);
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        header.setReorderingAllowed(false);
        
        PaddedCellRenderer textRenderer = new PaddedCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        PaddedCellRenderer valueRenderer = new PaddedCellRenderer();
        valueRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        PaddedCellRenderer dateRenderer = new PaddedCellRenderer();
        dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        ActionCellRenderer actionRenderer = new ActionCellRenderer();

        tabela.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabela.getColumnModel().getColumn(1).setCellRenderer(valueRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(3).setMaxWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(4).setMaxWidth(100);
    }

    private void configurarTabelaSaidas(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(45);
        tabela.setBackground(new Color(236, 204, 200));
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(178, 67, 62));
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 5));
        tabela.setSelectionBackground(new Color(214, 160, 160));
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(178, 67, 62));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        header.setReorderingAllowed(false);
        
        PaddedCellRenderer textRenderer = new PaddedCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        PaddedCellRenderer valueRenderer = new PaddedCellRenderer();
        valueRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        PaddedCellRenderer dateRenderer = new PaddedCellRenderer();
        dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        ActionCellRenderer actionRenderer = new ActionCellRenderer();

        tabela.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabela.getColumnModel().getColumn(1).setCellRenderer(valueRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(3).setMaxWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
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
                totalEntradas = Double.parseDouble(
                        lblTotalEntradas.getText()
                                .replace("Total: R$ ", "")
                                .replace(".", "")
                                .replace(",", "."));
            }
            if (lblTotalSaidas != null) {
                totalSaidas = Double.parseDouble(
                        lblTotalSaidas.getText()
                                .replace("Total: R$ ", "")
                                .replace(".", "")
                                .replace(",", "."));
            }
        } catch (NumberFormatException e) {
            totalEntradas = 0;
            totalSaidas = 0;
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

        lblTotalEntradas.setText("Total: R$ " + calcularTotal(modelo));
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

        lblTotalSaidas.setText("Total: R$ " + calcularTotal(modelo));
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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