import java.util.ArrayList;

public class Predateur {
    protected double posX, posY;  // Position du prédateur
    protected double vitesseX, vitesseY;  // Vitesse du prédateur
    protected double angle;  // Angle de déplacement (direction)
    protected double vitesseMax;  // Vitesse maximale du prédateur
    // Ajoutez ce champ dans la classe Predateur
protected int score;  // Le score du prédateur (nombre de poissons mangés)


    // Constructeur pour initialiser un prédateur
    public int getScore() {
        return this.score;
    }
    
    public Predateur(double x, double y, double direction, double vitesseMax) {
        this.posX = x;
        this.posY = y;
        this.angle = direction;
        this.vitesseMax = vitesseMax;  // La vitesse maximale du prédateur
        this.vitesseX = Math.cos(angle) * vitesseMax;  // Vitesse en X
        this.vitesseY = Math.sin(angle) * vitesseMax;  // Vitesse en Y
        this.score = 0;  // Initialisation du score à 0

    }

    // Mise à jour de la position du prédateur
    public void MiseAJour(Poisson[] poissons, ArrayList<ZoneAEviter> obstacles, double largeur, double hauteur, ArrayList<Poisson> poissonsManges) {
        // Le prédateur se déplace vers le poisson le plus proche
        Poisson plusProche = trouverPoissonProche(poissons);

        // Calculer l'angle vers le poisson le plus proche
        if (plusProche != null) {
            double dx = plusProche.posX - this.posX;
            double dy = plusProche.posY - this.posY;
            this.angle = Math.atan2(dy, dx);  // Angle vers le poisson

            // Mise à jour de la vitesse en fonction de l'angle
            this.vitesseX = Math.cos(angle) * vitesseMax;
            this.vitesseY = Math.sin(angle) * vitesseMax;
        }

        // Déplacement du prédateur
        this.posX += vitesseX;
        this.posY += vitesseY;

        // Gestion des limites de l'océan (rebondir sur les bords)
        if (posX < 0) posX = largeur;
        if (posX > largeur) posX = 0;
        if (posY < 0) posY = hauteur;
        if (posY > hauteur) posY = 0;

        // Vérifier si le prédateur a mangé un poisson
        if (plusProche != null && Math.abs(posX - plusProche.posX) < 10 && Math.abs(posY - plusProche.posY) < 10) {
            poissonsManges.add(plusProche);  // Ajouter le poisson à la liste des poissons mangés
            // Marquer le poisson comme mangé
            plusProche.estMange = true;
        }
    }

    // Trouver le poisson le plus proche
    private Poisson trouverPoissonProche(Poisson[] poissons) {
        Poisson plusProche = null;
        double distanceMin = Double.MAX_VALUE;

        for (Poisson poisson : poissons) {
            // Vérifier si le poisson n'a pas déjà été mangé
            if (poisson.estMange) continue;

            if (plusProche != null && !plusProche.estMange && Math.abs(posX - plusProche.posX) < 10 && Math.abs(posY - plusProche.posY) < 10) {
                // Marquer le poisson comme mangé
                plusProche.estMange = true;
                this.score++;  // Incrémenter le score
            }
            // Calculer la distance au prédateur
            double distance = Math.sqrt(Math.pow(poisson.posX - this.posX, 2) + Math.pow(poisson.posY - this.posY, 2));
            if (distance < distanceMin) {
                distanceMin = distance;
                plusProche = poisson;

            }
        }
        return plusProche;
    }
    
}
