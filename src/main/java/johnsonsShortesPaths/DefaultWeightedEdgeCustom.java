package johnsonsShortesPaths;

import org.jgrapht.graph.DefaultWeightedEdge;

public class DefaultWeightedEdgeCustom extends DefaultWeightedEdge {

    public DefaultWeightedEdgeCustom() {
        super();
    }

    public Object getSourceCustom() {
        return getSource();
    }

    public Object getTargetCustom() {
        return getTarget();
    }

    public String getWeightCustom() {

        return String.valueOf(getWeight());
    }

}
