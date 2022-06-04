package Operations;

import Model.*;

import java.util.Objects;
import java.util.Random;

public class Relocate extends Operation {

    private final static Random random = new Random();
    private final Tournee firstTournee;
    private final Tournee secTournee;
    private final Node node;
    private final Integer startPoint;
    private final Integer endPoint;

    public Relocate(Tournee tournee1, Tournee tournee2, Node node) {
        this.firstTournee = tournee1;
        this.secTournee = tournee2;
        this.node = node;
        this.startPoint = tournee1.indexOf(this.node);
        this.endPoint = random.nextInt(tournee2.size());
        if (this.node instanceof Depot) {
            throw new IllegalArgumentException("le node ne peut pas etre un dépot");
        }
    }

    private Relocate(Tournee tournee1, Tournee tournee2, Node node, int startPoint, int endPoint) {
        this.firstTournee = tournee1;
        this.secTournee = tournee2;
        this.node = node;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        if (node instanceof Depot) {
            throw new IllegalArgumentException("Point cannot be depot");
        }
    }

    @Override
    public void apply(Graph graph) {
        this.firstTournee.remove(node);
        this.secTournee.add(endPoint, node);
    }

    @Override
    public Operation revert() {
        return new Relocate(secTournee, firstTournee, node, endPoint, startPoint);
    }

    @Override
    public boolean isValid(Graph graph) {
        boolean valid = true;
        apply(graph);
        if (graph.getVehiculeFromTournee(this.firstTournee).getNbColis() > Vehicule.MAX_CAPACITY) {
            valid = false;
        }
        if (graph.getVehiculeFromTournee(this.secTournee).getNbColis() > Vehicule.MAX_CAPACITY) {
            valid = false;
        }
        revert().apply(graph);
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relocate that = (Relocate) o;
        return Objects.equals(firstTournee, that.firstTournee) && Objects.equals(secTournee, that.secTournee) && Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstTournee, secTournee, node);
    }
}