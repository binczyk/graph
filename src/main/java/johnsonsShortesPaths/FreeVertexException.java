package johnsonsShortesPaths;

public class FreeVertexException extends Exception {

    public FreeVertexException() {
        super("Graf nie może posiadać wierzchołków wiszących!");
    }

}
