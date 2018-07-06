package johnsonsShortesPaths;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FrontEnd extends JFrame {

    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private Graph jonhsonGraph;
    private mxGraph graphUI;
    private Object parentObject;
    private Map<String, Object> vertexs;
    private String selectedVertex;
    private JTable distanceTableView;
    private JTable predecesorsTableView;
    private static final String RESULT = "RESULT";
    private static final String PREDECESSOR = "PREDECESSOR";


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
        Johnson johnson = new Johnson();
        jonhsonGraph = johnson.createGraph(file);
        graphUI.getModel().beginUpdate();
        try {
            for (Object vertex : jonhsonGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexs.put((String) vertex, graphUI.insertVertex(parentObject, null, vertex, 0, 0, 30, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }
            for (Object edge : jonhsonGraph.edgeSet()) {
                if (edge instanceof DefaultWeightedEdgeCustom) {
                    graphUI.insertEdge(parentObject, null, ((DefaultWeightedEdgeCustom) edge).getWeightCustom(), vertexs.get(((DefaultWeightedEdgeCustom) edge).getSourceCustom()),
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
            @Override
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
        parentObject = graphUI.getDefaultParent();
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
        JMenuItem runJohnson = new JMenuItem("Wykonaj algorytm");
        configureAlgorithm(runJohnson);
        JMenu menu = new JMenu("Opcje");
        menu.add(importGraph);
        menu.add(runJohnson);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void configureAlgorithm(JMenuItem runJohnson) {
        runJohnson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTableWithResult();
            }
        });
    }

    private void printTableWithResult() {
        Johnson johnson = new Johnson(jonhsonGraph);
        Map<String, DijkstraResult> result = johnson.execute();
        JFrame resutFrame = new JFrame();

        String distanceColumn[] = addColumn(result);
        String distanceData[][] = addResult(result, distanceColumn, RESULT);

        configureFrame(resutFrame);
        buildWindow(distanceColumn, distanceData, resutFrame, distanceTableView, BorderLayout.NORTH);

        String predecessorsColumn[] = addColumn(result);
        String predecessorsData[][] = addResult(result, distanceColumn, PREDECESSOR);

        buildWindow(predecessorsColumn, predecessorsData, resutFrame, predecesorsTableView, BorderLayout.CENTER);
        System.out.println("Johnson.execute(jonhsonGraph)");
    }

    private void configureFrame(JFrame resultFrame) {
        resultFrame.setTitle("Wynik");
        resultFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultFrame.pack();
        resultFrame.setSize(WIDTH, HEIGHT);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    private void buildWindow(String[] column, String[][] data, JFrame resultFrame, JTable tableView, String position) {
        tableView = new JTable(data, column);
        tableView.setDefaultEditor(Object.class, null);
        tableView.setPreferredScrollableViewportSize(tableView.getPreferredSize());
        tableView.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(tableView);
        resultFrame.add(sp, position);

    }

    private String[] addColumn(Map<String, DijkstraResult> result) {
        String[] columns = new String[result.size() + 1];
        columns[0] = "Wierzchołek";
        int i = 1;
        for (String vertex : result.keySet()) {
            columns[i] = vertex;
            i++;
        }

        return columns;
    }

    private String[][] addResult(Map<String, DijkstraResult> result, String[] column, String type) {
        int columnNo = 0;
        int row = 0;
        String[][] data = new String[result.size()][result.size() + 2];
        Iterator iterator = result.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry map = (Map.Entry) iterator.next();
            data[row][columnNo] = (String) map.getKey();
            for (String vertex : column) {
                String value = getValue(result, map, vertex, type);
                if (!value.equalsIgnoreCase("null")) {
                    columnNo++;
                    data[row][columnNo] = value.equalsIgnoreCase("$$$") ? "-" : value;
                }
            }
            row++;
            columnNo = 0;
        }
        return data;
    }

    private String getValue(Map<String, DijkstraResult> result, Entry map, String vertex, String type) {
        if (type.equalsIgnoreCase(RESULT)) {
            return String.valueOf(result.get(map.getKey()).getDistance().get(vertex));
        } else if (type.equalsIgnoreCase(PREDECESSOR)) {
            return String.valueOf(result.get(map.getKey()).getPredecessors().get(vertex));
        }
        return "";
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