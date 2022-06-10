package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Relocate extends Operation {

    private final static Random random = new Random();
    private final List<Node> firstTournee;
    private final List<Node> secTournee;
    private final Node node;
    private final Integer startPoint;
    private final Integer endPoint;
    private Graph graph;

    public Relocate(Graph graph) {
        this.firstTournee = new ArrayList<>();
        this.secTournee = new ArrayList<>();
        this.node = null;
        this.startPoint = 0;
        this.endPoint = 0;
        this.graph = graph;
    }

    public Relocate(List<Node> tournee1, List<Node> tournee2, Node node) {
        this.firstTournee = tournee1;
        this.secTournee = tournee2;
        this.node = node;
        this.startPoint = tournee1.indexOf(this.node);
        this.endPoint = random.nextInt(tournee2.size());
        if (this.node instanceof Depot) {
            throw new IllegalArgumentException("le node ne peut pas etre un dépot");
        }
    }

    private Relocate(List<Node> tournee1, List<Node> tournee2, Node node, int startPoint, int endPoint) {
        this.firstTournee = tournee1;
        this.secTournee = tournee2;
        this.node = node;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        if (node instanceof Depot) {
            throw new IllegalArgumentException("Point cannot be depot");
        }
    }

    public void relocateIntra() {
        Vehicule vehicleToModify = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));

        List<Node> visitToModify = vehicleToModify.tournee;

        if (visitToModify.size() > 1) {
            int clientToMoveIndex = random.nextInt(visitToModify.size());

            if (!(vehicleToModify.tournee.get(clientToMoveIndex) instanceof Depot)) {
                Node clientToMove = vehicleToModify.tournee.remove(clientToMoveIndex);
                if (clientToMove instanceof Depot) {
                    System.out.println("DEPOOOOOOOOOOT");
                }
                int insertIndex;
                while ((insertIndex = random.nextInt(visitToModify.size() + 1)) == clientToMoveIndex) ;
                vehicleToModify.tournee.add(insertIndex, clientToMove);
            }
        }

    }

    @Override
    public void apply(Graph graph) {
       // relocateIntra();

        if (graph.getVehiculeFromTournee(secTournee).getNbColis() + node.getPoids() <= Vehicule.MAX_CAPACITY) {
            this.firstTournee.remove(node);
            this.secTournee.add(endPoint, node);
        }

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
