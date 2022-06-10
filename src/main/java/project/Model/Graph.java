package project.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Vehicule getVehiculeFromTournee(List<Node> _tournee) {
        for (final Vehicule vehicule : vehicules) {
            if (vehicule.tournee.equals(_tournee)) {
                return vehicule;
            }
        }
        return null;
    }

    public Depot getDepot() {
        return (Depot) this.nodes.get(0);
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

    public ArrayList<Vehicule> cloneVehicules() {
        return (ArrayList<Vehicule>) this.vehicules.stream().map(Vehicule::clone).collect(Collectors.toList());
    }

    public static ArrayList<Vehicule> cloneVehicles(ArrayList<Vehicule> vehicles) {
        return (ArrayList<Vehicule>) vehicles.stream().map(Vehicule::clone).collect(Collectors.toList());
    }

    public void setVehicules(ArrayList<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public double getFitness() {
        return this.vehicules.stream().mapToDouble(Vehicule::getLongueur).sum();
    }

    public static double getFitness(ArrayList<Vehicule> vehicles) {
        return vehicles.stream().mapToDouble(Vehicule::getLongueur).sum();
    }
    public Graph cloneMap() {

        ArrayList<Vehicule> veh = new ArrayList<>();
        for (Vehicule v : this.vehicules) {
            Vehicule vehicle = new Vehicule(getDepot());
            vehicle.setTournee(new ArrayList<>(v.tournee));
            veh.add(vehicle);
        }

        return new Graph(new ArrayList<>(this.getNodes()), veh);
    }
}
