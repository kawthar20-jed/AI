import javax.swing.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulation Océan");
        OceanJPanel oceanPanel = new OceanJPanel();
        
        // Configure la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Taille de la fenêtre
        frame.add(oceanPanel);
        frame.setVisible(true);
        
        oceanPanel.Lancer();  // Lance la simulation
    }
}
