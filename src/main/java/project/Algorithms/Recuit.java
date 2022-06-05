package project.Algorithms;

import project.Model.Graph;
import project.Operations.Neighbourhood;
import project.Operations.Operation;

import java.util.Random;

public class Recuit {
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
        final double f = graph.getNodes().size();

        final Operation operation = neighbourhood.getRandomVoisinage(graph);

        operation.apply(graph);
        final double new_f = graph.getNodes().size();
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
