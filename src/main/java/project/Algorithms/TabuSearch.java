package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.Random;
import java.util.*;

public class TabuSearch {
    final java.util.Random random = new Random();
    private int tailleListe;

    public TabuSearch(int tailleListe) {
        this.tailleListe = tailleListe;
    }


    public Graph tabuSearch(Graph graph, int maximumIteration, int tabuLength) {
        ArrayList<Vehicule> bestSolution = graph.cloneVehicules();
        double latestFitness = graph.getFitness();
        double bestFitness = latestFitness;
        Queue<ArrayList<Vehicule>> tabuList = new LinkedList<>();

        ArrayList<ArrayList<Vehicule>> neighbors;
        Neighbourhood neighbourhood = new Neighbourhood(graph);

        for (int i = 0; i < maximumIteration; i++) {

            neighbors = neighbourhood.generateNeighbors();
            ArrayList<Vehicule> min = neighbors.stream()
                    .filter(neighbor -> !tabuList.contains(neighbor))
                    .min((solution1, solution2) -> (int) (Graph.getFitness(solution1) - Graph.getFitness(solution2)))
                    .get();

            double newRouteLength = Graph.getFitness(min);
            if (newRouteLength > latestFitness) {
                if (tabuList.size() == tabuLength) {
                    tabuList.remove();
                }
                tabuList.add(Graph.cloneVehicles(min));
            }

            latestFitness = newRouteLength;
            if (newRouteLength < bestFitness) {
                bestFitness = newRouteLength;
                bestSolution = Graph.cloneVehicles(min);
            }

            graph.setVehicules(min);
        }
        graph.setVehicules(bestSolution);
        return graph;
    }
}
