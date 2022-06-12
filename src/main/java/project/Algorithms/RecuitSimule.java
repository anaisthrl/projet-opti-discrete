package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.ArrayList;
import java.util.Random;

public class RecuitSimule {
    public Graph recuitSimule(Graph graph, int nbMaxIter, double mu, double temperature) {
        final java.util.Random random = new Random();

        ArrayList<Vehicule> currentSolution;
        double initialFitness = graph.getFitness();

        Operation operation;
        Neighbourhood neighbourhood = new Neighbourhood();

        //Nb changement temperature
        int nbTemp = (int) (Math.log(Math.log(0.8) / Math.log(0.01)) / Math.log(mu)) ;
        for (int i = 0; i < nbTemp; i++) {
            for (int j = 0; j < nbMaxIter; j++) {
                currentSolution = graph.cloneVehicules();

                //on choisi une transformation elementaire aléatoire et on l'applique
                operation = neighbourhood.choixTransforAleatoire(graph);
                operation.apply(graph);

                double currentFitness = graph.getFitness();
                double delta = currentFitness - initialFitness;
                if (delta < 0 || (random.nextDouble() < Math.exp(-delta / temperature))) {
                    initialFitness = currentFitness;
                } else {
                    //on applique les nouvelles tournées
                    graph.setVehicules(currentSolution);
                }
                temperature = mu * temperature;
            }
        }
        return graph;
    }
}
