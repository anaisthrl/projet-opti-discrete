package project.Operations;

import project.Model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Neighbourhood {
    private final Random random = new Random();
    private int tailleVoisinage = 30;
    private Graph graph;

    //RECUIT

    public Neighbourhood() {

    }
    public Neighbourhood(Graph graph) {
        this.graph = graph;
    }


    public Operation choixTransforAleatoire(Graph graph) {
        int randOperation = random.nextInt(3);

        final List<Node> nodes = graph.getNodes();
        final int nodeSize = nodes.size();
        Vehicule vehicule;
        int tourneeSize;

        int relocate_inter = 0;
        int relocate_intra = 1;
        int exchange = 2;
        int twoopt = 3;

        switch (randOperation) {
            case 0 : return  new Relocate(graph);
            case 1 : return new RelocateInter(graph);
            case 2 : return new Exchange(graph);
            //case 3 : return new TwoOpt(graph);
           /* case 0:
                return new SwapInter(
                        nodes.get(random.nextInt(nodeSize - 1) + 1),
                        nodes.get(random.nextInt(nodeSize - 1) + 1)
                );
            case 1:
                vehicule = graph.getVehicules().get(random.nextInt(graph.getVehicules().size()));
                tourneeSize = vehicule.tournee.size() - 2;
               /*if(tourneeSize < 2){
                    final Node node1 =getRandomPoint(graph);
                    return new Relocate(
                            graph.getVehiculeContaining(node1).tournee,
                            graph.getVehiculeContaining(getRandomPoint(graph)).tournee,
                            node1);
                }
                return new SwapIntra(
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1),
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1)
                );
            case 2:
                final Node node1 =getRandomPoint(graph);
                final Node node2 =getRandomPoint(graph);
                return new Relocate(
                        graph.getVehiculeContaining(node1).tournee,
                        graph.getVehiculeContaining(node2).tournee,
                        node1);
            case 3:
                vehicule = graph.getVehicules().get(random.nextInt(graph.getVehicules().size()));
                tourneeSize = vehicule.tournee.size() - 2;
                /*if(tourneeSize < 2){
                    final Node node = getRandomPoint(graph);
                    return new Relocate(
                            graph.getVehiculeContaining(node).tournee,
                            graph.getVehiculeContaining(getRandomPoint(graph)).tournee,
                            node);
                }
                return new TwoOpt(
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1),
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1)
                );*/
            default: return null;
        }
    }


    /////TABOU

    public List<Operation> getVoisinage(Graph graph) {
        Node firstNode = getRandomPoint(graph);
        List<Operation> op = getClosestNodes(graph, firstNode, tailleVoisinage - 2)
                .stream()
                .map(point2 -> (Operation) new SwapInter(firstNode, point2))
                .filter(operation -> operation.isValid(graph))
                .collect(Collectors.toList());
        op.addAll(bougerPointDeChemin(graph));
        return op;
    }

    /**
     * Move proche (long a l'execution)
     *
     * @param graph graph
     * @return liste d'op qui bougent un point d'un chemin a un autre
     */
    private List<Operation> bougerPointDeChemin(Graph graph) {
        List<Operation> operationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Node node1 = getRandomPoint(graph);
            Node node2 = getClosestNodeNotInTournee(graph, node1, graph.getVehiculeContaining(node1).tournee);
            Operation operation = new Relocate(graph.getVehiculeContaining(node1).tournee, graph.getVehiculeContaining(node2).tournee, node1);
            if (operation.isValid(graph))
                operationList.add(operation);
        }
        return operationList;
    }

    /**
     * @param node   node
     * @param amount nombre de points a renvoyer
     * @return liste des points les plus proche de node
     */
    private List<Node> getClosestNodes(Graph graph, Node node, int amount) {
        List<Node> closestNodes = new ArrayList<>();
        List<Node> nodes = graph.getVehicules().stream().map((vehicule -> vehicule.tournee.stream().toList()))
                .flatMap(Collection::parallelStream)
                .filter(_node -> !(_node instanceof Depot))
                .collect(Collectors.toList());
        for (int i = 0; i < amount; i++) {
            closestNodes.add(
                    nodes.stream()
                            .filter(_node ->
                                    _node.getDistanceToNode(node) == nodes.stream()
                                            .mapToDouble(_node2 -> _node.getDistanceToNode(node))
                                            .min()
                                            .orElseThrow()
                            ).findFirst()
                            .orElseThrow()
            );
            nodes.remove(closestNodes.get(i));
        }
        return closestNodes;
    }

    /**
     * @param graph   graph
     * @param node    node initial
     * @param tournee tournee dans lequel le node suivant ne dois pas se trouver
     * @return node le plus proche qui ne se trouve pas dans le tournee
     */
    private Node getClosestNodeNotInTournee(Graph graph, Node node, List<Node>  tournee) {

        return getClosestNodes(graph, node, 20).stream()
                .filter(point1 -> !tournee.contains(point1))
                .findFirst()
                .orElseThrow();

    }

    /**
     * @param graph Graph
     * @return point aleatoire (non depot)
     */
    private Node getRandomPoint(Graph graph) {
        List<Node> points = getAllPointsWithoutDepot(graph);
        return points.get(random.nextInt(points.size()));
    }

    /**
     * @param graph Graph
     * @return tous les points de la graph (non depot)
     */
    private List<Node> getAllPointsWithoutDepot(Graph graph) {
        return graph.getVehicules().stream().map((vehicule -> vehicule.tournee.stream().toList()))
                .flatMap(Collection::parallelStream)
                .filter(node -> !(node instanceof Depot))
                .toList();
    }

    /**
     * Génération de tout les voisins pour tout les points du graph
     *
     * @return neighbors
     */
    public ArrayList<ArrayList<Vehicule>> generateNeighbors() {
        ArrayList<ArrayList<Vehicule>> neighbors = new ArrayList<>();

        ArrayList<Vehicule> storedSolution = this.graph.cloneVehicules();

        for (int vehicleIndex = 0; vehicleIndex < storedSolution.size(); vehicleIndex++) {
            for (int pointIndex = 0; pointIndex < storedSolution.get(vehicleIndex).getTournee().size(); pointIndex++) {
                neighbors.addAll(generateRelocateInternNeighbors(vehicleIndex, pointIndex));
                neighbors.addAll(generateRelocateExternNeighbors(vehicleIndex, pointIndex));
//                neighbors.addAll(generateExchangeInternNeighbors(vehicleIndex, pointIndex));
                this.graph.setVehicules(this.graph.cloneVehicules());
            }
        }

        //On remet la solution sauvegardée au graph
        this.graph.setVehicules(storedSolution);

        return neighbors;

    }

    /**
     *Génération de TOUT les voisins avec RELOCATE INTERNE pour un véhicule et un point donné
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
     *
     * Génération de TOUT les voisins avec RELOCATE EXTERNE pour un véhicule et un point donné
     * @param vehicleIndex
     * @param pointIndex
     * @return
     */
    public ArrayList<ArrayList<Vehicule>> generateRelocateExternNeighbors(int vehicleIndex, int pointIndex) {
        ArrayList<ArrayList<Vehicule>> neighbors = new ArrayList<>();

        ArrayList<Vehicule> defaultSolution = this.graph.cloneVehicules();

        Vehicule vehicleToModify = this.graph.getVehicules().get(vehicleIndex);
        Node clientToMove = vehicleToModify.tournee.remove(pointIndex);
        if (vehicleToModify.getTournee().isEmpty()) {
            this.graph.getVehicules().remove(vehicleIndex);
        }

        ArrayList<Vehicule> intermediateSolution = this.graph.cloneVehicules();

        for (int vehicleInsertIndex = 0; vehicleInsertIndex < intermediateSolution.size(); vehicleInsertIndex++) {

            if (vehicleToModify.getTournee().isEmpty() || vehicleInsertIndex != vehicleIndex) {

                Vehicule vehicleToInsert = this.graph.getVehicules().get(vehicleInsertIndex);

                if (vehicleToInsert.getLongueur() + clientToMove.getPoids() <= Vehicule.MAX_CAPACITY) {
                    List<Node> clientsToInsert = vehicleToInsert.getTournee();
                    for (int pointInsertIndex = 0; pointInsertIndex <= clientsToInsert.size(); pointInsertIndex++) {
                        vehicleToInsert.tournee.add(pointInsertIndex, clientToMove);
                        neighbors.add(this.graph.cloneVehicules());
                        vehicleToInsert.tournee.remove(pointInsertIndex);
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

}
