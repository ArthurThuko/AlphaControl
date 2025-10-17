package alphacontrol;

import javax.swing.SwingUtilities;
import alphacontrol.view.TelaLogin;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}