import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.JohnsonShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Johnson {
    public static void main(String[] args) {
        Graph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject data = (JSONObject) jsonParser.parse(new FileReader("F:\\projekty\\graphs\\src\\main\\resources\\graphA.json"));

            for (Object o : (ArrayList) data.get("vertex")) {
                graph.addVertex((String) ((JSONObject) o).get("name"));
            }

            for (Object o : (ArrayList) data.get("edge")) {
                if (checkIfcanBeParesd(o)) {
                    DefaultWeightedEdge defaultWeightedEdge = graph.addEdge((String) ((JSONObject) o).get("start"), (String) ((JSONObject) o).get("end"));
                    String waight = (String) ((JSONObject) o).get("weight");
                    try {
                        graph.setEdgeWeight(defaultWeightedEdge, Double.valueOf(waight));
                    } catch (NumberFormatException e) {
                        System.console().writer().print(waight + " is incorrect");
                    }
                }
            }

          //  FrontEnd frontEnd = new FrontEnd(graph);

            JohnsonShortestPaths johnsonShortestPaths = new JohnsonShortestPaths(graph, String.class);
            ShortestPathAlgorithm.SingleSourcePaths<DefaultWeightedEdge, String> paths = johnsonShortestPaths.getPaths("B");
            paths.getGraph();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static Boolean checkIfcanBeParesd(Object o) {
        return ((JSONObject) o).get("start") instanceof String && ((JSONObject) o).get("end") instanceof String && ((JSONObject) o).get("weight") instanceof String;

    }
}
