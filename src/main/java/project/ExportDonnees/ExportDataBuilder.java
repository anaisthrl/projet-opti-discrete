package project.ExportDonnees;

public class ExportDataBuilder {
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

    private ExportDataBuilder() {
    }

    public static ExportDataBuilder builder() {
        return new ExportDataBuilder();
    }

    public ExportDataBuilder mu(double mu) {
        this.mu = mu;
        return this;
    }

    public ExportDataBuilder tailleListTabou(int tailleListTabou) {
        this.tailleListTabou = tailleListTabou;
        return this;
    }

    public ExportDataBuilder nomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
        return this;
    }

    public ExportDataBuilder nbClient(int nbClient) {
        this.nbClient = nbClient;
        return this;
    }

    public ExportDataBuilder baseFitness(double baseFitness) {
        this.baseFitness = baseFitness;
        return this;
    }

    public ExportDataBuilder nbVehicule(int nbVehicule) {
        this.nbVehicule = nbVehicule;
        return this;
    }

    public ExportDataBuilder nomAlgo(String nomAlgo) {
        this.nomAlgo = nomAlgo;
        return this;
    }

    public ExportDataBuilder endFitness(double endFitness) {
        this.endFitness = endFitness;
        return this;
    }

    public ExportDataBuilder temperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ExportDataBuilder iterationCount(int nbIteration) {
        this.nbIteration = nbIteration;
        return this;
    }

    public ExportDataBuilder executionTime(double executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public ExportData build() {
        return new ExportData(nomFichier, nbClient, nomAlgo,  baseFitness, endFitness, nbVehicule, nbIteration, mu, temperature,tailleListTabou, executionTime);
    }
}
