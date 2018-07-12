package johnsonsShortesPaths;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Johnson {

    private Dijkstra dijkstra;
    private BellmanFord bellmanFord = new BellmanFord();
    private Graph<String, DefaultWeightedEdgeCustom> graph;

    public Johnson(Graph<String, DefaultWeightedEdgeCustom> graph) {
        this.graph = graph;
    }

    public Johnson() {
    }

    public Map<String, DijkstraResult> execute() throws NegativeCycleException, FreeVertexException {
        checkIfThereAreFreeVertex();
        Map<String, Double> bFdistance = bellmanFord.execute(graph);
        reweightGraph(graph, bFdistance);
        removeAdditionalVertex(graph);
        Map<String, DijkstraResult> weightFunction = runDijkstraForAllVertex(graph);
        return calculateShortestPath(weightFunction, bFdistance);
    }

    private void checkIfThereAreFreeVertex() throws FreeVertexException {
        for (String vertex : graph.vertexSet()) {
            if (graph.degreeOf(vertex) == 0) {
                throw new FreeVertexException();
            }
        }
    }

    private Map<String, DijkstraResult> calculateShortestPath(Map<String, DijkstraResult> waightFunction, Map<String, Double> bFdistance) {
        Map<String, DijkstraResult> allPairsPath = new HashMap<>();
        for (String vertex : waightFunction.keySet()) {
            allPairsPath.put(vertex, calculateWeight(vertex, waightFunction, bFdistance));
        }
        return allPairsPath;

    }

    private DijkstraResult calculateWeight(String currentVertex, Map<String, DijkstraResult> waightFunction, Map<String, Double> bFdistance) {
        DijkstraResult dijkstraResult = new DijkstraResult(waightFunction.get(currentVertex).getVertices(), waightFunction.get(currentVertex).getPredecessors());
        for (String destVertex : waightFunction.get(currentVertex).getVertices()) {
            dijkstraResult.getDistance().put(destVertex, calculate(currentVertex, waightFunction, bFdistance, destVertex));
        }
        return dijkstraResult;
    }

    private double calculate(String currentVertex, Map<String, DijkstraResult> waightFunction, Map<String, Double> bFdistance, String destVertex) {
        Double weightSource = waightFunction.get(currentVertex).getDistance().get(destVertex);
        Double bfSource = bFdistance.get(currentVertex);
        Double bfDest = bFdistance.get(destVertex);
        return weightSource - bfSource + bfDest;
    }

    private Map<String, DijkstraResult> runDijkstraForAllVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        dijkstra = new Dijkstra(graph);
        Map<String, DijkstraResult> waightFunction = new HashMap<>();
        for (String vertex : graph.vertexSet()) {
            waightFunction.put(vertex, dijkstra.execte(vertex));
        }
        return waightFunction;
    }

    public Graph createGraph(BufferedReader jsnoWithGraph) throws ParseException {
        Graph<String, DefaultWeightedEdgeCustom> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdgeCustom.class);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject data = (JSONObject) jsonParser.parse(jsnoWithGraph);
            createVertex(graph, data);
            createEdges(graph, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private void createEdges(Graph<String, DefaultWeightedEdgeCustom> graph, JSONObject data) {
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

    private void createVertex(Graph<String, DefaultWeightedEdgeCustom> graph, JSONObject data) {
        for (Object o : (ArrayList) data.get("vertex")) {
            graph.addVertex((String) ((JSONObject) o).get("name"));
        }
    }

    private Graph<String, DefaultWeightedEdgeCustom> reweightGraph(Graph<String, DefaultWeightedEdgeCustom> graph, Map<String, Double> distance) {
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

    private double newWeight(String source, String dest, Graph<String, DefaultWeightedEdgeCustom> graph, Map<String, Double> distance) {
        return Double.valueOf(graph.getEdge(source, dest).getWeightCustom()) + distance.get(source) - distance.get(dest);
    }


    private Boolean checkIfcanBeParesd(Object o) {
        return ((JSONObject) o).get("start") instanceof String && ((JSONObject) o).get("end") instanceof String && ((JSONObject) o).get("weight") instanceof String;

    }

    private void removeAdditionalVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        graph.removeVertex(bellmanFord.getAdditionalVertex());
    }

    public void setGraph(Graph<String, DefaultWeightedEdgeCustom> graph) {
        this.graph = graph;
    }
}












