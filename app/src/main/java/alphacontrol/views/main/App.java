package alphacontrol.views.main;

import javax.swing.SwingUtilities;

import alphacontrol.views.login.TelaLogin;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}