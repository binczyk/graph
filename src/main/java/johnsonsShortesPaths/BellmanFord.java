package johnsonsShortesPaths;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;

public class BellmanFord {

    private static final String ADDITIONAL_VERTEX = "#";
    private Map<String, Double> distance = new HashMap<>();

    public BellmanFord() {
    }

    public Map<String, Double> execute(Graph<String, DefaultWeightedEdgeCustom> graph) throws NegativeCycleException {
        createNewGraph(graph);
        initDistanceAndPrevList(graph);
        relax(graph);
        checkCycle(graph);
        return distance;
    }

    private Graph<String, DefaultWeightedEdgeCustom> createNewGraph(Graph<String, DefaultWeightedEdgeCustom> graph) {
        DefaultWeightedEdgeCustom edge;
        graph.addVertex(ADDITIONAL_VERTEX);
        for (String vertex : graph.vertexSet()) {
            if (!vertex.equalsIgnoreCase(ADDITIONAL_VERTEX)) {
                edge = graph.getEdge(ADDITIONAL_VERTEX, vertex);
                if (edge == null) {
                    edge = graph.addEdge(ADDITIONAL_VERTEX, vertex);
                }
                graph.setEdgeWeight(edge, 0D);
            }
        }
        return graph;
    }

    private void initDistanceAndPrevList(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (String vertex : graph.vertexSet()) {
            distance.put(vertex, Double.MAX_VALUE);
        }
        distance.put(ADDITIONAL_VERTEX, 0D);
    }

    private void relax(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (int i = 0; i < graph.vertexSet().size(); i++) {
            for (DefaultWeightedEdgeCustom edge : graph.edgeSet()) {
                if (checkIfNewWaightIsless(edge)) {
                    distance.put(edge.getTargetCustom().toString(), distance.get(edge.getSourceCustom().toString()) + Double.valueOf(edge.getWeightCustom()));
                }
            }
        }
    }

    private void checkCycle(Graph<String, DefaultWeightedEdgeCustom> graph) throws NegativeCycleException {
        for (int i = 0; i < graph.vertexSet().size(); i++) {
            for (DefaultWeightedEdgeCustom edge : graph.edgeSet()) {
                if (checkIfNewWaightIsless(edge)) {
                    throw new NegativeCycleException();
                }
            }
        }
    }

    private boolean checkIfNewWaightIsless(DefaultWeightedEdgeCustom edge) {
        return distance.get(edge.getTargetCustom().toString()) > distance.get(edge.getSourceCustom().toString()) + Double.valueOf(edge.getWeightCustom());
    }

    public String getAdditionalVertex() {
        return ADDITIONAL_VERTEX;
    }
}

