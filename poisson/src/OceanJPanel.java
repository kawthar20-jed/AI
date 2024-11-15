import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;
import java.util.Observer;

public class OceanJPanel extends JPanel implements Observer, MouseListener {
    protected Ocean ocean;  // L'océan contenant les poissons et obstacles
    protected Timer timer;  // Timer pour la mise à jour périodique de l'océan

    public OceanJPanel() {
        this.setBackground(new Color(150, 255, 255));  // Couleur de fond de l'océan
        this.addMouseListener(this);  // Ajoute un écouteur pour détecter les clics
    }

    // Lancer la simulation : initialise l'océan et démarre le timer
    public void Lancer() {
        ocean = new Ocean(250, this.getWidth(), getHeight());  // Crée un océan avec 250 poissons
        ocean.addObserver(this);  // Enregistre le panneau comme observateur de l'océan
        
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                ocean.MiseAJourOcean();  // Met à jour l'océan à chaque tick du timer
            }
        };
        
        // Crée un timer pour mettre à jour l'océan toutes les 15 millisecondes
        timer = new Timer();
        timer.scheduleAtFixedRate(tache, 0, 15);
    }

    // Dessiner un poisson
    protected void DessinerPoisson(Poisson p, Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval((int) p.posX - 5, (int) p.posY - 5, 10, 10);
    }

    // Dessiner un prédateur
    protected void DessinerPredateur(Predateur p, Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) p.posX - 10, (int) p.posY - 10, 20, 20);
    }

    // Dessiner un obstacle
    protected void DessinerObstacle(ZoneAEviter o, Graphics g) {
        g.drawOval((int) (o.posX - o.rayon), (int) (o.posY - o.rayon), (int) o.rayon *
        
        2, (int) o.rayon * 2);
        }

    // Méthode de mise à jour de l'affichage
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner les poissons
        for (Poisson p : ocean.poissons) {
            if (!p.estMange) {
                DessinerPoisson(p, g);
            }
              // Afficher le score
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 16));
    g.drawString("Score: " + ocean.predateur.getScore(), 10, 20);
            }
            

        // Dessiner le prédateur
        DessinerPredateur(ocean.predateur, g);

        // Dessiner les obstacles
        for (ZoneAEviter obstacle : ocean.obstacles) {
            DessinerObstacle(obstacle, g);
        }
    }
   



    // Gestion des clics de souris
    @Override
    public void mouseClicked(MouseEvent e) {
        ocean.AjouterObstacle(e.getX(), e.getY(), 10);
        }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    // Méthode pour notifier l'interface quand l'océan a changé
    @Override
    public void update(Observable o, Object arg) {
        repaint();  // Rafraîchit l'affichage de l'océan
    }
  
    
}
