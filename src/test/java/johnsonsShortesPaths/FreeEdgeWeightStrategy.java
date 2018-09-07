package johnsonsShortesPaths;

import org.jgrapht.Graph;

public class FreeEdgeWeightStrategy implements EdgeWeightStrategy {

    @Override
    public void createEdges(Graph<String, DefaultWeightedEdgeCustom> graph) {
        DefaultWeightedEdgeCustom edge;

        edge = graph.addEdge("1", "2");
        graph.setEdgeWeight(edge, 2);

        edge = graph.addEdge("1", "3");
        graph.setEdgeWeight(edge, 3);

        edge = graph.addEdge("2", "3");
        graph.setEdgeWeight(edge, 4);

        edge = graph.addEdge("2", "4");
        graph.setEdgeWeight(edge, 6);

        edge = graph.addEdge("3", "4");
        graph.setEdgeWeight(edge, 5);

    }
}
