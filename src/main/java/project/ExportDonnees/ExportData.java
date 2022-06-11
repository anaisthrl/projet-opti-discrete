package project.ExportDonnees;

import project.Algorithms.Algorithm;

import java.util.stream.Stream;

public class ExportData {
    private String nomFichier;
    private int nbClient; // Nombre de clients
    private int nbVehicule; // Nombre de véhicules minimum après simulation
    private String nomAlgo; // Recuit ou Tabu (type d'algo)
    private double baseFitness;
    private double endFitness; // Fitness résultat après simulation
    private int nbIteration; // Nombre d'itérations
    private double executionTime; // Temps d'éxécution de l'algorithme
    private double mu;
    private double temperature;
    private int tailleListTabou;

    public ExportData(String nomFichier,
                      int nbClient,
                      String nomAlgo,
                      double baseFitness,
                      double endFitness,
                      int nbVehicule,
                      int nbIteration,
                      double mu,
                      double temperature,
                      int tailleListTabou,
                      double executionTime) {
        this.nomFichier = nomFichier;
        this.nbClient = nbClient;
        this.baseFitness = baseFitness;
        this.endFitness = endFitness;
        this.nbIteration = nbIteration;
        this.nbVehicule = nbVehicule;
        this.mu = mu;
        this.temperature = temperature;
        this.tailleListTabou = tailleListTabou;
        this.nomAlgo = nomAlgo;
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "CsvData{" +
                "nomFichier='" + nomFichier + '\'' +
                ", nbClient=" + nbClient +
                ", nomAlgo=" + nomAlgo +
                ", baseFitness=" + baseFitness +
                ", endFitness=" + endFitness +
                ", nbIteration=" + nbIteration +
                ", nbVehicule=" + nbVehicule +
                ", mu=" + mu +
                ", tailleListTabou=" + tailleListTabou +
                ", executionTime" + executionTime +
                ", temperature" +temperature +
                '}';
    }

    public String[] getRowForAlgorithm(Algorithm algorithm) {
        String[] rows = new String[]{nomFichier, nbClient + "", baseFitness + "",
                nomAlgo, endFitness + "", nbVehicule + "",
                nbIteration + "", executionTime +""};

        if (algorithm == Algorithm.RECUIT) {
            return Stream.of(rows, new String[]{mu + "", temperature + ""}).flatMap(Stream::of).toArray(String[]::new);
        } else {
            return Stream.of(rows, new String[]{tailleListTabou + ""}).flatMap(Stream::of).toArray(String[]::new);
        }
    }


    public int getNbVehicule() {
        return nbVehicule;
    }

    public void setNbVehicule(int nbVehicule) {
        this.nbVehicule = nbVehicule;
    }

    public double getEndFitness() {
        return endFitness;
    }

    public void setEndFitness(double endFitness) {
        this.endFitness = endFitness;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }
}
