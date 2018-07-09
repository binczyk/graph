package johnsonsShortesPaths;

public class NegativeCycleException extends Exception {

    public NegativeCycleException() {
        super("Graph has negative cycle");
    }

}
