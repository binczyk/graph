package johnsonsShortesPaths;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;

class Dijkstra {

    private DijkstraResult dijkstraResult;
    private List<String> vertices = new ArrayList<>();
    private Graph<String, DefaultWeightedEdgeCustom> graph;

    public Dijkstra(Graph<String, DefaultWeightedEdgeCustom> graph) {
        this.graph = graph;
    }

    public DijkstraResult execte(String vertex) {
        dijkstraResult = new DijkstraResult();
        initAdditionals(graph, vertex);
        findPath(graph);
        return dijkstraResult;
    }

    private void findPath(Graph<String, DefaultWeightedEdgeCustom> graph) {
        while (!vertices.isEmpty()) {
            String cheapestVertex = getCheapestVertex();
            checkNeighbours(cheapestVertex, graph);
        }
    }

    private String getCheapestVertex() {
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

    private void checkNeighbours(String cheapestVertex, Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String destVertex : graph.vertexSet()) {
            if (vertices.contains(destVertex) && graph.getEdge(cheapestVertex, destVertex) != null && checkIfNewCostIsLower(cheapestVertex, graph, destVertex)) {
                dijkstraResult.getDistance()
                              .put(destVertex, dijkstraResult.getDistance().get(cheapestVertex) + Double.parseDouble(graph.getEdge(cheapestVertex, destVertex).getWeightCustom()));
                dijkstraResult.getPredecessors().put(destVertex, cheapestVertex);
            }
        }
    }

    private boolean checkIfNewCostIsLower(String cheapestVertex, Graph<String, DefaultWeightedEdgeCustom> graph, String destVertex) {
        return dijkstraResult.getDistance().get(destVertex) >
               dijkstraResult.getDistance().get(cheapestVertex) + Double.parseDouble(graph.getEdge(cheapestVertex, destVertex).getWeightCustom());
    }

    private void initAdditionals(Graph<String, DefaultWeightedEdgeCustom> graph, String vertex) {
        initCostsList(graph, vertex);
        initPredecesorsList(graph);
        initVertexList(graph);
    }

    private void initCostsList(Graph<String, DefaultWeightedEdgeCustom> graph, String sourceVertex) {
        for (String vertex : graph.vertexSet()) {
            dijkstraResult.getDistance().put(vertex, Double.MAX_VALUE);
        }
        dijkstraResult.getDistance().put(sourceVertex, 0D);
    }

    private void initPredecesorsList(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String vertex : graph.vertexSet()) {
            dijkstraResult.getPredecessors().put(vertex, "$$$");
        }
    }

    private void initVertexList(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String vertex : graph.vertexSet()) {
            vertices.add(vertex);
            if (!dijkstraResult.getVertices().contains(vertex)) {
                dijkstraResult.getVertices().add(vertex);
            }
        }
    }
}
