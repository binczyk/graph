import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

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
        Set<Object> vertexs = new HashSet<>();
        graph.getModel().beginUpdate();
        try {
            for (Object vertex : simpleGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexs.add(graph.insertVertex(parent, null, vertex, Math.round(Math.random() * WIDTH / 2), Math.round(Math.random() * HEIGHT / 2), 80, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }


           // graph.insertEdge(parent, null, "Edge", v1, v2);
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