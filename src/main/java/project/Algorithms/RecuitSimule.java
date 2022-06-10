package project.Algorithms;

import project.Model.Graph;
import project.Model.Vehicule;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.ArrayList;
import java.util.Random;

public class RecuitSimule {
    private static int N1 = 100;
    private static int N2 = 10000;

    public void recuitSimule(Graph graph, int maxIteration, float mu, double temperature) {
        final java.util.Random random = new Random();

        ArrayList<Vehicule> currentSolution;
        double latestFitness = graph.getFitness();
        Operation operation;
        Neighbourhood neighbourhood = new Neighbourhood();
        int nbTemp = (int) (Math.log(Math.log(0.8) / Math.log(0.01)) / Math.log(mu)) * 3; //Nb changement de temperature
        //System.out.println(nbTemp);
        for (int i = 0; i < nbTemp; i++) {
            for (int j = 0; j < maxIteration; j++) {
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
    }
/*
    private Graph recuitSimule(Graph map, double mu) {
        int nbTemp = (int) (Math.log(Math.log(0.8) / Math.log(0.01)) / Math.log(mu)) * 3;
        double temperature = 0;

        long startTime = System.nanoTime();

        Random rnd = new Random();
        Graph cloneMap = map.cloneMap();

        Graph bestSolution = cloneMap.cloneMap();
        double minFitness = Integer.MAX_VALUE;
        double fitnessBestSolution = cloneMap.getFitness();
        Graph nextVoisin = null;

        for (int k = 0; k < nbTemp; k++) {
            for (int l = 1; l < N2; l++) {
                cloneMap = map.cloneMap();
                ArrayList<Graph> voisin = new ArrayList<>();
                for (VoisinAlgo voisinAlgo : this.voisins)
                    voisin.addAll(voisinAlgo.lancerToutVoisin(map));

                //choix du voisin random
                Graph randomVoisin = voisin.get(rnd.nextInt(voisin.size() - 1));
                double fitnessVoisin = randomVoisin.getFitness();

                var diffFitness = fitnessVoisin - fitnessBestSolution;
                // Check pk fichier A6009 diffFitness = fitnessVoisin - fitnessBestSolution = 0
                if (k == 1 && l == 1) {
                    temperature = -(Math.abs(diffFitness)) / Math.log(0.8);
                    System.out.println(temperature);
                }

                //si voisin est meilleur
                double proba = rnd.nextDouble();
                if (diffFitness <= 0 || proba <= Math.exp(-diffFitness / temperature)) {
                    nextVoisin = randomVoisin;
                    fitnessBestSolution = fitnessVoisin;
                } else {
                    nextVoisin = cloneMap;
                }
                map = nextVoisin;
            }
            temperature = mu * temperature;
            System.out.println(k);
        }
        long elapsedTime = System.nanoTime() - startTime;
        long durationInMs = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        System.out.println("Total exec. time: " + durationInMs + "ms");


        return map;
    }*/
}
