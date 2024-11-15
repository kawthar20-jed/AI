import java.util.ArrayList;

public class Poisson extends Objet {
    // Constantes
    public static final double PAS = 3;
    public static final double DISTANCE_MIN_CARRE = 25; // distance au carré
    public static final double DISTANCE_MAX_CARRE = 1600; // distance au carré
    public static final double DISTANCE_MIN = 5;  // distance minimum
    public static final double DISTANCE_MAX = 40; // distance maximum
    public boolean estMange = false;  // Marque si le poisson a été mangé


    // Attributs
    protected double vitesseX;
    protected double vitesseY;

    // Constructeur : initialisation de la position et de la direction
    public Poisson(double _x, double _y, double _dir) {
        posX = _x;
        posY = _y;
        vitesseX = Math.cos(_dir);  // Calcul de la vitesse sur l'axe X à partir de l'angle
        vitesseY = Math.sin(_dir);  // Calcul de la vitesse sur l'axe Y à partir de l'angle
    }

    // Getter pour la vitesse sur X
    public double getVitesseX() {
        return vitesseX;
    }

    // Getter pour la vitesse sur Y
    public double getVitesseY() {
        return vitesseY;
    }

    // Mise à jour de la position du poisson en fonction de sa vitesse
    protected void MiseAJourPosition() {
        posX += PAS * vitesseX;
        posY += PAS * vitesseY;
    }

    // Vérifie si le poisson est dans un alignement avec un autre poisson
    protected boolean DansAlignement(Poisson p) {
        double distanceCarre = DistanceCarre(p);
        return (distanceCarre < DISTANCE_MAX_CARRE && distanceCarre > DISTANCE_MIN_CARRE);
    }

    // Calcule la distance au mur le plus proche
    protected double DistanceAuMur(double murXMin, double murYMin, double murXMax, double murYMax) {
        double min = Math.min(posX - murXMin, posY - murYMin);
        min = Math.min(min, murXMax - posX);
        min = Math.min(min, murYMax - posY);
        return min;
    }

    // Normalise la vitesse pour que sa longueur soit toujours égale à 1
    protected void Normaliser() {
        double longueur = Math.sqrt(vitesseX * vitesseX + vitesseY * vitesseY);
        vitesseX /= longueur;
        vitesseY /= longueur;
    }

    // Gère l'évitement des murs
    protected boolean EviterMurs(double murXMin, double murYMin, double murXMax, double murYMax) {
        // Vérifier si le poisson touche un mur
        if (posX < murXMin) posX = murXMin;
        else if (posY < murYMin) posY = murYMin;
        else if (posX > murXMax) posX = murXMax;
        else if (posY > murYMax) posY = murYMax;

        // Si trop proche d'un mur, on change la direction
        double distance = DistanceAuMur(murXMin, murYMin, murXMax, murYMax);
        if (distance < DISTANCE_MIN) {
            if (posX == murXMin) vitesseX += 0.3;
            else if (posY == murYMin) vitesseY += 0.3;
            else if (posX == murXMax) vitesseX -= 0.3;
            else if (posY == murYMax) vitesseY -= 0.3;

            Normaliser();
            return true;  // On a évité un mur
        }
        return false;
    }

    // Gère l'évitement des obstacles
    protected boolean EviterObstacles(ArrayList<ZoneAEviter> obstacles) {
        if (!obstacles.isEmpty()) {
            ZoneAEviter obstacleProche = obstacles.get(0);
            double distanceCarre = DistanceCarre(obstacleProche);
    
            // Vérifier la distance par rapport à chaque obstacle
            for (ZoneAEviter o : obstacles) {
                if (DistanceCarre(o) < distanceCarre) {
                    obstacleProche = o;
                    distanceCarre = DistanceCarre(o);
                }
            }
    
            // Si le poisson entre dans la zone d'un obstacle
            if (distanceCarre < (obstacleProche.rayon * obstacleProche.rayon)) {
                double distance = Math.sqrt(distanceCarre);
                double diffX = (obstacleProche.posX - posX) / distance;
                double diffY = (obstacleProche.posY - posY) / distance;
    
                // Ajuster la vitesse pour s'éloigner de l'obstacle
                vitesseX -= diffX / 2;
                vitesseY -= diffY / 2;
    
                Normaliser();  // Réajuster la direction de la vitesse
                return true;
            }
        }
        return false;
    }
    

    // Gère l'évitement des poissons proches
    protected boolean EviterPoissons(Poisson[] poissons) {
        double distanceCarreMin = Double.MAX_VALUE;
        Poisson poissonProche = null;

        // Trouve le poisson le plus proche
        for (Poisson p : poissons) {
            if (!p.equals(this)) {
                double distanceCarre = DistanceCarre(p);
                if (distanceCarre < distanceCarreMin) {
                    distanceCarreMin = distanceCarre;
                    poissonProche = p;
                }
            }
        }

        // Si un poisson proche est trouvé et trop proche, évite-le
        if (poissonProche != null && distanceCarreMin < DISTANCE_MIN_CARRE) {
            double distance = Math.sqrt(distanceCarreMin);
            double diffX = (poissonProche.posX - posX) / distance;
            double diffY = (poissonProche.posY - posY) / distance;
            vitesseX = vitesseX - diffX / 4;
            vitesseY = vitesseY - diffY / 4;
            Normaliser();
            return true;  // On a évité un poisson
        }
        return false;
    }

    // Calcule la direction moyenne des poissons voisins
    protected void CalculerDirectionMoyenne(Poisson[] poissons) {
        double vitesseXTotal = 0;
        double vitesseYTotal = 0;
        int nbTotal = 0;

        // Somme des vitesses des poissons dans un rayon d'alignement
        for (Poisson p : poissons) {
            if (DansAlignement(p)) {
                vitesseXTotal += p.vitesseX;
                vitesseYTotal += p.vitesseY;
                nbTotal++;
            }
        }

        // Si au moins un poisson est dans le rayon d'alignement, on calcule la direction moyenne
        if (nbTotal > 0) {
            vitesseX = (vitesseXTotal / nbTotal + vitesseX) / 2;
            vitesseY = (vitesseYTotal / nbTotal + vitesseY) / 2;
            Normaliser();
        }
    }

    // Mise à jour de la position en tenant compte des obstacles, poissons et murs
    protected void MiseAJour(Poisson[] poissons, ArrayList<ZoneAEviter> obstacles, double largeur, double hauteur) {
        if (!EviterMurs(0, 0, largeur, hauteur)) {
            if (!EviterObstacles(obstacles)) {
                if (!EviterPoissons(poissons)) {
                    CalculerDirectionMoyenne(poissons);
                }
            }
        }
        MiseAJourPosition();  // Mise à jour de la position finale après calculs
    }
}
