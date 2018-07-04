import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;

class Dijkstra {

    private static DijkstraResult dijkstraResult = new DijkstraResult();
    private static List<String> vertices = new ArrayList<>();

    public static DijkstraResult execte(Graph<String, DefaultWeightedEdgeCustom> graph, String vertex) {
        initAdditionals(graph, vertex);
        findPath(graph);
        return dijkstraResult;
    }

    private static void findPath(Graph<String, DefaultWeightedEdgeCustom> graph) {
        while (!vertices.isEmpty()) {
            String cheapestVertex = getCheapestVertex();
            checkNeighbours(cheapestVertex, graph);

        }
    }

    private static String getCheapestVertex() {
        Double cost = Double.MAX_VALUE;
        String cheapestVertex = "";
        for (String vertex : vertices) {
            if (cost > dijkstraResult.getDistance().get(vertex)) {
                cost = dijkstraResult.getDistance().get(vertex);
                cheapestVertex = vertex;
            }
        }
        vertices.remove(cheapestVertex);
        return cheapestVertex;
    }

    private static void checkNeighbours(String cheapestVertex, Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String destVertex : graph.vertexSet()) {
            if (vertices.contains(destVertex) && graph.getEdge(cheapestVertex, destVertex) != null) {
                if (checkIfNewCostIsLower(cheapestVertex, graph, destVertex)) {
                    dijkstraResult.getDistance().put(destVertex, dijkstraResult.getDistance().get(cheapestVertex) +
                                                                 Double.parseDouble(graph.getEdge(cheapestVertex, destVertex).getWeightCustom()));
                    dijkstraResult.getPredecessors().put(destVertex, cheapestVertex);
                }
            }
        }
    }

    private static boolean checkIfNewCostIsLower(String cheapestVertex, Graph<String, DefaultWeightedEdgeCustom> graph, String destVertex) {
        return dijkstraResult.getDistance().get(destVertex) >
               dijkstraResult.getDistance().get(cheapestVertex) + Double.parseDouble(graph.getEdge(cheapestVertex, destVertex).getWeightCustom());
    }

    private static void initAdditionals(Graph<String, DefaultWeightedEdgeCustom> graph, String vertex) {
        initCostsList(graph, vertex);
        initPredecesorsList(graph);
        initVertexList(graph);
    }

    private static void initCostsList(Graph<String, DefaultWeightedEdgeCustom> graph, String sourceVertex) {
        for (String vertex : graph.vertexSet()) {
            dijkstraResult.getDistance().put(vertex, Double.MAX_VALUE);
        }
        dijkstraResult.getDistance().put(sourceVertex, 0D);
    }

    private static void initPredecesorsList(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String vertex : graph.vertexSet()) {
            dijkstraResult.getPredecessors().put(vertex, "$$$");
        }
    }

    private static void initVertexList(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String vertex : graph.vertexSet()) {
            vertices.add(vertex);
            dijkstraResult.getVertices().add(vertex);
        }
    }
}
