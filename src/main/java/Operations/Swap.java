package Operations;

import Model.Depot;
import Model.Graph;
import Model.Node;
import Model.Vehicule;

import java.util.Objects;

public class Swap extends Operation {
    private final Node a;
    private final Node b;

    public Swap(Node a, Node b) {
        if (a instanceof Depot) throw new IllegalArgumentException("A cannot be Depot");
        if (b instanceof Depot) throw new IllegalArgumentException("B cannot be Depot");

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

        vA.tournee.set(iA, b);
        vB.tournee.set(iB, a);
    }

    @Override
    public Operation revert() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Swap swap = (Swap) o;
        return Objects.equals(a, swap.a) && Objects.equals(b, swap.b);
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
