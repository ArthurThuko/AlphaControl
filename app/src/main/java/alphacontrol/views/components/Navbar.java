package alphacontrol.views.components;

import alphacontrol.controllers.TelaPrincipalController;
import alphacontrol.views.estoque.TelaEstoque;
import alphacontrol.views.TelaPDV;
import alphacontrol.views.fluxo_caixa.TelaFluxoCaixa;
import alphacontrol.views.TelaRelatorios;
import alphacontrol.views.fiado.TelaFiado;
import alphacontrol.views.TelaPrincipal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        NavItem navPrincipal = new NavItem("Principal", "Principal".equals(activeItem));
        NavItem navEstoque = new NavItem("Estoque", "Estoque".equals(activeItem));
        NavItem navPDV = new NavItem("PDV", "PDV".equals(activeItem));
        NavItem navFluxoCaixa = new NavItem("Fluxo de Caixa", "Fluxo de Caixa".equals(activeItem));
        NavItem navRelatorio = new NavItem("Relatório", "Relatório".equals(activeItem));
        NavItem navFiado = new NavItem("Fiado", "Fiado".equals(activeItem));
        NavItem navSair = new NavItem("Sair", false);

        navPrincipal.addActionListener(e -> {
            if (!"Principal".equals(activeItem)) {
                new TelaPrincipal(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });

        navEstoque.addActionListener(e -> {
            if (!"Estoque".equals(activeItem)) {
                new TelaEstoque(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });

        navPDV.addActionListener(e -> {
            if (!"PDV".equals(activeItem)) {
                new TelaPDV(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });

        navFluxoCaixa.addActionListener(e -> {
            if (!"Fluxo de Caixa".equals(activeItem)) {
                new TelaFluxoCaixa(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });

        navRelatorio.addActionListener(e -> {
            if (!"Relatório".equals(activeItem)) {
                new TelaRelatorios(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });

        navFiado.addActionListener(e -> {
            if (!"Fiado".equals(activeItem)) {
                new TelaFiado(mainController).setVisible(true);
                currentScreen.dispose();
            }
        });
        
        navSair.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(
                currentScreen, 
                "Deseja realmente sair do AlphaControl?", 
                "Confirmar Saída", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        add(navPrincipal);
        add(navEstoque);
        add(navPDV);
        add(navFluxoCaixa);
        add(navRelatorio);
        add(navFiado);
        
        add(Box.createHorizontalGlue());
        
        add(navSair);
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