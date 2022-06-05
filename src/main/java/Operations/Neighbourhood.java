package Operations;

import Model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Neighbourhood {
    private final Random random = new Random();
    private int tailleVoisinage = 30;

    //RECUIT

    private final double w_swap = 1;
    private final double w_2opt = 2;
    private final double w_pathSwap = 0;
    private double w_sum = 0;

    public Neighbourhood() {
        w_sum = w_swap + w_2opt + w_pathSwap;
    }


    public Operation getRandomVoisinage(Graph solution) {
        double rand_type = random.nextDouble() * w_sum;

        final List<Node> nodes = solution.getNodes();
        final int points$size = nodes.size();

        if (rand_type < w_swap) {
            return new Swap(
                    nodes.get(random.nextInt(points$size - 1) + 1),
                    nodes.get(random.nextInt(points$size - 1) + 1)
            );
        }
        rand_type -= w_swap;
        if (rand_type < w_2opt) {
            final Vehicule vehicule = solution.getVehicules().get(random.nextInt(solution.getVehicules().size()));
            final int tourneeSize = vehicule.tournee.size() - 2;

            return new TwoOpt(
                    vehicule.tournee.get(random.nextInt(tourneeSize) + 1),
                    vehicule.tournee.get(random.nextInt(tourneeSize) + 1)
            );
        }
        rand_type -= w_2opt;
        if (rand_type < w_pathSwap) {
            return new Swap(
                    nodes.get(random.nextInt(points$size - 1) + 1),
                    nodes.get(random.nextInt(points$size - 1) + 1)
            );
        }

        return null;
    }


    /////TABOU

    public List<Operation> getVoisinage(Graph graph) {
        Node firstNode = getRandomPoint(graph);
        List<Operation> op = getClosestNodes(graph, firstNode, tailleVoisinage - 2)
                .stream()
                .map(point2 -> (Operation) new Swap(firstNode, point2))
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
    private Node getClosestNodeNotInTournee(Graph graph, Node node, Tournee tournee) {
        //ALED LE TEMPS
//		List<Point> allPoints = getAllPointsWithoutDepot(graph);
//		return allPoints
//				.stream()
//				.filter(point1 -> !tournee.contains(point1))
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
