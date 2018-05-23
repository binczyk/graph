import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class FrontEnd extends JFrame {

    private int WIDTH = 800;
    private int HEIGHT = 600;

    public FrontEnd() {
        super("Algorytm jonhsona");
    }

    public FrontEnd(Graph simpleGraph) {
        new FrontEnd();
        final mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        Map<String, Object> vertexs = new HashMap<>();
        graph.getModel().beginUpdate();
        try {
            for (Object vertex : simpleGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexs.put((String) vertex, graph.insertVertex(parent, null, vertex, Math.round(Math.random() * WIDTH / 2), Math.round(Math.random() * HEIGHT / 2), 30, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }
            for (Object edge : simpleGraph.edgeSet()) {
                if (edge instanceof DefaultWeightedEdgeCustom) {
                    graph.insertEdge(parent, null, ((DefaultWeightedEdgeCustom) edge).getWeightCustom(),
                            vertexs.get(((DefaultWeightedEdgeCustom) edge).getSourceCustom()), vertexs.get(((DefaultWeightedEdgeCustom) edge).getTargetCustom()));
                }
            }


        } finally {
            graph.getModel().endUpdate();
        }

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());

                if (cell != null) {
                    System.out.println("cell=" + graph.getLabel(cell));
                }
            }
        });

        launch();
    }

    private void launch() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    public static void main(String[] args) {
        FrontEnd frame = new FrontEnd();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}