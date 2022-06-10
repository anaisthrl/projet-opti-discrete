package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CrossExchange extends Operation {
    private final Node a;
    private final Node b;

    public CrossExchange(Node a, Node b) {
        if (a instanceof Depot) throw new IllegalArgumentException("A ne peut pas etre le Depot");
        if (b instanceof Depot) throw new IllegalArgumentException("B ne peut pas etre le Depot");

        this.a = a;
        this.b = b;
    }

    @Override
    public void apply(Graph graph) {
        final Vehicule vA = graph.getVehiculeContaining(a);
        final Vehicule vB = graph.getVehiculeContaining(b);

        if (vA == null) throw new IllegalArgumentException("Node A not in solution");
        if (vB == null) throw new IllegalArgumentException("Node B not in solution");

        final int iA = vA.tournee.indexOf(a);
        final int iB = vB.tournee.indexOf(b);

        List<Node> subPathA =  vA.tournee.subList(iA,  vA.tournee.size());
        List<Node> subPathB =  vB.tournee.subList(iB,  vB.tournee.size());

        List<Node> tmp = new ArrayList<>(subPathB);
        List<Node> tmp2 = new ArrayList<>(subPathA);

        subPathB.clear();
        subPathA.clear();

        vA.tournee.addAll(tmp);
        vB.tournee.addAll(tmp2);
    }

    @Override
    public Operation revert() {
        return this;
    }


    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Swap{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    /**
     * Check si le poids max n'est pas enfreint
     *
     * @param graph Graphe
     * @return validite de l'operation
     */
    @Override
    public boolean isValid(Graph graph) {
        boolean valid = true;
        apply(graph);
        if (graph.getVehiculeContaining(a).getNbColis() > Vehicule.MAX_CAPACITY) {
            valid = false;
        }
        if (graph.getVehiculeContaining(b).getNbColis() > Vehicule.MAX_CAPACITY) {
            valid = false;
        }
        revert().apply(graph);
        return valid;
    }
}
