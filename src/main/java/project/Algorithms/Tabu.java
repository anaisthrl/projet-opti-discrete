package project.Algorithms;

import project.Model.Graph;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.ArrayList;
import java.util.List;

public class Tabu implements Algorithme {
    private Graph graph;
    private final List<Operation> opmMoins1 = new ArrayList<>();
    private final int tailleVoisin = 100;
    private double fitnessMini = Double.POSITIVE_INFINITY;
    private final Neighbourhood neighbourhood;
    private List<Operation> opGetBackBestSolution = new ArrayList<>();
    private int tailleListe = 15;

    public Tabu(Graph graph) {
        this.graph = graph;
        neighbourhood = new Neighbourhood();
    }

    @Override
    public void update() {
        //On choisis C
        List<Operation> C = this.neighbourhood.getVoisinage(graph)
                .stream()
                .filter(op -> !operationPresentInList(op))
                .toList();


        //On garde celui avec la plus petite fitness
        double fitnessMiniXPlus1 = Double.POSITIVE_INFINITY;
        Operation operationXPlus1 = null;
        for (Operation operation : C) {
            double fitnessxPlus1 = this.getLongueurOfOperatation(operation);
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

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Graph stop() {
        applyListeOperationToSolution();
        return graph;
    }

    /**
     * @param operation Operation a verifier
     * @return true si l'operation existe dans la liste
     */
    private boolean operationPresentInList(Operation operation) {
        return this.opmMoins1.contains(operation);
    }

    /**
     * @param operation Operation dont l'inverse est a ajouter
     */
    private void ajoutInverseOperationToListMMoins1(Operation operation) {
        //While au lieu de if au cas ou la taille de la liste est change
        /*while (this.opmMoins1.size() >= tailleListe) {
            //this.parametres.remove(0);
        }*/
        this.opmMoins1.add(operation.revert());
    }

    /**
     * @param operation operation a tester
     * @return fitness de la solution si l'on applique l'operation
     */
    private double getLongueurOfOperatation(Operation operation) {
        operation.apply(graph);
        double fitnessOperation = graph.getFitness();
        operation.revert().apply(graph);
        return fitnessOperation;
    }

    /**
     * Applique la liste des operations inverse pour recuperer la solution minimale
     */
    private void applyListeOperationToSolution() {
        for (int i = opGetBackBestSolution.size() - 1; i >= 0; i--) {
            opGetBackBestSolution.get(i).apply(graph);
            opGetBackBestSolution.remove(i);
        }
    }
/*
    @Override
    public void applyParametre() {
        this.tailleListe = ((ParametreInt) parametres.find("Taille de la liste")).getValue().intValue();
    }*/
}
