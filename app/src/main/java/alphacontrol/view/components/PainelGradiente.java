package alphacontrol.view.components;

import javax.swing.*;
import java.awt.*;

public class PainelGradiente extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradiente = new GradientPaint(
                0, 0, Estilos.COR_FUNDO_CLARO,
                0, getHeight(), Estilos.COR_FUNDO_ESCURO
        );
        g2d.setPaint(gradiente);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}