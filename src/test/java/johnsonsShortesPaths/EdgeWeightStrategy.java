package johnsonsShortesPaths;

import johnsonsShortesPaths.DefaultWeightedEdgeCustom;
import org.jgrapht.Graph;

public interface EdgeWeightStrategy {
    void createEdges(Graph<String, DefaultWeightedEdgeCustom> graph);
}
