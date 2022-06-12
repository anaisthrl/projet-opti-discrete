package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    private static final boolean ExportCSV = false;

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        String path = System.getProperty("user.dir");
        String sep = FileSystems.getDefault().getSeparator();

        URL fxmlFile = new File("src" + sep + "main" + sep + "resources" + sep + "form" + sep + "cvrp.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(fxmlFile);
        Scene scene = new Scene(root);
        stage.setTitle("Projet Opti CVRP");
        stage.setScene(scene);
        Main.stage = stage;
        stage.show();
    }


    public static void main(String args[]) throws IOException {
        if (Main.ExportCSV) {
//            ExportController.prepareExport(Algorithm.TABOU, 5000,0 , 0, 1, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.TABOU, 5000,0 , 0, 10, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.TABOU, 5000,0 , 0, 10, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.TABOU, 5000,0 , 0, 20, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.1, 10, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.5, 10, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.9, 10, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.1, 100, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.5, 100, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.9, 100, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.1, 1000, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.5, 1000, 0, "RelocateINTER RelocateINTRA Exchange");
//            ExportController.prepareExport(Algorithm.RECUIT, 5000, 0.9, 1000, 0, "RelocateINTER RelocateINTRA Exchange");
        }


        launch();
    }

/*
    public static void showresult() throws IOException {
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
            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for(Node node : vehicule.tournee) {
                System.out.print(graph.nodes.indexOf(node) + " ");
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueur);
        }
    }*/

    public static Graph genAleatoire(Graph graph) {
        ArrayList<Node> copy = new ArrayList<>(graph.nodes);
        Random rand = new Random();
        Depot depot = (Depot) copy.remove(0);

        while (!copy.isEmpty()) {
            Vehicule vehicule = new Vehicule(depot);
            vehicule.tournee = new ArrayList<Node>();
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

                if (vehicule.getNbColis() + poids <= 100) {
                    vehicule.addClient(copy.remove(i));
                } else {
                    encorePlace = false;
                }
            }
            graph.vehicules.add(vehicule);
        }
        return graph;
    }

    public static Stage getStage() {
        return stage;
    }
}
