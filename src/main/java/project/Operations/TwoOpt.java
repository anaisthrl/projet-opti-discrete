package project.Operations;

import project.Model.*;

import java.util.*;

public class TwoOpt extends Operation {

    private final Node a;
    private final Node b;
    private Graph graph;
    private final Random random  = new Random();

    public TwoOpt(Graph graph) {
        this.graph = graph;
        Vehicule vehicule1 = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));
        Vehicule vehicule2 = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));
        this.a = vehicule1.tournee.get(random.nextInt(vehicule1.getTournee().size()));
        this.b = vehicule2.tournee.get(random.nextInt(vehicule2.getTournee().size()));
    }

    public TwoOpt(Node a, Node b) {

        this.a = a;
        this.b = b;
    }

    public void doTransfo(Vehicule vehicule, int client1Ind, int client2Ind){

        if(client1Ind != client2Ind && client2Ind != vehicule.getTournee().size()){
            int client1Adj = client1Ind+1;
            int client2Adj = client2Ind+1;

            if (client1Ind > client2Ind) {
                int temp= client1Ind;
                client1Ind = client2Ind;
                client2Ind = temp;
            }

            Collections.reverse(vehicule.getTournee().subList(client1Adj, client2Ind));

            Node client1adj, client2;
            client2 = vehicule.tournee.remove(client2Ind);
            client1adj = vehicule.tournee.remove(client1Adj);

            vehicule.tournee.add(client2Ind, client1adj);
            vehicule.tournee.add(client1Ind, client2);
        }
    }

    @Override
    public void apply(Graph graph) {
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
