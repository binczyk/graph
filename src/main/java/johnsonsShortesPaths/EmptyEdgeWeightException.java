package johnsonsShortesPaths;

public class EmptyEdgeWeightException extends Exception {

    public EmptyEdgeWeightException() {
        super("Wszystkie wierzchołki muszą mieć wartość");
    }
}
