package alphacontrol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BotaoEstilizado extends JButton {

    public BotaoEstilizado(String texto, Color corBase) {
        super(texto);
        setFont(new Font("Georgia", Font.BOLD, 13));
        setForeground(Estilos.COR_TEXTO);
        setBackground(corBase);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Estilos.clarearCor(corBase, 0.15f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(corBase);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(corBase.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Estilos.clarearCor(corBase, 0.15f));
            }
        });
    }
}