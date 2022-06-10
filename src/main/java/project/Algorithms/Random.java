package project.Algorithms;

import project.Model.*;

import java.util.ArrayList;

public class Random {
    public static Graph genAleatoire(Graph graph) {
        ArrayList<Node> copy = new ArrayList<>(graph.nodes);
        java.util.Random rand = new java.util.Random();
        Depot depot = (Depot) copy.remove(0);

        while (!copy.isEmpty()) {
            Vehicule vehicule = new Vehicule(depot);
            vehicule.tournee = new ArrayList<Node>();
            //vehicule.tournee.add(depot);
            vehicule.nbColis = 0;
            int poids = 0;
            int i = 0;
            boolean encorePlace = true;
            while (encorePlace && !copy.isEmpty()) {
                if (copy.size() == 1) {
                    i = 0;
                } else {
                    i = rand.nextInt(0, copy.size() - 1);
                }
                poids = copy.get(i).getPoids();

                if(vehicule.getNbColis() + poids <= 100) {
                    vehicule.addClient(copy.remove(i));
                } else {
                    encorePlace = false;
                }
            }
            //vehicule.tournee.add(depot);
            graph.vehicules.add(vehicule);
        }
        return graph;
    }
}
