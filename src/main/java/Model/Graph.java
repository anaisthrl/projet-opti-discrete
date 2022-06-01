package Model;

import Model.Node;
import Model.Vehicule;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;
    public ArrayList<Vehicule> vehicules;

    public Graph() {}

    public Graph(ArrayList<Node> nodes, ArrayList<Vehicule> vehicules) {
        this.nodes = nodes;
        this.vehicules = vehicules;
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
}
