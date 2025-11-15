package alphacontrol.views.fluxo_caixa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import java.util.List;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.controllers.principal.TelaPrincipalController;
import alphacontrol.models.MovimentacaoCaixa;
import alphacontrol.views.components.Navbar; 
import alphacontrol.views.fluxo_caixa.ModalEntrada;
import alphacontrol.views.fluxo_caixa.ModalSaida; 

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class TelaFluxoCaixa extends JFrame {

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64); 
    private static final Color VERMELHO_TERROSO = new Color(178, 67, 62); 
    private static final Color AZUL_ACAO = new Color(0, 100, 200);

    private JTable tabelaEntradas;
    private JTable tabelaSaidas;
    private JLabel lblTotalEntradas;
    private JLabel lblTotalSaidas;
    private JLabel lblSaldo;
    
    private JComboBox<String> cmbMes;
    private JTextField txtAno;
    
    private FluxoCaixaController controller;
    private TelaPrincipalController mainController;


    public TelaFluxoCaixa(TelaPrincipalController mainController) {
          this.mainController = mainController;
        this.controller = mainController.getFluxoCaixaController();
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Fluxo de Caixa", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(MARROM_ESCURO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(titulo, gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0; 
        gbc.insets = new Insets(0, 0, 20, 0);
        painelPrincipal.add(criarPainelFiltro(), gbc);
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0; 
        gbc.insets = new Insets(0, 0, 30, 0);
        painelPrincipal.add(criarPainelResumo(), gbc);
        
        JPanel painelTabelas = new JPanel(new GridBagLayout());
        painelTabelas.setOpaque(false);

        JPanel painelEntradas = criarPainelEntradas();
        JPanel painelSaidas = criarPainelSaidas();

        GridBagConstraints gbcTabelas = new GridBagConstraints();
        gbcTabelas.gridy = 0;
        gbcTabelas.fill = GridBagConstraints.BOTH; 
        gbcTabelas.weighty = 1.0; 
        gbcTabelas.insets = new Insets(0, 15, 0, 15);

        gbcTabelas.gridx = 0;
        gbcTabelas.weightx = 0.5; 
        painelTabelas.add(painelEntradas, gbcTabelas);

        gbcTabelas.gridx = 1;
        gbcTabelas.weightx = 0.5; 
        painelTabelas.add(painelSaidas, gbcTabelas);

        gbc.gridx = 0;
        gbc.gridy = 3; 
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(0, 0, 0, 0);
        painelPrincipal.add(painelTabelas, gbc);

        add(painelPrincipal);

        atualizarTudo();
    }
    
    private JPanel criarPainelFiltro() {
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelFiltro.setOpaque(false);
        
        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMes.setForeground(MARROM_ESCURO);
        
        String[] meses = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        cmbMes = new JComboBox<>(meses);
        cmbMes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cmbMes.setPreferredSize(new Dimension(150, 40));
        cmbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAno.setForeground(MARROM_ESCURO);
        
        txtAno = new JTextField(String.valueOf(LocalDate.now().getYear()));
        txtAno.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtAno.setPreferredSize(new Dimension(80, 40));
        
        JButton btnFiltrar = new RoundedButton("Filtrar", MARROM_MEDIO, Color.WHITE, 120, 40);
        btnFiltrar.addActionListener(e -> atualizarTudo());
        
        painelFiltro.add(lblMes);
        painelFiltro.add(cmbMes);
        painelFiltro.add(lblAno);
        painelFiltro.add(txtAno);
        painelFiltro.add(btnFiltrar);
        
        return painelFiltro;
    }

    private JPanel criarPainelResumo() {
        JPanel painelResumo = new RoundedPanel(15);
        painelResumo.setBackground(BEGE_CLARO);
        painelResumo.setLayout(new GridBagLayout());
        painelResumo.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0 / 3.0;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTituloEntradas = new JLabel("Total Entradas", SwingConstants.CENTER);
        lblTituloEntradas.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloEntradas.setForeground(MARROM_ESCURO);
        painelResumo.add(lblTituloEntradas, gbc);

        gbc.gridy = 1;
        lblTotalEntradas = new JLabel("R$ 0,00");
        lblTotalEntradas.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotalEntradas.setForeground(VERDE_OLIVA);
        lblTotalEntradas.setHorizontalAlignment(SwingConstants.CENTER);
        painelResumo.add(lblTotalEntradas, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel lblTituloSaidas = new JLabel("Total Saídas", SwingConstants.CENTER);
        lblTituloSaidas.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloSaidas.setForeground(MARROM_ESCURO);
        painelResumo.add(lblTituloSaidas, gbc);

        gbc.gridy = 1;
        lblTotalSaidas = new JLabel("R$ 0,00");
        lblTotalSaidas.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotalSaidas.setForeground(VERMELHO_TERROSO);
        lblTotalSaidas.setHorizontalAlignment(SwingConstants.CENTER);
        painelResumo.add(lblTotalSaidas, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        JLabel lblTituloSaldo = new JLabel("Saldo Atual", SwingConstants.CENTER);
        lblTituloSaldo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloSaldo.setForeground(MARROM_ESCURO);
        painelResumo.add(lblTituloSaldo, gbc);

        gbc.gridy = 1;
        lblSaldo = new JLabel("R$ 0,00");
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSaldo.setForeground(MARROM_ESCURO); 
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
        painelResumo.add(lblSaldo, gbc);

        return painelResumo;
    }

    private JPanel criarPainelEntradas() {
        JPanel painel = new RoundedPanel(15);
        painel.setBackground(BEGE_CLARO);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0; 
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
                
                int mes = cmbMes.getSelectedIndex() + 1;
                int ano = Integer.parseInt(txtAno.getText());
                List<MovimentacaoCaixa> lista = controller.listarEntradas(mes, ano);

                if (coluna == 3) {
                    MovimentacaoCaixa mov = lista.get(linha);
                    ModalEntrada modal = new ModalEntrada(TelaFluxoCaixa.this, mov, controller);
                    modal.setVisible(true);
                    atualizarTudo();
                } else if (coluna == 4) {
                    int confirm = JOptionPane.showConfirmDialog(
                            TelaFluxoCaixa.this,
                            "Tem certeza que deseja excluir esta entrada?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        MovimentacaoCaixa mov = lista.get(linha);
                        controller.removerMovimentacao(mov.getId());
                        atualizarTudo();
                    }
                }
            }
        });

        configurarTabela(tabelaEntradas, VERDE_OLIVA);
        
        gbc.gridy = 1;
        gbc.weighty = 0; 
        gbc.insets = new Insets(10, 10, 0, 10);
        painel.add(tabelaEntradas.getTableHeader(), gbc);

        JScrollPane scroll = new JScrollPane(tabelaEntradas);
        scroll.setColumnHeaderView(null); 
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        gbc.gridy = 2;
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Entrada", VERDE_OLIVA, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalEntrada modal = new ModalEntrada(this, controller);
            modal.setVisible(true);
            atualizarTudo();
        });
        gbc.gridy = 3;
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private JPanel criarPainelSaidas() {
        JPanel painel = new RoundedPanel(15);
        painel.setBackground(BEGE_CLARO);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0; 
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

                int mes = cmbMes.getSelectedIndex() + 1;
                int ano = Integer.parseInt(txtAno.getText());
                List<MovimentacaoCaixa> lista = controller.listarSaidas(mes, ano);

                if (coluna == 3) {
                    MovimentacaoCaixa mov = lista.get(linha);
                    ModalSaida modal = new ModalSaida(TelaFluxoCaixa.this, mov, controller);
                    modal.setVisible(true);
                    atualizarTudo();
                } else if (coluna == 4) {
                    int confirm = JOptionPane.showConfirmDialog(
                            TelaFluxoCaixa.this,
                            "Tem certeza que deseja excluir esta saída?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        MovimentacaoCaixa mov = lista.get(linha);
                        controller.removerMovimentacao(mov.getId());
                        atualizarTudo();
                    }
                }
            }
        });

        configurarTabela(tabelaSaidas, VERMELHO_TERROSO);

        gbc.gridy = 1;
        gbc.weighty = 0; 
        gbc.insets = new Insets(10, 10, 0, 10);
        painel.add(tabelaSaidas.getTableHeader(), gbc);

        JScrollPane scroll = new JScrollPane(tabelaSaidas);
        scroll.setColumnHeaderView(null);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BEGE_CLARO);

        gbc.gridy = 2;
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scroll, gbc);

        JButton btnAdd = new RoundedButton("Adicionar Saída", VERMELHO_TERROSO, Color.WHITE, 220, 45);
        btnAdd.addActionListener(e -> {
            ModalSaida modal = new ModalSaida(this, controller);
            modal.setVisible(true);
            atualizarTudo();
        });
        gbc.gridy = 3;
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnAdd, gbc);

        return painel;
    }

    private void configurarTabela(JTable tabela, Color headerColor) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabela.setRowHeight(60);
        tabela.setBackground(BEGE_CLARO);
        tabela.setForeground(MARROM_ESCURO);
        tabela.setGridColor(new Color(223, 214, 198));
        tabela.setShowGrid(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setSelectionBackground(MARROM_CLARO.brighter());
        tabela.setSelectionForeground(MARROM_ESCURO);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(headerColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer(tabela));
        
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
    
    private void atualizarTudo() {
        int mes = cmbMes.getSelectedIndex() + 1;
        int ano;
        try {
            ano = Integer.parseInt(txtAno.getText());
            if (ano < 2000 || ano > 3000) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano inválido. Insira um ano válido (ex: 2024).", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            txtAno.setText(String.valueOf(LocalDate.now().getYear()));
            ano = LocalDate.now().getYear();
        }
        
        recarregarTabelaEntradas(mes, ano);
        recarregarTabelaSaidas(mes, ano);
        atualizarSaldo(mes, ano);
    }

    private void atualizarSaldo(int mes, int ano) {
        DecimalFormat formatador = new DecimalFormat("R$ #,##0.00");
        
        double[] totais = controller.calcularTotais(mes, ano);
        double totalEntradas = totais[0];
        double totalSaidas = totais[1];
        double saldo = totalEntradas - totalSaidas;
        
        lblTotalEntradas.setText(formatador.format(totalEntradas));
        lblTotalSaidas.setText(formatador.format(totalSaidas));
        lblSaldo.setText(formatador.format(saldo));
        
        if (saldo < 0) {
            lblSaldo.setForeground(VERMELHO_TERROSO);
        } else if (saldo > 0) {
            lblSaldo.setForeground(VERDE_OLIVA);
        } else {
            lblSaldo.setForeground(MARROM_ESCURO);
        }
    }

    private void recarregarTabelaEntradas(int mes, int ano) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaEntradas.getModel();
        modelo.setRowCount(0);

        List<MovimentacaoCaixa> entradas = controller.listarEntradas(mes, ano);
        for (MovimentacaoCaixa m : entradas) {
            modelo.addRow(new Object[] {
                    m.getNome(),
                    String.format("%.2f", m.getValor()),
                    m.getData(),
                    "Editar",
                    "Excluir"
            });
        }
    }

    private void recarregarTabelaSaidas(int mes, int ano) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaSaidas.getModel();
        modelo.setRowCount(0);

        List<MovimentacaoCaixa> saidas = controller.listarSaidas(mes, ano);
        for (MovimentacaoCaixa m : saidas) {
            modelo.addRow(new Object[] {
                    m.getNome(),
                    String.format("%.2f", m.getValor()),
                    m.getData(),
                    "Editar",
                    "Excluir"
            });
        }
    }
    
    private void atualizarSaldo() {
        atualizarTudo();
    }
    
    private void recarregarTabelaEntradas() {
        atualizarTudo();
    }
    
    private void recarregarTabelaSaidas() {
        atualizarTudo();
    }

    static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            graphics.setColor(MARROM_CLARO);
            graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }

    static class RoundedButton extends JButton {
        private final Color backgroundColor, hoverColor;

        public RoundedButton(String text, Color bg, Color fg, int w, int h) {
            super(text);
            backgroundColor = bg;
            hoverColor = bg.brighter();
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setPreferredSize(new Dimension(w, h));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(backgroundColor);
            }
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            g2.dispose();
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

            if (value != null) {
                ((JComponent) c).setToolTipText(value.toString());
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
            
            ((JComponent) c).setToolTipText(value.toString());
            
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