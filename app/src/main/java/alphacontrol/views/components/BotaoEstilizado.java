package alphacontrol.views.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class BotaoEstilizado extends JButton {

    private Color corAtual;
    private final Color corBase;
    private final int raio = 30; // controla o arredondamento

    public BotaoEstilizado(String texto, Color corBase) {
        super(texto);

        this.corBase = corBase;
        this.corAtual = corBase;

        setFont(new Font("Georgia", Font.BOLD, 13));
        setForeground(Estilos.COR_TEXTO);

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                corAtual = Estilos.clarearCor(corBase, 0.15f);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                corAtual = corBase;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                corAtual = corBase.darker();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                corAtual = Estilos.clarearCor(corBase, 0.15f);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(corAtual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);

        g2.dispose();

        super.paintComponent(g);
    }
}
