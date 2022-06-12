package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.Objects;

public class SwapIntra extends Operation {
    private final Node a;
    private final Node b;

    public SwapIntra(Node a, Node b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void apply(Graph graph) {
        final Vehicule vA = graph.getVehiculeContaining(a);
        final Vehicule vB = graph.getVehiculeContaining(b);

        if (vA == null || vB == null) {
            if (vA == vB) {
                if (!(a instanceof Depot)) {
                    final int iA = vA.tournee.indexOf(a);
                    final int iB = vA.tournee.indexOf(b);

                    vA.tournee.set(iA, b);
                    vA.tournee.set(iB, a);
                }
            }
        }
    }

    @Override
    public Operation revert() {
        return new SwapIntra(this.b, this.a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwapIntra swapIntra = (SwapIntra) o;
        return Objects.equals(a, swapIntra.a) && Objects.equals(b, swapIntra.b);
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
