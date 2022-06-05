import Model.*;
import Operations.OperateursVoisinage;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static String[] paths = {
            "ressources/A3205.txt",
            "ressources/A3305.txt",
            "ressources/A3306.txt",
            "ressources/A3405.txt",
    };

    public static void main(String args[]) throws IOException {
        Graph graph = load(paths[0]);
        double nbColis = 0;

        for (int i = 0; i < graph.nodes.size(); i++) {
            nbColis += graph.nodes.get(i).getPoids();
            System.out.println("index : " + i + "; "
                    + " x : " + graph.nodes.get(i).getPos().getX() + "; "
                    + " y : " + graph.nodes.get(i).getPos().getY() + "; "
                    + " q : " + graph.nodes.get(i).getPoids());
        }

        double res = nbColis / Vehicule.MAX_CAPACITY;
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
            System.out.println("Distance de la tournee : " + vehicule.longueur);

            //AT
            System.out.print("\n");
            System.out.println("relocate...\n");
            OperateursVoisinage ov = new OperateursVoisinage();
            //ov.relocate(arIndex);
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
            vehicule.tournee = new Tournee();
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

    public static Graph load(String pathFile) throws IOException {
        File myFile = new File(pathFile);

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
