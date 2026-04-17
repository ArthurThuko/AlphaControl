package alphacontrol.views.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import alphacontrol.controllers.principal.TelaPrincipalController;

public class Navbar extends JMenuBar {

    private static final Color BEGE_CLARO = new Color(255, 250, 240);
    private static final Color MARROM_ESCURO = new Color(77, 51, 30);
    private static final Color MARROM_MEDIO = new Color(143, 97, 54);
    private static final Color MARROM_CLARO = new Color(184, 142, 106);
    private static final Color DOURADO_SUAVE = new Color(226, 180, 90);

    public Navbar(JFrame currentScreen, TelaPrincipalController mainController, String activeItem) {
        setBackground(MARROM_MEDIO);
        setBorder(new EmptyBorder(0, 5, 0, 5));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Criação dos itens de navegação
        NavItem navPrincipal = new NavItem("Principal (F1)", "Principal".equals(activeItem));
        NavItem navPDV = new NavItem("PDV (F3)", "PDV".equals(activeItem));
        NavItem navEstoque = new NavItem("Estoque (F2)", "Estoque".equals(activeItem));
        NavItem navFiado = new NavItem("Fiado/Cliente (F4)", "Fiado".equals(activeItem));
        NavItem navFluxoCaixa = new NavItem("Fluxo de Caixa (F5)", "Fluxo de Caixa".equals(activeItem));
        NavItem navRelatorio = new NavItem("Relatório (F6)", "Relatório".equals(activeItem));
        NavItem navSair = new NavItem("Sair", false);

        // Ações dos botões
        navPrincipal.addActionListener(e -> {
            if (!"Principal".equals(activeItem)) {
                mainController.abrirTelaPrincipal();
            }
        });

        navPDV.addActionListener(e -> {
            if (!"PDV".equals(activeItem)) {
                mainController.abrirTelaPDV();
            }
        });

        navEstoque.addActionListener(e -> {
            if (!"Estoque".equals(activeItem)) {
                mainController.abrirTelaEstoque();
            }
        });

        navFiado.addActionListener(e -> {
            if (!"Fiado".equals(activeItem)) {
                mainController.abrirTelaFiado();
            }
        });

        navFluxoCaixa.addActionListener(e -> {
            if (!"Fluxo de Caixa".equals(activeItem)) {
                mainController.abrirTelaFluxoCaixa();
            }
        });

        navRelatorio.addActionListener(e -> {
            if (!"Relatórios".equals(activeItem)) {
                mainController.abrirTelaRelatorios();
            }
        });

        navSair.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(
                    currentScreen,
                    "Deseja realmente sair do AlphaControl?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Adição dos itens na ordem desejada
        add(navPrincipal);
        add(navEstoque);
        add(navPDV);
        add(navFiado);
        add(navFluxoCaixa);
        add(navRelatorio);

        // Empurra o botão "Sair" para o canto direito
        add(Box.createHorizontalGlue());

        add(navSair);

        // Atalhos de teclado (F1 a F6)
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // F1 - Principal
        inputMap.put(KeyStroke.getKeyStroke("F1"), "goPrincipal");
        actionMap.put("goPrincipal", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"Principal".equals(activeItem)) {
                    mainController.abrirTelaPrincipal();
                }
            }
        });

        // F2 - Estoque
        inputMap.put(KeyStroke.getKeyStroke("F2"), "goEstoque");
        actionMap.put("goEstoque", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"Estoque".equals(activeItem)) {
                    mainController.abrirTelaEstoque();
                }
            }
        });

        // F3 - PDV
        inputMap.put(KeyStroke.getKeyStroke("F3"), "goPDV");
        actionMap.put("goPDV", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"PDV".equals(activeItem)) {
                    mainController.abrirTelaPDV();
                }
            }
        });

        // F4 - Fiado
        inputMap.put(KeyStroke.getKeyStroke("F4"), "goFiado");
        actionMap.put("goFiado", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"Fiado".equals(activeItem)) {
                    mainController.abrirTelaFiado();
                }
            }
        });

        // F5 - Fluxo de Caixa
        inputMap.put(KeyStroke.getKeyStroke("F5"), "goFluxo");
        actionMap.put("goFluxo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"Fluxo de Caixa".equals(activeItem)) {
                    mainController.abrirTelaFluxoCaixa();
                }
            }
        });

        // F6 - Relatórios
        inputMap.put(KeyStroke.getKeyStroke("F6"), "goRelatorio");
        actionMap.put("goRelatorio", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"Relatório".equals(activeItem)) {
                    mainController.abrirTelaRelatorios();
                }
            }
        });
    }

    class NavItem extends JButton {
        private boolean active;
        private final Color hoverColor = MARROM_CLARO;
        private final Color defaultColor = BEGE_CLARO;
        private final Color activeColor = DOURADO_SUAVE;

        public NavItem(String text, boolean active) {
            super(text);
            this.active = active;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 18, 10, 18));
            setBackground(MARROM_MEDIO);

            if (active) {
                setForeground(activeColor);
            } else {
                setForeground(defaultColor);
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!active) {
                        setForeground(DOURADO_SUAVE);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!active) {
                        setForeground(defaultColor);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (active) {
                g2.setColor(MARROM_ESCURO);
                g2.fillRect(0, 0, getWidth(), getHeight());
            } else if (getModel().isRollover()) {
                g2.setColor(hoverColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}