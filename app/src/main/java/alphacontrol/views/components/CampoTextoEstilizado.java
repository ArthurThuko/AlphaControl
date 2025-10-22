package alphacontrol.views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CampoTextoEstilizado extends JTextField {

    public CampoTextoEstilizado(int colunas) {
        super(colunas);
        configurarEstilo();
    }

    private void configurarEstilo() {
        setFont(Estilos.FONTE_PADRAO);
        setBackground(Estilos.COR_FUNDO_CLARO);
        setForeground(Estilos.COR_TEXTO);
        setCaretColor(new Color(120, 90, 60));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_BORDA, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 140, 100), 2, true),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Estilos.COR_BORDA, 1, true),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
            }
        });
    }
}