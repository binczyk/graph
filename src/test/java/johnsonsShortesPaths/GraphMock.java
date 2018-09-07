package johnsonsShortesPaths;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class GraphMock {
    Graph<String, DefaultWeightedEdgeCustom> graph;

    public Graph<String, DefaultWeightedEdgeCustom> getGraph() {
        return graph;
    }

    public GraphMock(EdgeWeightStrategy edgeWeightStrategy) {
        this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdgeCustom.class);
        addFiveVertex(this.graph);
        edgeWeightStrategy.createEdges(this.graph);
    }

    private void addFiveVertex(Graph<String, DefaultWeightedEdgeCustom> graph) {
        for (int i = 0; i < 5; i++) {
            graph.addVertex(String.valueOf(i));
        }
    }

}
