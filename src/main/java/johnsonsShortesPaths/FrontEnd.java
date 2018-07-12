package johnsonsShortesPaths;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;
import org.json.simple.parser.ParseException;

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
import java.util.Objects;

public class FrontEnd extends JFrame {

    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private Graph johnsonGraph;
    private mxGraph graphUI;
    private Object parentObject;
    private Map<String, Object> vertexes;
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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLocationRelativeTo(null);
    }

    private void refrash(File file) {
        Johnson johnson = new Johnson();
        johnsonGraph = tryToCreateGraph(file, johnson);
        if (Objects.nonNull(johnsonGraph)) {
            johnson.setGraph(johnsonGraph);
            addVertexesAndEdges();

            mxGraphComponent graphComponent = new mxGraphComponent(graphUI);
            mxOrganicLayout layout = new mxOrganicLayout(graphUI);
            layout.setFineTuning(true);
            layout.setEdgeLengthCostFactor(0.00001D);
            layout.execute(graphUI.getDefaultParent());
            getContentPane().removeAll();
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
    }

    private Graph tryToCreateGraph(File file, Johnson johnson) {
        try {
            return johnson.createGraph(file);
        } catch (ParseException e) {
            JPanel errJPanel = new JPanel();
            JOptionPane.showMessageDialog(errJPanel, "Błąd pliku na pozycji: " + e.getPosition(), "błąd", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void addVertexesAndEdges() {
        vertexes = new HashMap<>();
        graphUI = new mxGraph() {
            @Override
            public boolean isCellEditable(Object cell) {
                if (cell != null) {
                    if (cell instanceof mxCell) {
                        mxCell myCell = (mxCell) cell;
                        if (myCell.isVertex()) {
                            return false;
                        }
                    }
                }
                return super.isCellSelectable(cell);
            }
        };
        graphUI.setAllowDanglingEdges(false);
        graphUI.getModel().beginUpdate();
        parentObject = graphUI.getDefaultParent();
        try {
            for (Object vertex : johnsonGraph.vertexSet()) {
                if (vertex instanceof String) {
                    vertexes.put((String) vertex, graphUI.insertVertex(parentObject, null, vertex, 0, 0, 30, 30));
                } else {
                    System.console().writer().print(vertex + " is not a String");
                }
            }
            for (Object edge : johnsonGraph.edgeSet()) {
                if (edge instanceof DefaultWeightedEdgeCustom) {
                    graphUI.insertEdge(parentObject, null, ((DefaultWeightedEdgeCustom) edge).getWeightCustom(), vertexes.get(((DefaultWeightedEdgeCustom) edge).getSourceCustom()),
                                       vertexes.get(((DefaultWeightedEdgeCustom) edge).getTargetCustom()));
                }
            }
        } finally {
            graphUI.getModel().endUpdate();
        }
    }

    private void launch() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        Johnson johnson = tryToUpdateGraph();
        if (Objects.nonNull(johnson)) {
            Map<String, DijkstraResult> result = getAllPaths(johnson);
            if (result != null) {
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
        }
    }

    private Johnson tryToUpdateGraph() {
        try {
            return updateGraph();
        } catch (EmptyEdgeWeightException e) {
            printAlert(e);
        }
        return null;
    }

    private Johnson updateGraph() throws EmptyEdgeWeightException {
        DefaultWeightedEdgeCustom defaultWeightedEdge;
        for (Object o : graphUI.getAllEdges(graphUI.getChildVertices(graphUI.getDefaultParent()))) {
            if (o instanceof mxCell && ((mxCell) o).isEdge() && ((mxCell) o).getTarget() != null) {
                mxCell cell = (mxCell) o;
                defaultWeightedEdge = (DefaultWeightedEdgeCustom) johnsonGraph.getEdge(cell.getSource().getValue(), cell.getTarget().getValue());
                if (Objects.isNull(defaultWeightedEdge)) {
                    defaultWeightedEdge = (DefaultWeightedEdgeCustom) johnsonGraph.addEdge(cell.getSource().getValue(), cell.getTarget().getValue());

                }
                String weight = (String) cell.getValue();
                if (!weight.isEmpty()) {
                    tryToSetNewWeight(defaultWeightedEdge, weight);
                } else {
                    throw new EmptyEdgeWeightException();
                }
            }
        }
        return new Johnson(johnsonGraph);
    }

    private void tryToSetNewWeight(DefaultWeightedEdgeCustom defaultWeightedEdge, String weight) {
        try {
            johnsonGraph.setEdgeWeight(defaultWeightedEdge, Double.valueOf(weight));
        } catch (NumberFormatException e) {

            printAlert(new NumberFormatException(e.getMessage() + ". Graf obliczony ze starą wartością."));
        }
    }

    private Map<String, DijkstraResult> getAllPaths(Johnson johnson) {
        try {
            johnson.setGraph(johnsonGraph);
            return johnson.execute();
        } catch (NegativeCycleException e) {
            printAlert(e);
        }
        return null;
    }

    private void printAlert(Exception e) {
        JPanel errJPanel = new JPanel();
        JOptionPane.showMessageDialog(errJPanel, e.getMessage(), "błąd", JOptionPane.ERROR_MESSAGE);
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