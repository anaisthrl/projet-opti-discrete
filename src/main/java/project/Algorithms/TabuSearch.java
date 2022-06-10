package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.*;
import java.util.Random;

public class TabuSearch {
    private final List<Operation> opmMoins1 = new ArrayList<>();
    private double fitnessMini = Double.POSITIVE_INFINITY;
    private List<Operation> opGetBackBestSolution = new ArrayList<>();
    private int tailleListe;

    public TabuSearch(int tailleListe) {
        this.tailleListe = tailleListe;

    }

/*
    public Graph tabuSearch(Graph graph, int maximumIteration, int tabuLength) {
        final java.util.Random random = new Random();

        ArrayList<Vehicule> bestSolution = graph.cloneVehicles();
        double latestFitness = graph.getFitness();
        double bestFitness = latestFitness;
        Queue<ArrayList<Vehicule>> tabuList = new LinkedList<>();

        ArrayList<ArrayList<Vehicule>> neighbors;
        Neighbourhood neighbourhood = new Neighbourhood();
        for (int i = 0; i < maximumIteration; i++) {
            neighbors = t.generateNeighbors();
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
    }*/

    public Graph tabuNum2 (Graph graph, int maxIter) {
        Neighbourhood neighbourhood = new Neighbourhood();
        for(int i = 0; i < maxIter; i++) {
            List<Operation> C = neighbourhood.getVoisinage(graph)
                    .stream()
                    .filter(op -> !operationPresentInList(op))
                    .toList();


            //On garde celui avec la plus petite fitness
            double fitnessMiniXPlus1 = Double.POSITIVE_INFINITY;
            Operation operationXPlus1 = null;
            for (Operation operation : C) {
                double fitnessxPlus1 = this.getLongueurOfOperatation(operation, graph);
                if (fitnessxPlus1 < fitnessMiniXPlus1) {
                    fitnessMiniXPlus1 = fitnessxPlus1;
                    operationXPlus1 = operation;
                }
            }

            if (operationXPlus1 != null) {
                //On calcule les deltas
                double deltaF = fitnessMiniXPlus1 - graph.getFitness();
                if (deltaF > 0) {
                    ajoutInverseOperationToListMMoins1(operationXPlus1);
                    opGetBackBestSolution.add(operationXPlus1.revert());
                }
                if (fitnessMiniXPlus1 < fitnessMini) {
                    fitnessMini = fitnessMiniXPlus1;
                    opGetBackBestSolution = new ArrayList<>();
                }
                operationXPlus1.apply(graph);
            }
        }
        return graph;
    }

    /**
     * @param operation operation a tester
     * @return fitness de la solution si l'on applique l'operation
     */
    private double getLongueurOfOperatation(Operation operation, Graph graph) {
        operation.apply(graph);
        double fitnessOperation = graph.getFitness();
        operation.revert().apply(graph);
        return fitnessOperation;
    }

    private boolean operationPresentInList(Operation operation) {
        return this.opmMoins1.contains(operation);
    }

    private void ajoutInverseOperationToListMMoins1(Operation operation) {
        this.opmMoins1.add(operation.revert());
    }
}
