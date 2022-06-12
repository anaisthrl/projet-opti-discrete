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
        Vehicule vehicule1 = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));
        Vehicule vehicule2 = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));
        this.a = vehicule1.tournee.get(random.nextInt(vehicule1.getTournee().size()));
        this.b = vehicule2.tournee.get(random.nextInt(vehicule2.getTournee().size()));
    }
    public TwoOpt(Node a, Node b) {

        this.a = a;
        this.b = b;
    }

    public Graph OPTOnce(Graph graph, int posA, int posB, int nbV) {
        ArrayList<Node> clients = (ArrayList<Node>) graph.getVehicules().get(nbV).getTournee();
        List<Node> clientCopy = new ArrayList<>(clients.subList(posA + 1, posB + 1));
        Collections.reverse(clientCopy);
        List<Node> finalList = new ArrayList<>(clients.subList(0, posA + 1));
        finalList.addAll(clientCopy);
        finalList.addAll(clients.subList(posB + 1, clients.size()));
        graph.getVehicules().get(nbV).setTournee(new ArrayList<>(finalList));
        return graph;
    }

    public ArrayList<Graph> OPTAll(Graph graph) {
        ArrayList<Graph> maps = new ArrayList<>();
        for (int k = 0; k <= graph.getVehicules().size() - 1; k++) {
            ArrayList<Node> clients = (ArrayList<Node>) graph.getVehicules().get(k).getTournee();
            if (clients.size() >= 4)
                for (int i = 1; i < clients.size() - 1; i++) {
                    Node cl1 = clients.get(i);
                    Node cl2 = clients.get(i + 1);

                    for (int j = i + 2; j < clients.size() - 2; j++) {
                        Node cl3 = clients.get(j);
                        Node cl4 = clients.get(j + 1);

                        if (AreTwoEdgeDisjoint(cl1, cl2, cl3, cl4)) {
                            Graph mapClone = graph.cloneMap();
                            Graph m = OPTOnce(mapClone, i, j, k);
                            maps.add(m);

                        }
                    }
                }
        }
        return maps;
    }

    public boolean AreTwoEdgeDisjoint(Node cl1, Node cl2, Node cl3, Node cl4) {
        if (AreTwoClientEquals(cl1, cl2) || AreTwoClientEquals(cl3, cl4)) {
            return false;
        }

        return !AreTwoClientEquals(cl1, cl3) && !AreTwoClientEquals(cl1, cl4) && !AreTwoClientEquals(cl2, cl3) && !AreTwoClientEquals(cl2, cl4);
    }

    public boolean AreTwoClientEquals(Node cl1, Node cl2) {
        return Objects.equals(cl1.getPos().getX(), cl2.getPos().getX()) && Objects.equals(cl1.getPos().getY(), cl2.getPos().getY());
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
        vA.updateNbColis();
        vB.updateNbColis();
        vA.updateDistanceTournee();
        vB.updateDistanceTournee();


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
