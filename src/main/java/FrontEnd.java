import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FrontEnd extends JFrame {

    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private Graph jonhsonGraph;
    private mxGraph graphUI;
    private Object parent;
    private Map<String, Object> vertexs;
    private String selectedVertex;

    public FrontEnd() {
        super("Algorytm Jonhsona");
        File file = new File(".\\src\\main\\resources\\graphA.json");
        refrash(file);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    private void refrash(File file) {
        init();
        jonhsonGraph = Johnson.createGraph(file);
        graphUI.getModel().beginUpdate();
        try {
            for (Object vertex : jonhsonGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexs.put((String) vertex, graphUI.insertVertex(parent, null, vertex, 0, 0, 30, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }
            for (Object edge : jonhsonGraph.edgeSet()) {
                if (edge instanceof DefaultWeightedEdgeCustom) {
                    graphUI.insertEdge(parent, null, ((DefaultWeightedEdgeCustom) edge).getWeightCustom(), vertexs.get(((DefaultWeightedEdgeCustom) edge).getSourceCustom()),
                            vertexs.get(((DefaultWeightedEdgeCustom) edge).getTargetCustom()));
                }
            }


        } finally {
            graphUI.getModel().endUpdate();
        }

        final mxGraphComponent graphComponent = new mxGraphComponent(graphUI);
        mxOrganicLayout layout = new mxOrganicLayout(graphUI);
        layout.setFineTuning(true);
        layout.setEdgeLengthCostFactor(0.00001D);
        layout.execute(graphUI.getDefaultParent());
        getContentPane().add(graphComponent);
        graphComponent.zoomAndCenter();
        graphComponent.refresh();
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    System.out.println("cell=" + graphUI.getLabel(cell));
                    selectedVertex = graphUI.getLabel(cell);
                }
            }
        });

        launch();
    }

    private void init() {
        graphUI = new mxGraph();
        parent = graphUI.getDefaultParent();
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
        configureImport(importGraph);
        JMenuItem runJohnson = new JMenuItem("Run algorithm");
        configureAlgorithm(runJohnson);
        JMenu menu = new JMenu("Options");
        menu.add(importGraph);
        menu.add(runJohnson);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void configureAlgorithm(JMenuItem runJohnson) {
        Double[][] shortestPaths;
        runJohnson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedVertex != null && !selectedVertex.isEmpty()) {
                    Johnson.execute(jonhsonGraph, selectedVertex);
                    System.out.println("Johnson.execute(jonhsonGraph)");
                } else {
                    final JPanel woringPanel = new JPanel();
                    JOptionPane.showMessageDialog(woringPanel, "Nie wybrano wierzchołka", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
    }

    private void configureImport(JMenuItem importGraph) {
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