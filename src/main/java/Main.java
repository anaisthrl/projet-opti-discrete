import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static double MAX_CAPACITY = 100;
    public static String[] paths = {
            "C:/Users/Epulapp/Documents/4A/OptiDiscrete/Projet/A3205.txt",
            "C:/Users/Epulapp/Documents/4A/OptiDiscrete/Projet/A3205.txt",
            "C:/Users/Epulapp/Documents/4A/OptiDiscrete/Projet/A3205.txt",
            "C:/Users/Epulapp/Documents/4A/OptiDiscrete/Projet/A3205.txt",
    };

    public static void main(String args[]) throws IOException {
        Graph graph = load(paths[0]);
        double nbColis = 0;

        for (int i = 0; i < graph.nodes.size(); i++) {
            nbColis += graph.nodes.get(i).poids;
            System.out.println("index : " + i + "; "
                    + " x : " + graph.nodes.get(i).posX + "; "
                    + " y : " + graph.nodes.get(i).posY + "; "
                    + " q : " + graph.nodes.get(i).poids);
        }

        double res = nbColis / MAX_CAPACITY;
        System.out.println("Nb colis : " + nbColis);
        System.out.println("Nb minimum de vehicules : " + Math.ceil(res));

        graph = genAleatoire(graph);
        System.out.println("Nb vehicule : " + graph.vehicules.size());

        for (Vehicule vehicule : graph.vehicules) {
            //tableau index AT
            ArrayList<Integer> arIndex = new ArrayList<>();

            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for(Node node : vehicule.tournee) {
                System.out.print(graph.nodes.indexOf(node) + " ");
                //AT
                arIndex.add(graph.nodes.indexOf(node));
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.getDistanceTournee());

            //AT
            System.out.print("\n");
            System.out.println("relocate...\n");
            OperateursVoisinage ov = new OperateursVoisinage();
            ov.relocate(arIndex);
            System.out.print("\n");

        }

        CVRP cvrp = new CVRP(graph);

        //cvrp.repaint();
    }

    public static Graph genAleatoire(Graph graph) {
        ArrayList<Node> copy = new ArrayList<>(graph.nodes);
        Random rand = new Random();
        Depot depot = (Depot) copy.remove(0);

        while (!copy.isEmpty()) {
            Vehicule vehicule = new Vehicule();
            vehicule.tournee = new ArrayList<>();
            vehicule.tournee.add(depot);
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
                poids = copy.get(i).poids;

                if(vehicule.nbColis + poids <= 100) {
                    vehicule.nbColis += poids;
                    vehicule.tournee.add(copy.remove(i));
                } else {
                    encorePlace = false;
                }
            }
            vehicule.tournee.add(depot);
            graph.vehicules.add(vehicule);
        }
        return graph;
    }

    public static Graph load(String pathFile) throws IOException {
        File myFile = new File(pathFile);

        FileInputStream inputStream = new FileInputStream(myFile);

        List<String> inputLines = IOUtils.readLines(inputStream);

        /*
         * READ input
         */
        int i = 0, x, y, q;
        String[] splittedLine;
        Graph graph = new Graph();
        graph.nodes = new ArrayList<Node>();
        graph.vehicules = new ArrayList<Vehicule>();

        for (String line : inputLines) {

            splittedLine = line.trim().split(";");

            //     log.debug("line {}: {}", i, Arrays.toString(splittedLine));

            if (i == 0) {

            } else if (i == 1) {                            // second line: depot coordinates
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // y-coordinates
                graph.nodes.add(new Depot(x, y, q));

            } else {                                        // other lines
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // demand

                graph.nodes.add(new Client(x, y, q));
            }
            i++;
        }
        return graph;
    }
}
