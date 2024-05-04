package it.univr.wordautomata.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;

public class AutomataSaver {
    private AutomataSaver() {}

    public static void save(String filename) {
        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();

        try (
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            out.writeObject(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static DigraphEdgeList<State, Transition> read(String filename) {
        DigraphEdgeList<State, Transition> graph = null;

        try (
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file)
        ) {
            graph = (DigraphEdgeList<State, Transition>)in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return graph;
    }

    private static final String DEFAULT_FILENAME = "tmp.automata";

    public static void save() {
        save(DEFAULT_FILENAME);
    }

    public static DigraphEdgeList<State, Transition> read() {
        return read(DEFAULT_FILENAME);
    }
}
