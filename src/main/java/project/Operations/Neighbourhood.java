package project.Operations;

import project.Model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Neighbourhood {
    private final Random random = new Random();
    private int tailleVoisinage = 30;

    //RECUIT

    private final int swap_inter = 0;
    private final int swap_intra = 1;
    private final int twoopt = 2;
    private final int relocate = 3;;

    public Neighbourhood() {

    }


    public Operation getRandomVoisinage(Graph graph) {
        int randOperation = random.nextInt(4);

        final List<Node> nodes = graph.getNodes();
        final int nodeSize = nodes.size();
        Vehicule vehicule;
        int tourneeSize;

        switch (randOperation) {
            case swap_inter:
                return new SwapInter(
                        nodes.get(random.nextInt(nodeSize - 1) + 1),
                        nodes.get(random.nextInt(nodeSize - 1) + 1)
                );
            case swap_intra:
                vehicule = graph.getVehicules().get(random.nextInt(graph.getVehicules().size()));
                tourneeSize = vehicule.tournee.size() - 2;
                if(tourneeSize < 2){
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
            case twoopt:
                vehicule = graph.getVehicules().get(random.nextInt(graph.getVehicules().size()));
                tourneeSize = vehicule.tournee.size() - 2;
                if(tourneeSize < 2){
                    final Node node1 = getRandomPoint(graph);
                    return new Relocate(
                            graph.getVehiculeContaining(node1).tournee,
                            graph.getVehiculeContaining(getRandomPoint(graph)).tournee,
                            node1);
                }
                return new TwoOpt(
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1),
                        vehicule.tournee.get(random.nextInt(tourneeSize-1) +1)
                );
            case relocate:
                final Node node1 =getRandomPoint(graph);
                final Node node2 =getRandomPoint(graph);
                return new Relocate(
                        graph.getVehiculeContaining(node1).tournee,
                        graph.getVehiculeContaining(node2).tournee,
                        node1);
            default:
                return new SwapInter(
                        nodes.get(random.nextInt(nodeSize - 1) + 1),
                        nodes.get(random.nextInt(nodeSize - 1) + 1)
                );

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
     * Move random
     *
     * @param graph graph
     * @return liste d'op qui bougent un point d'un chemin a un autre
     */
//    private List<Operation> bougerPointDeCheminRandom(Graph graph) {
//        List<Operation> operationList = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            Node node1 = getRandomPoint(graph);
//            Node node2 = getRandomPoint(graph);
//            operationList.add(new MoveFromCheminToAnother(graph.getCheminContaining(node1), graph.getCheminContaining(node2), node1));
//        }
//        return operationList;
//    }

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
        //ALED LE TEMPS
//		List<Point> allPoints = getAllPointsWithoutDepot(graph);
//		return allPoints
//				.stream()
//				.filter(point1 -> !tournee.contains(point1))List<Node>
//				.filter(point1 ->
//						point1.distance(node) == allPoints.stream()
//								.mapToDouble(point2 -> point2.distance(node))
//								.min()
//								.orElseThrow()
//				).findFirst()
//				.orElseThrow();

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

}
