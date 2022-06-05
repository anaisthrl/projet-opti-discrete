package project.Operations;

import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

public class TwoOpt extends Operation {

    private final Node a;
    private final Node b;

    public TwoOpt(Node a, Node b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void apply(Graph graph) {
        final Vehicule vx = graph.getVehiculeContaining(a);
        final Vehicule vy = graph.getVehiculeContaining(b);

        if (vx == null) {
            throw new IllegalArgumentException("Node A not in solution");
        }
        if (vy == null) {
            throw new IllegalArgumentException("Node B not in solution");
        }
        if (vx != vy) {
            throw new IllegalArgumentException("Nodes A and B must be on the same path");
        }

        final int ia = vx.tournee.indexOf(a);
        final int ib = vx.tournee.indexOf(b);

        final int min = Math.min(ia, ib) + 1;
        final int max = Math.max(ia, ib) - 1;

        // inverser le centre du chemin
        for (int i = min; i < max; i++) {
            vx.tournee.addElementAtIndex(i, vx.tournee.remove(max));
        }
    }

    @Override
    public Operation revert() {
        return this;
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
