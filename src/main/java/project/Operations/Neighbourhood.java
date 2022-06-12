package project.Operations;

import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neighbourhood {
    private final Random random = new Random();
    private int tailleVoisinage = 30;
    private Graph graph;

    public Neighbourhood() {

    }

    public Neighbourhood(Graph graph) {
        this.graph = graph;
    }


    public Operation choixTransforAleatoire(Graph graph) {
        int randOperation = random.nextInt(3);

        final List<Node> nodes = graph.getNodes();

        switch (randOperation) {
            case 0:
                return new Relocate(graph);
            case 1:
                return new RelocateInter(graph);
            case 2:
                return new Exchange(graph);
            //case 3 : return new TwoOpt(graph);
            default:
                return null;
        }
    }

    /**
     * Génération de tout les voisins pour tout les points du graph
     *
     * @return neighbours
     */
    public ArrayList<ArrayList<Vehicule>> generateNeighbours() {
        ArrayList<ArrayList<Vehicule>> neighbours = new ArrayList<>();

        ArrayList<Vehicule> storedSolution = this.graph.cloneVehicules();

        for (int vehicleIndex = 0; vehicleIndex < storedSolution.size(); vehicleIndex++) {
            for (int pointIndex = 0; pointIndex < storedSolution.get(vehicleIndex).getTournee().size(); pointIndex++) {
                neighbours.addAll(generateRelocateInternNeighbors(vehicleIndex, pointIndex));
                neighbours.addAll(generateRelocateExternNeighbors(vehicleIndex, pointIndex));
                neighbours.addAll(generateExchangeInternNeighbors(vehicleIndex, pointIndex));
                this.graph.setVehicules(this.graph.cloneVehicules());
            }
        }

        this.graph.setVehicules(storedSolution);

        return neighbours;

    }

    /**
     * Génération de TOUT les voisins avec RELOCATE INTERNE pour un véhicule et un point donné
     */
    public ArrayList<ArrayList<Vehicule>> generateRelocateInternNeighbors(int vehicleIndex, int pointIndex) {
        ArrayList<ArrayList<Vehicule>> neighbors = new ArrayList<>();
        ArrayList<Vehicule> storedSolution = this.graph.cloneVehicules();
        Vehicule vehicleToModify = this.graph.getVehicules().get(vehicleIndex);

        int size = vehicleToModify.getTournee().size();

        if (size > 1) {
            Node clientToMove = vehicleToModify.tournee.remove(pointIndex);

            for (int i = 0; i < size; i++) {
                if (i != pointIndex) {
                    vehicleToModify.tournee.add(i, clientToMove);
                    neighbors.add(this.graph.cloneVehicules());
                    vehicleToModify.tournee.remove(i);
                }
            }
            //On remet la solution sauvegardée au graph
            this.graph.setVehicules(storedSolution);
        }


        return neighbors;
    }

    /**
     * Génération de TOUT les voisins avec RELOCATE EXTERNE pour un véhicule et un point donné
     *
     * @param vehiculeInd
     * @param pointInd
     * @return
     */
    public ArrayList<ArrayList<Vehicule>> generateRelocateExternNeighbors(int vehiculeInd, int pointInd) {
        ArrayList<ArrayList<Vehicule>> neighbors = new ArrayList<>();

        ArrayList<Vehicule> defaultSolution = this.graph.cloneVehicules();

        Vehicule vehicleToModify = this.graph.getVehicules().get(vehiculeInd);
        Node clientToMove = vehicleToModify.removeClientWithIndex(pointInd);
        if (vehicleToModify.getTournee().isEmpty()) {
            this.graph.getVehicules().remove(vehiculeInd);
        }

        ArrayList<Vehicule> intermediateSolution = this.graph.cloneVehicules();

        for (int vehicleInsertIndex = 0; vehicleInsertIndex < intermediateSolution.size(); vehicleInsertIndex++) {

            if (vehicleToModify.getTournee().isEmpty() || vehicleInsertIndex != vehiculeInd) {

                Vehicule vehicleToInsert = this.graph.getVehicules().get(vehicleInsertIndex);

                if (vehicleToInsert.getNbColis() + clientToMove.getPoids() <= Vehicule.MAX_CAPACITY) {
                    ArrayList<Node> clientsToInsert = (ArrayList<Node>) vehicleToInsert.getTournee();
                    for (int pointInsertIndex = 0; pointInsertIndex <= clientsToInsert.size(); pointInsertIndex++) {
                        vehicleToInsert.addClientWithIndex(clientToMove, pointInsertIndex);
                        neighbors.add(this.graph.cloneVehicules());
                        vehicleToInsert.removeClientWithIndex(pointInsertIndex);
                    }
                }

            }
        }


        Vehicule newVehicle = new Vehicule(graph.getDepot());
        newVehicle.addClient(clientToMove);
        this.graph.getVehicules().add(newVehicle);
        neighbors.add(this.graph.getVehicules());
        this.graph.setVehicules(defaultSolution);

        return neighbors;
    }

    /**
     * TABU
     * Génére tout les voisins en échangeant deux points d'un véhicule
     *
     * @param vehiculeInd
     * @param client1ToExchangeIndex
     * @return
     */
    public ArrayList<ArrayList<Vehicule>> generateExchangeInternNeighbors(int vehiculeInd, int client1ToExchangeIndex) {
        ArrayList<ArrayList<Vehicule>> neighbors = new ArrayList<>();
        ArrayList<Vehicule> storedSolution = this.graph.cloneVehicules();
        Vehicule vehicule = this.graph.getVehicules().get(vehiculeInd);

        int size = vehicule.getTournee().size();

        if (size >= 2) {

            for (int client2ToExchangeIndex = 0; client2ToExchangeIndex < size - 2; client2ToExchangeIndex++) {
                doExchangeTabu(vehicule, client1ToExchangeIndex, client2ToExchangeIndex);
                neighbors.add(this.graph.cloneVehicules());
            }

            //On remet la solution sauvegardée au graph
            this.graph.setVehicules(storedSolution);
        }

        return neighbors;
    }

    public void doExchangeTabu(Vehicule vehicule, int client1Index, int client2Index) {
        if (client2Index != client1Index) {
            Node client1, client2;
            //On retire le plus grand en premier pour ne pas décaler les index de la liste
            if (client1Index > client2Index) {
                int temp = client1Index;
                client1Index = client2Index;
                client2Index = temp;
            }
            client2 = vehicule.removeClientWithIndex(client2Index);
            client1 = vehicule.removeClientWithIndex(client1Index);
            vehicule.addClientWithIndex(client2, client1Index);
            vehicule.addClientWithIndex(client1, client2Index);

        }
    }

}
