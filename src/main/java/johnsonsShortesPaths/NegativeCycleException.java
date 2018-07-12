package johnsonsShortesPaths;

public class NegativeCycleException extends Exception {

    public NegativeCycleException() {
        super("Graf posiada cykl o ujemnej wadze");
    }

}
