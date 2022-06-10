package project.Operations;

import project.Model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

public class TwoOpt extends Operation {

    private final Node a;
    private final Node b;

    public TwoOpt(Node a, Node b) {

        this.a = a;
        this.b = b;
    }

    @Override
    public void apply(Graph graph) {

        final Vehicule vehicule1 = graph.getVehiculeContaining(a);
        final Vehicule vehicule2 = graph.getVehiculeContaining(b);

        if (vehicule1 == null) {
            throw new IllegalArgumentException("Node A not in solution");
        }
        if (vehicule2 == null) {
            throw new IllegalArgumentException("Node B not in solution");
        }


        if (vehicule1 == vehicule2) {

            final int ia = vehicule1.tournee.indexOf(a);
            final int ib = vehicule1.tournee.indexOf(b);

            final int min = Math.min(ia, ib) + 1;
            final int max = Math.max(ia, ib) - 1;
/*
        List<Node> clientCopy = new ArrayList<>(vehicule1.tournee.subList(min, max));
        Collections.reverse(clientCopy);
        List<Node> finalList = new ArrayList<>(vehicule1.tournee.subList(0, max));
        finalList.addAll(clientCopy);
        finalList.addAll(vehicule1.tournee.subList(min, vehicule1.tournee.size()));
        graph.getVehiculeContaining(a).setTournee(new ArrayList<Node>(finalList));
*/

            // inverser le centre du chemin
            for (int i = min; i < max; i++) {
                vehicule1.tournee.add(i, vehicule1.tournee.remove(max));
            }
        }
    }

    @Override
    public Operation revert() {
        return new TwoOpt(this.b, this.a);
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
