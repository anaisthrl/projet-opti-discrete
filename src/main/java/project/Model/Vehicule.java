package project.Model;

public class Vehicule {
    public static int MAX_CAPACITY = 100;

    public int nbColis = 0;
    public double longueur = 0;
    public Tournee tournee;

    public void addClient(Node client) {
        this.tournee.add(client);
        updateNbColis();
    }

    public double getDistanceBetween2Node(Node a, Node b) {
        //pas sqrt pour gagner temps
        return Math.pow(a.getPos().getX() - b.getPos().getX(), 2) + Math.pow(a.getPos().getY() - b.getPos().getY(), 2);
    }

    public void updateDistanceTournee() {
        double res = 0;

        for (int i = 1; i < tournee.size(); i++) {
            res += getDistanceBetween2Node(tournee.get(i-1), tournee.get(i));
        }
        this.longueur = res;
    }


    private void updateNbColis() {
        this.nbColis = tournee.stream()
                .mapToInt(Node::getPoids)
                .sum();
    }

    private void update() {
        updateDistanceTournee();
        updateNbColis();

        tournee.changed = false;
    }

    /**
     * @return la longueur totale du chemin
     */
    public double getLongueur() {
        if (tournee.changed || longueur == 0) {
            updateDistanceTournee();
        }
        return longueur;
    }

    /**
     * @return le poids total des colis transportés
     */
    public int getNbColis() {
        if (this.tournee.changed || nbColis == 0) {
            updateNbColis();
        }
        return nbColis;
    }
}
