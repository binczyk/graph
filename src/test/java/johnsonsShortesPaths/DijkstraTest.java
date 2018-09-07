package johnsonsShortesPaths;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DijkstraTest {


    @Test
    public void positiveWeightTest() {
        GraphMock graphMock = new GraphMock(new PositiveEdgeWeightStrategy());
        Dijkstra dijkstra = new Dijkstra(graphMock.getGraph());
        DijkstraResult dijkstraResult = dijkstra.execute("0");

        assertGraphSize(graphMock, dijkstraResult.getDistance().size());
        assertGraphSize(graphMock, dijkstraResult.getPredecessors().size());
        assertGraphSize(graphMock, dijkstraResult.getVertices().size());

        assertEquals(Double.valueOf(1), dijkstraResult.getDistance().get("1"));
        assertEquals(Double.valueOf(3), dijkstraResult.getDistance().get("2"));
        assertEquals(Double.valueOf(4), dijkstraResult.getDistance().get("3"));
        assertEquals(Double.valueOf(9), dijkstraResult.getDistance().get("4"));
    }

    private void assertGraphSize(GraphMock graphMock, int mapSize) {
        assertEquals(graphMock.getGraph().vertexSet().size(), mapSize);

    }
}