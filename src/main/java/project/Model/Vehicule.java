package project.Model;

import java.util.ArrayList;
import java.util.List;

public class Vehicule {
    public static int MAX_CAPACITY = 100;

    public int nbColis = 0;
    public double longueur = 0;
    public Depot depot;
    public List<Node> tournee;

    public Vehicule(Depot depot) {
        this.depot = depot;
        this.tournee = new ArrayList<>();
    }

    public List<Node> getTournee() {
        return tournee;
    }



    public void addClient(Node client) {
        if (this.tournee == null) {
            this.tournee = new ArrayList<>();
        }
        this.tournee.add(client);
        updateDistanceTournee();
        updateNbColis();
    }

    public double getDistanceBetween2Node(Node a, Node b) {

        final int dx = a.getPos().getX() - b.getPos().getX();
        final int dy = a.getPos().getY() - b.getPos().getY();

        return Math.sqrt(dx * dx + dy * dy);

        //  return Math.sqrt(Math.pow(a.getPos().getX() - b.getPos().getX(), 2) + Math.pow(a.getPos().getY() - b.getPos().getY(), 2));

    }

    public void updateDistanceTournee() {
        double res = 0;

        res += getDistanceBetween2Node(depot, tournee.get(0));
        for (int i = 1; i < tournee.size(); i++) {
            res += getDistanceBetween2Node(tournee.get(i - 1), tournee.get(i));
        }
        res += getDistanceBetween2Node(tournee.get(tournee.size() - 1), depot);
        this.longueur = res;
    }


    private void updateNbColis() {
        this.nbColis = tournee.stream()
                .mapToInt(Node::getPoids)
                .sum();
    }


    /**
     * @return la longueur totale du chemin
     */
    public double getLongueur() {
        if(tournee.size() > 0) {
            updateDistanceTournee();
            updateNbColis();
        }
        return longueur;
    }

    /**
     * @return le poids total des colis transportés
     */
    public int getNbColis() {
        if(tournee.size() > 0) {
            updateDistanceTournee();
            updateNbColis();
        }
        return nbColis;
    }

    @Override
    public Vehicule clone() {
        Vehicule clone = new Vehicule(depot);
        tournee.forEach(clone::addClient);
        return clone;
    }

    public void setTournee(List<Node> tournee) {
        this.tournee = tournee;
    }
}
