package it.univr.wordautomata.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AutomataSaver {
    private AutomataSaver() {
    }

    public static void save(File file) {
        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();

        try (
                FileOutputStream fileStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileStream)) {
            out.writeObject(graph);
            Model.getInstance().setSaved(true);
        } catch (Exception e) {
            e.printStackTrace();

            Alerts.showErrorDialog(Components.getInstance().getScene(), "Error Saving Automata", "An error occurred while saving the automata.",
                    false);
        }
    }

    @SuppressWarnings("unchecked")
    public static DigraphEdgeList<State, Transition> read(File file) {
        DigraphEdgeList<State, Transition> graph = null;

        try (
                FileInputStream fileStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileStream)) {
            graph = (DigraphEdgeList<State, Transition>) in.readObject();
            Model.getInstance().setOpenedFile(file);
        } catch (Exception e) {
            e.printStackTrace();

            Alerts.showErrorDialog(Components.getInstance().getScene(), "Error Reading Automata", "An error occurred while reading the automata.",
                    false);
        }

        return graph;
    }

    public static void save() {
        save(Model.getInstance().getOpenedFile());
    }

    public static DigraphEdgeList<State, Transition> read() {
        return read(new File(Constants.DEFAULT_AUTOMATA_FILENAME + Constants.AUTOMATA_EXTENSION));
    }

    public static File showOpenDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + Constants.AUTOMATA_EXTENSION + " File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Automata Files", "*" + Constants.AUTOMATA_EXTENSION));
        return fileChooser.showOpenDialog(stage);
    }

    public static File showSaveDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save " + Constants.AUTOMATA_EXTENSION + " File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Automata Files", "*" + Constants.AUTOMATA_EXTENSION));
        return fileChooser.showSaveDialog(stage);
    }
}
