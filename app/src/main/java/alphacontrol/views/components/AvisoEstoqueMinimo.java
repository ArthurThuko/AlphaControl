package alphacontrol.views.components;

import alphacontrol.models.Produto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AvisoEstoqueMinimo extends JPanel {

    private static final Color COR_AVISO_FUNDO = new Color(248, 215, 218);
    private static final Color COR_AVISO_BORDA = new Color(245, 198, 203);
    private static final Color COR_AVISO_TEXTO = new Color(114, 28, 36);

    private JLabel lblAviso;
    private JTextArea txtProdutos;
    private JScrollPane scrollPane;

    public AvisoEstoqueMinimo() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setBackground(COR_AVISO_FUNDO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_AVISO_BORDA, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        lblAviso = new JLabel();
        lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAviso.setForeground(COR_AVISO_TEXTO);
        lblAviso.setIcon(UIManager.getIcon("OptionPane.errorIcon")); 
        lblAviso.setIconTextGap(10);
        lblAviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtProdutos = new JTextArea();
        txtProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtProdutos.setForeground(COR_AVISO_TEXTO);
        txtProdutos.setBackground(COR_AVISO_FUNDO);
        txtProdutos.setEditable(false);
        txtProdutos.setOpaque(true);
        txtProdutos.setBorder(new EmptyBorder(10, 5, 0, 0)); 

        scrollPane = new JScrollPane(txtProdutos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(COR_AVISO_FUNDO);
        scrollPane.getViewport().setBackground(COR_AVISO_FUNDO);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(lblAviso);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(scrollPane);
        
        setVisible(false); 
        scrollPane.setVisible(false);
    }

    public void setAviso(List<Produto> produtosComEstoqueBaixo) {
        if (produtosComEstoqueBaixo == null || produtosComEstoqueBaixo.isEmpty()) {
            setVisible(false);
            scrollPane.setVisible(false);
        } else {
            int contagem = produtosComEstoqueBaixo.size();
            StringBuilder sb = new StringBuilder();
            
            if (contagem == 1) {
                lblAviso.setText("Atenção: 1 produto está com estoque baixo ou zerado.");
                Produto p = produtosComEstoqueBaixo.get(0);
                sb.append("• ").append(p.getNome())
                  .append(" (Estoque Atual: ").append(p.getQntEstoque())
                  .append(" | Mínimo: ").append(p.getQntMinima()).append(")");
            } else {
                lblAviso.setText("Atenção: " + contagem + " produtos estão com estoque baixo ou zerado.");
                
                int maximoProdutosExibidos = 5; 
                for (int i = 0; i < Math.min(contagem, maximoProdutosExibidos); i++) {
                    Produto p = produtosComEstoqueBaixo.get(i);
                    sb.append("• ").append(p.getNome())
                      .append(" (Estoque Atual: ").append(p.getQntEstoque())
                      .append(" | Mínimo: ").append(p.getQntMinima()).append(")\n");
                }
                
                if (contagem > maximoProdutosExibidos) {
                    sb.append("...e mais ").append(contagem - maximoProdutosExibidos).append(" outro(s).");
                }
            }
            
            txtProdutos.setText(sb.toString().trim());
            txtProdutos.setCaretPosition(0); 
            
            setVisible(true);
            scrollPane.setVisible(true);
        }
    }
}