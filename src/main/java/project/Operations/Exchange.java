package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Exchange extends Operation {
    private final Node a;
    private final Node b;
    private final static Random random = new Random();
    private Graph graph;

    public Exchange(Graph graph) {
        this.graph = graph;
        this.a = null;
        this.b = null;
    }
    public Exchange(Node a, Node b) {
        if (a instanceof Depot) throw new IllegalArgumentException("A ne peut pas etre le Depot");
        if (b instanceof Depot) throw new IllegalArgumentException("B ne peut pas etre le Depot");

        this.a = a;
        this.b = b;
    }

    public void doTransfo(Vehicule vehicule, int client1Index, int client2Index) {
        if (client2Index != client1Index) {
            Node client1, client2;
            //On retire le plus grand en premier pour ne pas décaler les index de la liste
            if (client1Index > client2Index) {
                int temp = client1Index;
                client1Index = client2Index;
                client2Index = temp;
            }
            client2 = vehicule.tournee.remove(client2Index);
            client1 = vehicule.tournee.remove(client1Index);
            vehicule.tournee.add(client1Index, client2);
            vehicule.tournee.add(client2Index, client1);

        }
    }

    @Override
    public void apply(Graph graph) {
            int vehicleToModifyIndex = random.nextInt(this.graph.getVehicules().size());
            Vehicule vehicleToModify = this.graph.getVehicules().get(vehicleToModifyIndex);
            int size = vehicleToModify.getTournee().size();

            if (size > 2) {
                int client1ToMoveIndex = random.nextInt(size);
                int client2ToMoveIndex;
                while ((client2ToMoveIndex = random.nextInt(size)) == client1ToMoveIndex);

                doTransfo(vehicleToModify, client1ToMoveIndex, client2ToMoveIndex);
            }

        /*final Vehicule vA = graph.getVehiculeContaining(a);
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
        vB.tournee.addAll(tmp2);*/
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
