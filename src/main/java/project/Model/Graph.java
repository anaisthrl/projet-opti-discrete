package project.Model;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;
    public ArrayList<Vehicule> vehicules;

    public Graph() {
    }

    public Graph(ArrayList<Node> nodes, ArrayList<Vehicule> vehicules) {
        this.nodes = nodes;
        this.vehicules = vehicules;
    }

    public Vehicule getVehiculeContaining(Node node) {
        for (final Vehicule vehicule : vehicules) {
            if (vehicule.tournee.contains(node)) {
                return vehicule;
            }
        }
        return null;
    }

    public Vehicule getVehiculeFromTournee(Tournee _tournee) {
        for (final Vehicule vehicule : vehicules) {
            if (vehicule.tournee.equals(_tournee)) {
                return vehicule;
            }
        }
        return null;
    }

    public Node getDepot() {
        return this.nodes.get(0);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(ArrayList<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public double getFitness() {
        return this.vehicules.stream().mapToDouble(Vehicule::getLongueur).sum();
    }
}
