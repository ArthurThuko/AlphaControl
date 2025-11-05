package alphacontrol.views.fiado;

import alphacontrol.models.Cliente;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;

public class ModalEditarFiado extends JDialog {
    
    private final JTextField txtNome;
    private final JFormattedTextField txtCpf;
    private final JFormattedTextField txtTelefone;
    private final JFormattedTextField txtCep;
    private final JTextField txtRua;
    private final JTextField txtBairro;
    private final JTextField txtNumero;
    
    private final JButton btnSalvar;
    private final JLabel titulo;
    private Cliente cliente;

    private Point mouseClickPoint;

    public ModalEditarFiado(JFrame parent, Cliente cliente) {
        super(parent, "Editar Cliente", true);
        this.cliente = cliente;

        Color begeFundo = new Color(247, 239, 224);
        Color marromEscuro = new Color(77, 51, 30);
        Color marromMedio = new Color(143, 97, 54);
        Color marromClaro = new Color(184, 142, 106);
        Color begeClaro = new Color(255, 250, 240);

        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(begeFundo);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
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

        titulo = new JLabel("Editar Cliente", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(marromEscuro);
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        painel.add(criarLabel("Nome:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtNome = criarCampoTexto(begeClaro, marromClaro, marromEscuro);
        painel.add(txtNome, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("CPF:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtCpf = criarCampoFormatado(begeClaro, marromClaro, marromEscuro, createFormatter("###.###.###-##"));
        painel.add(txtCpf, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Telefone:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtTelefone = criarCampoFormatado(begeClaro, marromClaro, marromEscuro, createFormatter("(##) #####-####"));
        painel.add(txtTelefone, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("CEP:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtCep = criarCampoFormatado(begeClaro, marromClaro, marromEscuro, createFormatter("#####-###"));
        painel.add(txtCep, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Rua:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtRua = criarCampoTexto(begeClaro, marromClaro, marromEscuro);
        painel.add(txtRua, gbc);
        gbc.gridy++;
        
        gbc.gridx = 0;
        painel.add(criarLabel("Bairro:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtBairro = criarCampoTexto(begeClaro, marromClaro, marromEscuro);
        painel.add(txtBairro, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        painel.add(criarLabel("Número:", marromEscuro), gbc);
        gbc.gridx = 1;
        txtNumero = criarCampoTexto(begeClaro, marromClaro, marromEscuro);
        painel.add(txtNumero, gbc);
        gbc.gridy++;

        btnSalvar = new JButton("Salvar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? marromEscuro : marromMedio);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSalvar.setForeground(begeClaro);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);
        gbc.gridy++;

        JButton botaoFechar = new JButton("Fechar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? marromClaro.darker() : marromClaro);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        botaoFechar.setForeground(marromEscuro);
        botaoFechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setContentAreaFilled(false);
        botaoFechar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoFechar, gbc);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        add(painel);
        setSize(500, 750); 
        setLocationRelativeTo(parent);
        setResizable(false);
        
        preencherCampos();
    }
    
    private void preencherCampos() {
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtCep.setText(cliente.getCep());
        txtRua.setText(cliente.getRua());
        txtBairro.setText(cliente.getBairro());
        txtNumero.setText(cliente.getNumeroCasa());
    }
    
    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter(' ');
            formatter.setValueContainsLiteralCharacters(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    private JFormattedTextField criarCampoFormatado(Color fundo, Color borda, Color texto, MaskFormatter formatter) {
        JFormattedTextField campo = new JFormattedTextField(formatter) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setForeground(texto);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return campo;
    }
    
    private JTextField criarCampoTexto(Color fundo, Color borda, Color texto) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setForeground(texto);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return campo;
    }
    
    private JLabel criarLabel(String texto, Color cor) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setForeground(cor);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return lbl;
    }
    
    private String getUnmaskedText(JFormattedTextField field) {
        Object value = field.getValue();
        if (value != null) {
            return value.toString().replaceAll("[^0-9]", "");
        }
        return field.getText().replaceAll("[^0-9]", "");
    }

    public boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("O campo Nome é obrigatório.");
            txtNome.requestFocus();
            return false;
        }
        
        if (getUnmaskedText(txtCpf).length() != 11) {
            mostrarErro("O CPF está incompleto. Deve conter 11 dígitos.");
            txtCpf.requestFocus();
            return false;
        }
        
        if (getUnmaskedText(txtTelefone).length() < 10) {
            mostrarErro("O Telefone está incompleto.");
            txtTelefone.requestFocus();
            return false;
        }
        
        if (getUnmaskedText(txtCep).length() != 8) {
            mostrarErro("O CEP está incompleto. Deve conter 8 dígitos.");
            txtCep.requestFocus();
            return false;
        }

        if (txtNumero.getText().trim().isEmpty()) {
            mostrarErro("O campo Número é obrigatório.");
            txtNumero.requestFocus();
            return false;
        }

        return true;
    }

    public JButton getBtnSalvar() { 
        return btnSalvar; 
    }
    
    public Cliente getClienteOriginal() {
        return this.cliente;
    }

    public Cliente getDadosEditadosDosCampos() throws ParseException {
        return new Cliente(
            txtNome.getText(),
            txtCpf.getText(),
            txtTelefone.getText(),
            txtCep.getText(),
            txtRua.getText(),
            txtBairro.getText(),
            txtNumero.getText()
        );
    }

    public void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.WARNING_MESSAGE);
    }
}