package alphacontrol.views.components;

import javax.swing.*;
import java.awt.*;

public class CampoSenhaEstilizado extends JPasswordField {

    public CampoSenhaEstilizado(int columns) {
        super(columns);
        setFont(Estilos.FONTE_PADRAO);
        setForeground(Estilos.COR_TEXTO);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 180, 150), 2, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setEchoChar('•');
    }
}