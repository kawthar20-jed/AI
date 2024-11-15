import java.util.ArrayList;
import java.util.Observable;

public class Ocean extends Observable {
    protected Poisson[] poissons;
    protected ArrayList<ZoneAEviter> obstacles;
    protected Predateur predateur;
    protected double largeur;
    protected double hauteur;

    // Constructeur pour initialiser l'océan avec un certain nombre de poissons et un prédateur
    public Ocean(int _nbPoissons, double _largeur, double _hauteur) {
        largeur = _largeur;
        hauteur = _hauteur;
        obstacles = new ArrayList<>();
        poissons = new Poisson[_nbPoissons];
         
       
        // Initialisation des poissons
        for (int i = 0; i < _nbPoissons; i++) {
            poissons[i] = new Poisson(Math.random() * largeur, Math.random() * hauteur, Math.random() * 2 * Math.PI);
        }
        

        // Initialisation du prédateur
        predateur = new Predateur(Math.random() * largeur, Math.random() * hauteur, Math.random() * 2 * Math.PI, 2.0);
    }

    // Mise à jour de l'océan : déplace les poissons et le prédateur
    public void MiseAJourOcean() {
        ArrayList<Poisson> poissonsManges = new ArrayList<>();

        // Mise à jour des poissons
        for (Poisson p : poissons) {
            if (!p.estMange) {
                p.MiseAJour(poissons, obstacles, largeur, hauteur);
            }
        }

        // Mise à jour du prédateur
        predateur.MiseAJour(poissons, obstacles, largeur, hauteur, poissonsManges);

        // Retirer les poissons mangés de la simulation
        for (Poisson p : poissonsManges) {
            p.estMange = true;
        }

        setChanged();
        notifyObservers();  // Notifie l'interface pour mettre à jour l'affichage
        
    }

    public void AjouterObstacle(double _posX, double _posY, double rayon) {
        obstacles.add(new ZoneAEviter(_posX, _posY, rayon));
        }
        protected void MiseAJourObstacles() {
        for (ZoneAEviter obstacle : obstacles) {
        obstacle.MiseAJour();
        }
        obstacles.removeIf(o -> o.estMort());
        }
        protected void MiseAJourPoissons() {
            for (Poisson p : poissons) {
            p.MiseAJour(poissons, obstacles, largeur, hauteur);
            }
            }
        }
