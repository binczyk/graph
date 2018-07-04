import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Johnson {

    public static Map<String, DijkstraResult> execute(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        Map<String, Double> BFdistance = BellmanFord.execute(graph, startVertex);
        Graph<String, DefaultWeightedEdgeCustom> newGraph = removeAdditionalVertex(graph);
        newGraph = reweightGraph(newGraph, BFdistance);
        return runDijkstraForAllVertex(newGraph);
    }

    private static Map<String, DijkstraResult> runDijkstraForAllVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        Map<String, DijkstraResult> allPairsPath = new HashMap<>();
        for (String vertex : graph.vertexSet()) {
            allPairsPath.put(vertex, Dijkstra.execte(graph, vertex));
        }
        return allPairsPath;
    }

    public static Graph createGraph(File jsnoWithGraph) {
        Graph<String, DefaultWeightedEdgeCustom> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdgeCustom.class);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject data = (JSONObject) jsonParser.parse(new FileReader(jsnoWithGraph));
            createVertex(graph, data);
            createEdges(graph, data);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private static void createEdges(Graph<String, DefaultWeightedEdgeCustom> graph, JSONObject data) {
        for (Object o : (ArrayList) data.get("edge")) {
            if (checkIfcanBeParesd(o)) {
                DefaultWeightedEdgeCustom defaultWeightedEdge = graph.addEdge((String) ((JSONObject) o).get("start"), (String) ((JSONObject) o).get("end"));
                String waight = (String) ((JSONObject) o).get("weight");
                try {
                    graph.setEdgeWeight(defaultWeightedEdge, Double.valueOf(waight));
                } catch (NumberFormatException e) {
                    System.console().writer().print(waight + " is incorrect");
                }
            }
        }
    }

    private static void createVertex(Graph<String, DefaultWeightedEdgeCustom> graph, JSONObject data) {
        for (Object o : (ArrayList) data.get("vertex")) {
            graph.addVertex((String) ((JSONObject) o).get("name"));
        }
    }

    private static Graph<String, DefaultWeightedEdgeCustom> reweightGraph(Graph<String, DefaultWeightedEdgeCustom> graph, Map<String, Double> distance) {
        Graph<String, DefaultWeightedEdgeCustom> newGraph = graph;
        for (String source : graph.vertexSet()) {
            for (String dest : graph.vertexSet()) {
                DefaultWeightedEdgeCustom defaultWeightedEdge = graph.getEdge(source, dest);
                if (defaultWeightedEdge != null) {
                    newGraph.setEdgeWeight(defaultWeightedEdge, newWeight(source, dest, graph, distance));
                }
            }
        }
        return newGraph;
    }

    private static double newWeight(String source, String dest, Graph<String, DefaultWeightedEdgeCustom> graph, Map<String, Double> distance) {
        return Double.valueOf(graph.getEdge(source, dest).getWeightCustom()) + distance.get(source) - distance.get(dest);
    }


    private static Boolean checkIfcanBeParesd(Object o) {
        return ((JSONObject) o).get("start") instanceof String && ((JSONObject) o).get("end") instanceof String && ((JSONObject) o).get("weight") instanceof String;

    }

    private static Graph<String, DefaultWeightedEdgeCustom> removeAdditionalVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        graph.removeVertex(BellmanFord.getAdditionalVertex());
        return graph;
    }

}












