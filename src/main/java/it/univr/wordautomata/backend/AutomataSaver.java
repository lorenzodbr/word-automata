package it.univr.wordautomata.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AutomataSaver {
    private AutomataSaver() {
    }

    public static void save(String filename) {
        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();

        try (
                FileOutputStream file = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(graph);
        } catch (Exception e) {
            e.printStackTrace();

            Alerts.showErrorDialog(null, "Error Saving Automata", "An error occurred while saving the automata.");
        }
    }

    @SuppressWarnings("unchecked")
    public static DigraphEdgeList<State, Transition> read(String filename) {
        DigraphEdgeList<State, Transition> graph = null;

        try (
                FileInputStream file = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(file)) {
            graph = (DigraphEdgeList<State, Transition>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();

            Alerts.showErrorDialog(null, "Error Reading Automata", "An error occurred while reading the automata.");
        }

        return graph;
    }

    private static final String DEFAULT_FILENAME = "tmp" + Constants.AUTOMATA_EXTENSION;

    public static void save() {
        save(DEFAULT_FILENAME);
    }

    public static DigraphEdgeList<State, Transition> read() {
        return read(DEFAULT_FILENAME);
    }

    public static File showOpenDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + Constants.AUTOMATA_EXTENSION + " File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Automata Files", "*" + Constants.AUTOMATA_EXTENSION));
        return fileChooser.showOpenDialog(stage);
    }
}
