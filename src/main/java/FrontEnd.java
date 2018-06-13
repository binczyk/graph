import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FrontEnd extends JFrame {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private Graph simpleGraph;
    private mxGraph graph;
    private Object parent;
    private Map<String, Object> vertexs;

    public FrontEnd() {
        super("Algorytm jonhsona");
        File file = new File(".\\src\\main\\resources\\graphA.json");
        refrash(file);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refrash(File file) {
        init();
        simpleGraph = Johnson.createGraph(file);
        graph.getModel().beginUpdate();
        try {
            for (Object vertex : simpleGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexs.put((String) vertex, graph.insertVertex(parent, null, vertex, 0, 0, 30, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }
            for (Object edge : simpleGraph.edgeSet()) {
                if (edge instanceof DefaultWeightedEdgeCustom) {
                    graph.insertEdge(parent, null, ((DefaultWeightedEdgeCustom) edge).getWeightCustom(), vertexs.get(((DefaultWeightedEdgeCustom) edge).getSourceCustom()),
                                     vertexs.get(((DefaultWeightedEdgeCustom) edge).getTargetCustom()));
                }
            }


        } finally {
            graph.getModel().endUpdate();
        }

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mxOrganicLayout layout = new mxOrganicLayout(graph);
        layout.setFineTuning(true);
        layout.setEdgeLengthCostFactor(0.00001D);
        layout.execute(graph.getDefaultParent());
        getContentPane().add(graphComponent);
        graphComponent.zoomAndCenter();
        graphComponent.refresh();
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

    private void init() {
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        vertexs = new HashMap<>();
    }

    private void launch() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        addMenu(this);
        setVisible(true);

    }


    private void addMenu(FrontEnd frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem importGraph = new JMenuItem("Import");
        configureAction(importGraph);
        JMenuItem runJohnson = new JMenuItem("Run algorithm");
        JMenu menu = new JMenu("Options");
        menu.add(importGraph);
        menu.add(runJohnson);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void configureAction(JMenuItem importGraph) {
        importGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser importFile = new JFileChooser();
                importFile.setDialogTitle("Wybierz plik");
                importFile.showOpenDialog(importGraph);
                File newGraph = importFile.getSelectedFile();
                refrash(newGraph);
            }
        });
    }
}