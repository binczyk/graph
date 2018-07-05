package johnsonsShortesPaths;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;

class BellmanFord {

    private static Map<String, Double> distance = new HashMap<>();
    private static Map<String, String> previous = new HashMap<>();
    private static final String ADDITIONAL_VERTEX = "#";

    private BellmanFord() {
    }

    public static Map<String, Double> execute(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        createNewGraph(graph);
        initDistanceAndPrevList(graph, startVertex);
        relax(graph);
        checkCycle(graph);
        return distance;
    }

    private static Graph<String, DefaultWeightedEdgeCustom> createNewGraph(Graph<String, DefaultWeightedEdgeCustom> graph) {
        graph.addVertex(ADDITIONAL_VERTEX);
        for (String vertex : graph.vertexSet()) {
            if (!vertex.equalsIgnoreCase(ADDITIONAL_VERTEX)) {
                DefaultWeightedEdgeCustom edge = graph.addEdge(ADDITIONAL_VERTEX, vertex);
                graph.setEdgeWeight(edge, 0D);
            }
        }
        return graph;
    }

    private static void initDistanceAndPrevList(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        for (String vertex : graph.vertexSet()) {
            distance.put(vertex, Double.MAX_VALUE);
            previous.put(vertex, "$$$");
        }
        distance.put(startVertex, 0D);
    }

    private static void relax(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (int i = 0; i < graph.vertexSet().size(); i++) {
            for (DefaultWeightedEdgeCustom edge : graph.edgeSet()) {
                if (checkIfNewWaightIsless(edge)) {
                    distance.put(edge.getTargetCustom().toString(), distance.get(edge.getSourceCustom().toString()) + Double.valueOf(edge.getWeightCustom()));
                    previous.put(edge.getSourceCustom().toString(), edge.getTargetCustom().toString());
                }
            }
        }
    }

    private static void checkCycle(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (int i = 0; i < graph.vertexSet().size(); i++) {
            for (DefaultWeightedEdgeCustom edge : graph.edgeSet()) {
                if (checkIfNewWaightIsless(edge)) {
                    System.out.println("Graph has negative cycle");
                }
            }
        }
    }

    private static boolean checkIfNewWaightIsless(DefaultWeightedEdgeCustom edge) {
        return distance.get(edge.getTargetCustom().toString()) > distance.get(edge.getSourceCustom().toString()) + Double.valueOf(edge.getWeightCustom());
    }

    public static String getAdditionalVertex() {
        return ADDITIONAL_VERTEX;
    }
}

