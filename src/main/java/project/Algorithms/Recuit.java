package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.ArrayList;
import java.util.Random;

public class Recuit implements Algorithme{
    private Graph graph;
    private double temp;
    private double mu = 0.9;
    private int n2_i = 5;
    private int n1_i = 0;

    private final Random random = new Random();
    private final Neighbourhood neighbourhood = new Neighbourhood();

//    ListeParametre parametres = ListeParametre.of(
//            new ParametreDouble("MU", 0.0, 0.9999, 0.99),
//            new ParametreDouble("T0", 1.0, 1000.0, 900.0),
//            new ParametreInt("n2", 1.0, 10.0, 5.0),
//            new ParametreListeStr("testCB", List.of("test1", "test2"), "test1")
//    );

    public Recuit(Graph graph) {

        this.graph = graph;
        this.temp = this.mu;
        //this.t = ((ParametreDouble) parametres.find("MU")).getValue();
    }

    public void update() {
        final double f = graph.getFitness();

        final Operation operation = neighbourhood.choixTransforAleatoire(graph);

        operation.apply(graph);
        final double new_f = graph.getFitness();
        final double delta_f = new_f - f;

        if (delta_f > 0) {
            double p = random.nextDouble();
            if (p > Math.exp(-delta_f / temp)) {
                // rollback
                operation.revert().apply(graph);
            }
        }

        n2_i++;
        if (n2_i == 5) {
            n2_i = 0;
            temp *= this.mu;
            n1_i++;
        }
    }

    public Graph recuitSimule(int maxIteration, float variation) {
        ArrayList<Vehicule> currentSolution;
        double latestFitness = this.graph.getFitness();
        double temperature = 1000;
        Operation operation;
        int nbTemp = (int)(Math.log(Math.log(0.8) / Math.log(0.01))/Math.log(variation) )* 3; //Nb changement de temperature
        System.out.println("nb temp : " + nbTemp);
        for (int i = 0; i < nbTemp; i++){
            for (int j = 0; j < maxIteration; j++) {
                currentSolution = this.graph.cloneVehicules();
                operation = neighbourhood.choixTransforAleatoire(graph);
                if(operation.isValid(graph)){
                    operation.apply(graph);

                }
                double currentTotalFitness = graph.getFitness();
                double delta = currentTotalFitness - latestFitness;
                if (delta < 0 || (random.nextDouble() < Math.exp(-delta / temperature))) {
                    latestFitness = currentTotalFitness;
                    //this.graph = g;
                } else {
                    operation.revert().apply(graph);
                }
                temperature = variation * temperature;
            }
        }
        return this.graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

//     public ListeParametre getParametres() {
//        return parametres;
//    }

    public Graph stop() {
        return graph;
        //Not to implement
    }
}
