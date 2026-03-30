package alphacontrol.views.fluxo_caixa;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;

import alphacontrol.controllers.fluxo.FluxoCaixaController;
import alphacontrol.models.MovimentacaoCaixa;

public class ModalEntrada extends JDialog {

    private Point mouseClickPoint;
    private FluxoCaixaController controller;
    private JTextField txtNome, txtData, txtValor;
    private MovimentacaoCaixa movimentacaoEditando;

    private static final Color BEGE_FUNDO = new Color(247, 239, 224);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color VERDE_OLIVA = new Color(101, 125, 64);

    public ModalEntrada(JFrame parent, FluxoCaixaController controller) {
        this(parent, null, controller);
    }

    public ModalEntrada(JFrame parent, MovimentacaoCaixa mov, FluxoCaixaController controller) {
        super(parent, true);
        this.movimentacaoEditando = mov;
        this.controller = controller;
        setTitle(mov == null ? "Adicionar Entrada" : "Editar Entrada");

        JPanel painel = criarPainelPrincipal();
        add(painel);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        if (mov != null) {
            txtNome.setText(mov.getNome());
            
            // Caso a data venha do banco sem máscara (ex: 23032004), força a máscara
            String dataBanco = mov.getData();
            if (dataBanco != null && dataBanco.length() == 8 && !dataBanco.contains("/")) {
                dataBanco = dataBanco.substring(0, 2) + "/" + dataBanco.substring(2, 4) + "/" + dataBanco.substring(4);
            }
            txtData.setText(dataBanco);
            
            txtValor.setText(String.format("%.2f", mov.getValor()).replace(",", "."));
        } else {
            // Garante que o valor comece como 0.00 ao adicionar nova entrada
            txtValor.setText("0.00");
        }
    }

    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fill(new RoundRectangle2D.Double(8, 8, getWidth() - 8, getHeight() - 8, 30, 30));

                g2.setColor(BEGE_FUNDO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 8, getHeight() - 8, 30, 30));
                g2.dispose();
            }
        };
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(0, 0, 0, 0));

        painel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });
        painel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = e.getLocationOnScreen();
                setLocation(p.x - mouseClickPoint.x, p.y - mouseClickPoint.y);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel(movimentacaoEditando == null ? "Adicionar Entrada" : "Editar Entrada",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(MARROM_ESCURO);
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // 1. Campo Nome
        txtNome = criarCampo("Nome:", painel, gbc, null);

        // 2. Campo Data com MaskFormatter
        MaskFormatter maskData = null;
        try {
            maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtData = criarCampo("Data (dd/mm/aaaa):", painel, gbc, maskData);

        // Joga o cursor para o início do campo Data se ele estiver vazio
        txtData.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (txtData.getText().replace("_", "").replace("/", "").trim().isEmpty()) {
                        txtData.setCaretPosition(0);
                    }
                });
            }
        });

        // 3. Campo Valor com DocumentFilter
        txtValor = criarCampo("Valor (R$):", painel, gbc, null);
        aplicarFiltroMoeda(txtValor);

        txtValor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> txtValor.selectAll());
            }
        });

        JButton btnSalvar = criarBotao("Salvar", VERDE_OLIVA, Color.WHITE, e -> salvarEntrada());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton btnFechar = criarBotao("Fechar", MARROM_MEDIO, Color.WHITE, e -> dispose());
        painel.add(btnFechar, gbc);

        return painel;
    }

    private JTextField criarCampo(String label, JPanel painel, GridBagConstraints gbc, MaskFormatter mascara) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label, SwingConstants.RIGHT);
        lbl.setForeground(MARROM_ESCURO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        JTextField campo;
        
        if (mascara != null) {
            campo = new JFormattedTextField(mascara) {
                @Override
                protected void paintComponent(Graphics g) {
                    desenharFundoArredondado(g, this);
                    super.paintComponent(g);
                }
            };
        } else {
            campo = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    desenharFundoArredondado(g, this);
                    super.paintComponent(g);
                }
            };
        }

        campo.setOpaque(false);
        campo.setForeground(MARROM_ESCURO);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        painel.add(campo, gbc);
        gbc.gridy++;
        return campo;
    }

    private void desenharFundoArredondado(Graphics g, JComponent componente) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BEGE_CLARO);
        g2.fill(new RoundRectangle2D.Float(0, 0, componente.getWidth() - 1, componente.getHeight() - 1, 15, 15));
        g2.setColor(MARROM_CLARO);
        g2.draw(new RoundRectangle2D.Float(0, 0, componente.getWidth() - 1, componente.getHeight() - 1, 15, 15));
        g2.dispose();
    }

    private void aplicarFiltroMoeda(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String futuroTexto = textoAtual.substring(0, offset) + string + textoAtual.substring(offset);
                
                if (futuroTexto.matches("\\d*([.,]\\d{0,2})?")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String futuroTexto = textoAtual.substring(0, offset) + text + textoAtual.substring(offset + length);
                
                if (futuroTexto.matches("\\d*([.,]\\d{0,2})?")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? corFundo.brighter() : corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private void salvarEntrada() {
        String nome = txtNome.getText().trim();
        // Pegando o texto direto, ele já vem no formato "23/03/2004"
        String data = txtData.getText().trim(); 
        String valorStr = txtValor.getText().trim();

        // Se a string da data ainda contém "_" significa que não foi totalmente preenchida
        if (nome.isEmpty() || data.contains("_") || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr.replace(",", "."));

            if (movimentacaoEditando == null) {
                // Aqui o controller já recebe a data no modelo "23/03/2004"
                controller.adicionarEntrada(nome, valor, data);
                JOptionPane.showMessageDialog(this, "Entrada adicionada com sucesso!");
            } else {
                controller.atualizarMovimentacao(
                        movimentacaoEditando.getId(),
                        "entrada",
                        nome,
                        valor,
                        data);
                JOptionPane.showMessageDialog(this, "Entrada atualizada com sucesso!");
            }

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Use ponto ou vírgula para decimais.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}