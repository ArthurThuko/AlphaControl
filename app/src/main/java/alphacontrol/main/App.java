package alphacontrol.main;

import javax.swing.SwingUtilities;

import alphacontrol.dao.DatabaseInitializer;
import alphacontrol.views.login.TelaLogin;

public class App {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "0.85");

        DatabaseInitializer.inicializarBanco(); 

        SwingUtilities.invokeLater(() -> {
            try {
                new TelaLogin().setVisible(true); 
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}