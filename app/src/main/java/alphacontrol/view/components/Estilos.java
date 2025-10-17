package alphacontrol.view.components;

import java.awt.*;

public class Estilos {

    // Paleta principal
    public static final Color COR_FUNDO_CLARO = new Color(255, 250, 245);
    public static final Color COR_FUNDO_ESCURO = new Color(240, 225, 200);
    public static final Color COR_TEXTO = new Color(70, 50, 30);
    public static final Color COR_BORDA = new Color(220, 200, 170);
    public static final Color COR_BOTAO = new Color(195, 160, 130);

    // Fontes
    public static final Font FONTE_PADRAO = new Font("Georgia", Font.PLAIN, 13);
    public static final Font FONTE_TITULO = new Font("Georgia", Font.BOLD, 20);
    public static final Font FONTE_LABEL = new Font("Georgia", Font.BOLD, 14);

    // Métodos utilitários
    public static Color clarearCor(Color cor, float fator) {
        int r = cor.getRed();
        int g = cor.getGreen();
        int b = cor.getBlue();

        r += (int) ((255 - r) * fator);
        g += (int) ((255 - g) * fator);
        b += (int) ((255 - b) * fator);

        return new Color(r, g, b);
    }
}