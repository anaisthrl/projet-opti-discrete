package project.Model;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        return this.vehicules.stream().mapToDouble(Vehicule::getLongueurTournee).sum();
    }

    public static double getFitness(ArrayList<Vehicule> vehicles) {
        return vehicles.stream().mapToDouble(Vehicule::getLongueurTournee).sum();
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

    public static Graph load(File myFile) throws IOException {
        //File myFile = new File(pathFile);

        FileInputStream inputStream = new FileInputStream(myFile);

        List<String> inputLines = IOUtils.readLines(inputStream);

        /*
         * READ input
         */
        int i = 0, x, y, q, index;
        String[] splittedLine;
        Graph graph = new Graph();
        graph.nodes = new ArrayList<Node>();
        graph.vehicules = new ArrayList<Vehicule>();

        for (String line : inputLines) {

            splittedLine = line.trim().split(";");

            //     log.debug("line {}: {}", i, Arrays.toString(splittedLine));

            if (i == 0) {

            } else if (i == 1) {// second line: depot coordinates
                index = Integer.parseInt(splittedLine[0]);
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // y-coordinates
                graph.nodes.add(new Depot(x, y, q, index));

            } else {
                index = Integer.parseInt(splittedLine[0]);// other lines
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // demand

                graph.nodes.add(new Client(x, y, q, index));
            }
            i++;
        }
        return graph;
    }
}
