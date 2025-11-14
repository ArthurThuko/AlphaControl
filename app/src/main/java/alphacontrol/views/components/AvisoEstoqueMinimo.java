package alphacontrol.views.components;

import alphacontrol.models.Produto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AvisoEstoqueMinimo extends JPanel {

    private static final Color COR_AVISO_FUNDO = new Color(255, 243, 205);
    private static final Color COR_AVISO_BORDA = new Color(255, 229, 153);
    private static final Color COR_AVISO_TEXTO = new Color(133, 100, 4);

    private JLabel lblAviso;

    public AvisoEstoqueMinimo() {
        super(new BorderLayout());
        setBackground(COR_AVISO_FUNDO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_AVISO_BORDA, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        lblAviso = new JLabel();
        lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAviso.setForeground(COR_AVISO_TEXTO);
        lblAviso.setIcon(UIManager.getIcon("OptionPane.warningIcon")); 
        lblAviso.setIconTextGap(10);

        add(lblAviso, BorderLayout.CENTER);
        setVisible(false); 
    }

    public void setAviso(List<Produto> produtosComEstoqueBaixo) {
        if (produtosComEstoqueBaixo == null || produtosComEstoqueBaixo.isEmpty()) {
            setVisible(false);
        } else {
            int contagem = produtosComEstoqueBaixo.size();
            if (contagem == 1) {
                lblAviso.setText("Atenção: 1 produto está com estoque baixo ou zerado.");
            } else {
                lblAviso.setText("Atenção: " + contagem + " produtos estão com estoque baixo ou zerado.");
            }
            setVisible(true);
        }
    }
}