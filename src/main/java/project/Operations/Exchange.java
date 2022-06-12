package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

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
        if (a instanceof Depot) {
            throw new IllegalArgumentException("A ne peut pas etre le Depot");
        }
        if (b instanceof Depot) {
            throw new IllegalArgumentException("B ne peut pas etre le Depot");
        }

        this.a = a;
        this.b = b;
    }

    public void doTransfo(Vehicule vehicule, int fstClientInd, int secClientInd) {
        if (secClientInd != fstClientInd) {
            Node client1, client2;
            //On retire le plus grand en premier pour ne pas décaler les index de la liste
            if (fstClientInd > secClientInd) {
                int temp = fstClientInd;
                fstClientInd = secClientInd;
                secClientInd = temp;
            }
            client2 = vehicule.tournee.remove(secClientInd);
            client1 = vehicule.tournee.remove(fstClientInd);
            vehicule.tournee.add(fstClientInd, client2);
            vehicule.tournee.add(secClientInd, client1);

        }
    }

    @Override
    public void apply(Graph graph) {
        int vehiculeIndex = random.nextInt(this.graph.getVehicules().size());
        Vehicule vehicule = this.graph.getVehicules().get(vehiculeIndex);
        int size = vehicule.getTournee().size();

        if (size > 2) {
            int fstClientInd = random.nextInt(size);
            int secClientInd;
            while ((secClientInd = random.nextInt(size)) == fstClientInd) ;

            doTransfo(vehicule, fstClientInd, secClientInd);
        }
    }

    @Override
    public Operation revert() {
        return this;
    }

    @Override
    public boolean isValid(Graph graph) {
        return false;
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
}
