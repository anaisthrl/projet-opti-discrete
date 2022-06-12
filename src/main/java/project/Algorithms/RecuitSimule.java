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
        double latestFitness = graph.getFitness();
        Operation operation;
        Neighbourhood neighbourhood = new Neighbourhood();
        int nbTemp = (int) (Math.log(Math.log(0.8) / Math.log(0.01)) / Math.log(mu)) ; //Nb changement de temperature
        for (int i = 0; i < nbTemp; i++) {
            for (int j = 0; j < nbMaxIter; j++) {
                currentSolution = graph.cloneVehicules();

                operation = neighbourhood.choixTransforAleatoire(graph);
                operation.apply(graph);

                double currentTotalFitness = graph.getFitness();
                double delta = currentTotalFitness - latestFitness;
                if (delta < 0 || (random.nextDouble() < Math.exp(-delta / temperature))) {
                    latestFitness = currentTotalFitness;
                } else {
                    graph.setVehicules(currentSolution);
                }
                temperature = mu * temperature;
            }
        }
        return graph;
    }
}
