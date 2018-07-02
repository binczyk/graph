import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;

public class BellmanFord {

    private static Map<String, Double> distance = new HashMap<>();
    private static Map<String, String> previous = new HashMap<>();
    private static final String additionalVertex = "#";

    public static void execute(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        initDistanceAndPrevList(graph, startVertex);
        relax(graph);
    }

    private static void createNewGraph(Graph<String, DefaultWeightedEdgeCustom> graph) {
        graph.addVertex(additionalVertex);
        for (String vertex : graph.vertexSet()) {
            if (!vertex.equalsIgnoreCase(additionalVertex)) {
                DefaultWeightedEdgeCustom edge = graph.addEdge(additionalVertex, vertex);
                graph.setEdgeWeight(edge, 0D);
            }
        }
    }

    private static Graph<String, DefaultWeightedEdgeCustom> removeAdditionalVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        graph.removeVertex(additionalVertex);
        return graph;
    }

    private static void initDistanceAndPrevList(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        for (String vertex : graph.vertexSet()) {
            distance.put(vertex, Double.MAX_VALUE);
            previous.put(vertex, "###");
        }
        distance.put(startVertex, 0D);
    }

    private static void relax(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (int i = 0; i < graph.vertexSet().size(); i++) {
            for (DefaultWeightedEdgeCustom edge : graph.edgeSet()) {
                if (checkIfNewWaightIsless(edge)) {
                    distance.put(edge.getSourceCustom().toString(), distance.get(edge.getTargetCustom().toString()) + Double.valueOf(edge.getWeightCustom()));
                    previous.put(edge.getSourceCustom().toString(), edge.getTargetCustom().toString());
                }
            }
        }
    }

    private static boolean checkIfNewWaightIsless(DefaultWeightedEdgeCustom edge) {
        return distance.get(edge.getSourceCustom().toString()) >
                distance.get(edge.getTargetCustom().toString()) + Double.valueOf(edge.getWeightCustom());
    }

}

