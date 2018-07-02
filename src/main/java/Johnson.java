import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.JohnsonShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Johnson {
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

    public static SingleSourcePaths execute(Graph<String, DefaultWeightedEdgeCustom> graph, String startVertex) {
        BellmanFord.execute(graph, startVertex);
        JohnsonShortestPaths johnsonShortestPaths = new JohnsonShortestPaths(graph, String.class);
        return johnsonShortestPaths.getPaths(startVertex);
    }

    private static Boolean checkIfcanBeParesd(Object o) {
        return ((JSONObject) o).get("start") instanceof String && ((JSONObject) o).get("end") instanceof String && ((JSONObject) o).get("weight") instanceof String;

    }
}
