package project.Operations;

import project.Model.*;

import java.util.*;
import java.util.function.UnaryOperator;

public class TwoOpt extends Operation {

    private final Node a;
    private final Node b;
    private Graph graph;
    private final Random random  = new Random();

    public TwoOpt(Graph graph) {
        this.graph = graph;
        this.a = null;
        this.b = null;
    }
    public TwoOpt(Node a, Node b) {

        this.a = a;
        this.b = b;
    }

    public void doTransfo(Vehicule vehicule, int client1Index, int client2Index){
        if(client1Index != client2Index && client2Index != vehicule.getTournee().size()){
            int client1adjIndex = client1Index+1;
            int client2adjIndex = client2Index+1;
            Node client1adj, client2;

            if (client1Index > client2Index) {
                int temp= client1Index;
                client1Index = client2Index;
                client2Index = temp;
            }

            Collections.reverse(vehicule.getTournee().subList(client1adjIndex, client2Index));

            client2 = vehicule.tournee.remove(client2Index);
            client1adj = vehicule.tournee.remove(client1adjIndex);

            vehicule.tournee.add(client2Index, client1adj);
            vehicule.tournee.add(client1Index, client2);
        }
    }

    @Override
    public void apply(Graph graph) {
        int vehculeIndexToMod = random.nextInt(this.graph.getVehicules().size());
        Vehicule vehiculeToMod = this.graph.getVehicules().get(vehculeIndexToMod);
        int size = vehiculeToMod.getTournee().size();

        if (size > 2) {
            int client1ToMoveIndex = random.nextInt(size);
            int client2ToMoveIndex;
            while ((client2ToMoveIndex = random.nextInt(size)) == client1ToMoveIndex);

            doTransfo(vehiculeToMod, client1ToMoveIndex, client2ToMoveIndex);
        }


        /*
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

            // inverser le centre du chemin
            for (int i = min; i < max; i++) {
                vehicule1.tournee.add(i, vehicule1.tournee.remove(max));
            }
        }*/

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
