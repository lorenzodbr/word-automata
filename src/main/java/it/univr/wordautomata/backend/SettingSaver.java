package it.univr.wordautomata.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.brunomnsilva.smartgraph.graph.Graph;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;

public class SettingSaver {
    private SettingSaver() {}

    public static void save(String filename) {
        Graph<State, Transition> g = Model.getInstance().getGraph();

        try (
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file)
        ) {
            out.writeObject(g);
        } catch (IOException e) {
        }
    }

    public static Graph<State, Transition> read(String filename) {
        Graph<State, Transition> g = null;
        try (
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file)
        ) {
            g = (Graph<State,Transition>)in.readObject();
        } catch (IOException e2) {
        } catch (ClassNotFoundException e2) {
        }
        return g;
    }
}
