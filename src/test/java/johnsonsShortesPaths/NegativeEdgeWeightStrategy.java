package johnsonsShortesPaths;

import org.jgrapht.Graph;

public class NegativeEdgeWeightStrategy implements EdgeWeightStrategy {

    @Override
    public void createEdges(Graph<String, DefaultWeightedEdgeCustom> graph) {
        DefaultWeightedEdgeCustom edge;

        edge = graph.addEdge("0", "1");
        graph.setEdgeWeight(edge, -1D);

        edge = graph.addEdge("1", "2");
        graph.setEdgeWeight(edge, -2D);

        edge = graph.addEdge("1", "3");
        graph.setEdgeWeight(edge, -3);

        edge = graph.addEdge("2", "3");
        graph.setEdgeWeight(edge, -4);

        edge = graph.addEdge("2", "4");
        graph.setEdgeWeight(edge, -6);

        edge = graph.addEdge("3", "4");
        graph.setEdgeWeight(edge, -5);

        edge = graph.addEdge("4", "0");
        graph.setEdgeWeight(edge, -7);
    }
}
