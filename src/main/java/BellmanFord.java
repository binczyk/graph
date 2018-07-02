import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;

public class BellmanFord {

    public static void execute(Graph<String, DefaultWeightedEdgeCustom> graph) {
        Graph<String, DefaultWeightedEdgeCustom> extraGraph = initExtraGraph(graph);
    }

    private static Graph<String, DefaultWeightedEdgeCustom> initExtraGraph(Graph<String, DefaultWeightedEdgeCustom> graph) {
        Graph<String, DefaultWeightedEdgeCustom> extraGraph = graph;
        String additionalVertex = "#";
        extraGraph.addVertex(additionalVertex);
        for (String vertex : extraGraph.vertexSet()) {
            DefaultWeightedEdgeCustom edge = extraGraph.addEdge(additionalVertex, vertex);
            extraGraph.setEdgeWeight(edge, 0D);
        }
        return extraGraph;
    }


    private static void relax(Graph<String, DefaultWeightedEdgeCustom> graph) {
      /*  for ( : distance.keySet()) {
            for (String relatedVertex : graph.getAllEdges()) {
                if (distance.get(sourceVertex) != Integer.MAX_VALUE && distance.get(sourceVertex) + graph.getEdge(sourceVertex, relatedVertex) < distance.get(relatedVertex)) {
                    distance.put(relatedVertex) = distance.get(sourceVertex) + weight;
                }
            }
        }*/
    }

    /*private static void relax(Graph<String, DefaultWeightedEdgeCustom> graph, Map<String, Integer> distance, Map<String, Integer> predecesors) {
        for (String sourceVertex : distance.keySet()) {
            for (String relatedVertex : graph.getAllEdges()) {
                if (distance.get(sourceVertex) != Integer.MAX_VALUE && distance.get(sourceVertex) + graph.getEdge(sourceVertex, relatedVertex) < distance.get(relatedVertex)) {
                    distance.put(relatedVertex) = distance.get(sourceVertex) + weight;
                }
            }
        }
    }*/

    private static Map initDistances(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        Map<String, Integer> distance = new HashMap<>();
        for (String vertex : graph.vertexSet()) {
            distance.put(vertex, Integer.MAX_VALUE);
        }
        distance.put(startVertex, 0);

        return distance;
    }
}
