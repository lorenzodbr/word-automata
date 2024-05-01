package it.univr.wordautomata.model;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Utils.Theme;
import static it.univr.wordautomata.utils.Utils.Theme.DARK;
import static it.univr.wordautomata.utils.Utils.Theme.LIGHT;
import javafx.stage.Stage;

/**
 *
 */
public class Model {

    private static Model instance;

    private Theme theme;
    private Graph<State, Transition> graph;
    private State initialState;

    private Model() {
        theme = Theme.DEFAULT;
        graph = new DigraphEdgeList<>();
        initialState = null;
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Theme cycleTheme() {
        Theme next = theme.next();
        instance.setTheme(next);
        return next;
    }
    
    public Graph getGraph(){
        return graph;
    }
    
    public void setInitialState(State s){
        this.initialState = s;
    }
    
    public State getInitialState(){
        return initialState;
    }
}
