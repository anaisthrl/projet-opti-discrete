package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.ArrayList;
import java.util.Random;

public class RecuitSimule {
    public Graph recuitSimule(Graph graph, int maxIteration, float mu, double temperature) {
        final java.util.Random random = new Random();

        ArrayList<Vehicule> currentSolution;
        double latestFitness = graph.getFitness();
        Operation operation;
        Neighbourhood neighbourhood = new Neighbourhood();
        int nbTemp = (int)(Math.log(Math.log(0.8) / Math.log(0.01))/Math.log(mu) )* 3; //Nb changement de temperature
        //System.out.println(nbTemp);
        for (int i = 0; i < nbTemp; i++){
            for (int j = 0; j < maxIteration; j++) {
                currentSolution = graph.cloneVehicules();

                operation = neighbourhood.getRandomVoisinage(graph);

                operation.apply(graph);

                double currentTotalFitness = graph.getFitness();
                double delta = currentTotalFitness - latestFitness;
                if (delta < 0 || (random.nextDouble() < Math.exp(-delta / temperature))) {
                    latestFitness = currentTotalFitness;
                    //graph = g;
                } else {
                    //operation.revert().apply(graph);
                    graph.setVehicules(currentSolution);
                }
                temperature = mu * temperature;
            }
        }
        return graph;
    }
}
