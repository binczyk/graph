package johnsonsShortesPaths;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JohnsonTest {

    @Test
    public void positiveWeightTest() throws NegativeCycleException, FreeVertexException {
        GraphMock graphMock = new GraphMock(new PositiveEdgeWeightStrategy());
        Johnson johnson = new Johnson(graphMock.getGraph());
        Map<String, DijkstraResult> jonhsonShortesPath = johnson.execute();

        assertEquals(Double.valueOf(1), jonhsonShortesPath.get("0").getDistance().get("1"));
        assertEquals(Double.valueOf(3), jonhsonShortesPath.get("0").getDistance().get("2"));
        assertEquals(Double.valueOf(4), jonhsonShortesPath.get("0").getDistance().get("3"));
        assertEquals(Double.valueOf(9), jonhsonShortesPath.get("0").getDistance().get("4"));

        assertEquals(Double.valueOf(15), jonhsonShortesPath.get("1").getDistance().get("0"));
        assertEquals(Double.valueOf(2), jonhsonShortesPath.get("1").getDistance().get("2"));
        assertEquals(Double.valueOf(3), jonhsonShortesPath.get("1").getDistance().get("3"));
        assertEquals(Double.valueOf(8), jonhsonShortesPath.get("1").getDistance().get("4"));

        assertEquals(Double.valueOf(13), jonhsonShortesPath.get("2").getDistance().get("0"));
        assertEquals(Double.valueOf(14), jonhsonShortesPath.get("2").getDistance().get("1"));
        assertEquals(Double.valueOf(4), jonhsonShortesPath.get("2").getDistance().get("3"));
        assertEquals(Double.valueOf(6), jonhsonShortesPath.get("2").getDistance().get("4"));

        assertEquals(Double.valueOf(12), jonhsonShortesPath.get("3").getDistance().get("0"));
        assertEquals(Double.valueOf(13), jonhsonShortesPath.get("3").getDistance().get("1"));
        assertEquals(Double.valueOf(15), jonhsonShortesPath.get("3").getDistance().get("2"));
        assertEquals(Double.valueOf(5), jonhsonShortesPath.get("3").getDistance().get("4"));

        assertEquals(Double.valueOf(7), jonhsonShortesPath.get("4").getDistance().get("0"));
        assertEquals(Double.valueOf(8), jonhsonShortesPath.get("4").getDistance().get("1"));
        assertEquals(Double.valueOf(10), jonhsonShortesPath.get("4").getDistance().get("2"));
        assertEquals(Double.valueOf(11), jonhsonShortesPath.get("4").getDistance().get("3"));
    }

    @Test
    public void mixedWeightTest() throws NegativeCycleException, FreeVertexException {
        GraphMock graphMock = new GraphMock(new MixedEdgeWeightStrategy());
        Johnson johnson = new Johnson(graphMock.getGraph());
        Map<String, DijkstraResult> jonhsonShortesPath = johnson.execute();

        assertEquals(Double.valueOf(-1), jonhsonShortesPath.get("0").getDistance().get("1"));
        assertEquals(Double.valueOf(1), jonhsonShortesPath.get("0").getDistance().get("2"));
        assertEquals(Double.valueOf(-4), jonhsonShortesPath.get("0").getDistance().get("3"));
        assertEquals(Double.valueOf(-5), jonhsonShortesPath.get("0").getDistance().get("4"));

        assertEquals(Double.valueOf(3), jonhsonShortesPath.get("1").getDistance().get("0"));
        assertEquals(Double.valueOf(2), jonhsonShortesPath.get("1").getDistance().get("2"));
        assertEquals(Double.valueOf(-3), jonhsonShortesPath.get("1").getDistance().get("3"));
        assertEquals(Double.valueOf(-4), jonhsonShortesPath.get("1").getDistance().get("4"));

        assertEquals(Double.valueOf(1), jonhsonShortesPath.get("2").getDistance().get("0"));
        assertEquals(Double.valueOf(0), jonhsonShortesPath.get("2").getDistance().get("1"));
        assertEquals(Double.valueOf(-3), jonhsonShortesPath.get("2").getDistance().get("3"));
        assertEquals(Double.valueOf(-6), jonhsonShortesPath.get("2").getDistance().get("4"));

        assertEquals(Double.valueOf(12), jonhsonShortesPath.get("3").getDistance().get("0"));
        assertEquals(Double.valueOf(11), jonhsonShortesPath.get("3").getDistance().get("1"));
        assertEquals(Double.valueOf(13), jonhsonShortesPath.get("3").getDistance().get("2"));
        assertEquals(Double.valueOf(5), jonhsonShortesPath.get("3").getDistance().get("4"));

        assertEquals(Double.valueOf(7), jonhsonShortesPath.get("4").getDistance().get("0"));
        assertEquals(Double.valueOf(6), jonhsonShortesPath.get("4").getDistance().get("1"));
        assertEquals(Double.valueOf(8), jonhsonShortesPath.get("4").getDistance().get("2"));
        assertEquals(Double.valueOf(3), jonhsonShortesPath.get("4").getDistance().get("3"));
    }

    @Test(expected = FreeVertexException.class)
    public void FreeVertexEceptionTest() throws NegativeCycleException, FreeVertexException {
        GraphMock graphMock = new GraphMock(new FreeEdgeWeightStrategy());
        Johnson johnson = new Johnson(graphMock.getGraph());
        johnson.execute();
    }
}