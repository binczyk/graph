package johnsonsShortesPaths;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BellmanFordTest {

    BellmanFord bellmanFord = new BellmanFord();

    @Test
    public void testAdditionalVertex() {
        assertEquals("#", bellmanFord.getAdditionalVertex());
    }

    @Test
    public void positiveWeightTest() throws NegativeCycleException {
        GraphMock graphMock = new GraphMock(new PositiveEdgeWeightStrategy());
        Map<String, Double> positiveMap = bellmanFord.execute(graphMock.getGraph());

        assertGraphSize(graphMock, positiveMap);
        assertEquals(Double.valueOf(0), positiveMap.get("0"));
        assertEquals(Double.valueOf(0), positiveMap.get("1"));
        assertEquals(Double.valueOf(0), positiveMap.get("2"));
        assertEquals(Double.valueOf(0), positiveMap.get("3"));
        assertEquals(Double.valueOf(0), positiveMap.get("4"));
    }

    @Test(expected = NegativeCycleException.class)
    public void negativeWeightTest() throws NegativeCycleException {
        GraphMock graphMock = new GraphMock(new NegativeEdgeWeightStrategy());
        bellmanFord.execute(graphMock.getGraph());
    }

    @Test
    public void mixedWeightTest() throws NegativeCycleException {
        GraphMock graphMock = new GraphMock(new MixedEdgeWeightStrategy());
        Map<String, Double> mixedMap = bellmanFord.execute(graphMock.getGraph());

        assertGraphSize(graphMock, mixedMap);
        assertEquals(Double.valueOf(0), mixedMap.get("0"));
        assertEquals(Double.valueOf(-1), mixedMap.get("1"));
        assertEquals(Double.valueOf(0), mixedMap.get("2"));
        assertEquals(Double.valueOf(-4), mixedMap.get("3"));
        assertEquals(Double.valueOf(-6), mixedMap.get("4"));


    }

    private void assertGraphSize(GraphMock graphMock, Map<String, Double> positiveMap) {
        assertEquals(graphMock.getGraph().vertexSet().size(), positiveMap.size());
    }
}