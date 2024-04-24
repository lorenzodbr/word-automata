package it.univr.wordautomata.components;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Utils;
import javafx.scene.layout.StackPane;

/**
 * Panel in which Graph is displayed
 */
public class GraphPanel extends StackPane {

    private Graph<State, Transition> graph;
    private SmartGraphPanel<State, Transition> graphView;
    private boolean autoLayout = false;

    public GraphPanel() {
        if (Utils.loadAndSetController(Utils.GRAPH_PANEL_FXML_FILENAME, this)) {
        }
    }

    private Graph<State, Transition> initSampleGraph() {
        Digraph<State, Transition> g = new DigraphEdgeList<>();

        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        State q4 = new State("q4");

        g.insertVertex(q0);
        g.insertVertex(q1);
        g.insertVertex(q2);
        g.insertVertex(q3);
        g.insertVertex(q4);

        g.insertEdge(q0, q1, new Transition("aab"));
        g.insertEdge(q0, q2, new Transition("abbb"));
        g.insertEdge(q2, q1, new Transition("aba"));
        g.insertEdge(q1, q2, new Transition("b"));
        g.insertEdge(q2, q3, new Transition("b"));
        g.insertEdge(q3, q3, new Transition("abb"));
        g.insertEdge(q3, q3, new Transition("abb2"));

        g.insertEdge(q1, q3, new Transition("a"));
        g.insertEdge(q1, q3, new Transition("a2"));

        g.insertEdge(q3, q4, new Transition("b"));
        g.insertEdge(q4, q1, new Transition("a"));
        g.insertEdge(q4, q2, new Transition("b"));

        return g;
    }

    public void addNode() {
        if (graph == null) {
            graph = initSampleGraph();

            SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
            ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();
            graphView = new SmartGraphPanel<State, Transition>(graph, initialPlacement, automaticPlacementStrategy);

            getChildren().add(new ContentZoomScrollPane(graphView));
            graphView.init();
        } else {
            graph.insertVertex(new State("newState"));
            graphView.update();
        }
    }

    public void toggleAutoPositioning() {
        graphView.setAutomaticLayout((autoLayout = !autoLayout));
    }
}
