import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DijkstraResult {
    private List<String> vertices = new ArrayList<>();
    private Map<String, Double> distance = new HashMap<>();
    private Map<String, String> predecessors = new HashMap<>();

    public Map<String, Double> getDistance() {
        return distance;
    }

    public Map<String, String> getPredecessors() {
        return predecessors;
    }

    public List<String> getVertices() {
        return vertices;
    }
}
