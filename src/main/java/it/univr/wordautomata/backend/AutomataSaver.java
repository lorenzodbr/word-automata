package it.univr.wordautomata.backend;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import javafx.stage.FileChooser;

/**
 * The AutomataSaver class provides methods for saving and reading automata
 * objects to/from files,
 * as well as managing recent files and file dialogs.
 */
public class AutomataSaver {
    private static File tmpFolder = null;

    private AutomataSaver() {
    }

    /**
     * Saves the automata to a file.
     *
     * @param file the file to save the automata to
     */
    public static void save(File file, boolean debug) {
        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();

        try (
                FileOutputStream fileStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileStream)) {
            out.writeObject(graph);
            out.writeObject(Model.getInstance().getInitialState());

            if (!debug) {
                Model.getInstance().setSaved(true);
                recordRecentFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (!debug)
                Alerts.showErrorDialog(Components.getInstance().getScene(), "Error Saving Automata",
                        "An error occurred while saving the automata.",
                        false);
        }
    }

    /**
     * Reads a DigraphEdgeList from a file.
     *
     * @param file the file to read the DigraphEdgeList from
     * @return the DigraphEdgeList read from the file
     */
    @SuppressWarnings("unchecked")
    public static DigraphEdgeList<State, Transition> read(File file, boolean debug) {
        DigraphEdgeList<State, Transition> graph = null;

        try (FileInputStream fileStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileStream)) {
            graph = (DigraphEdgeList<State, Transition>) in.readObject();
            Model.getInstance().setInitialState((State) in.readObject());

            if (!debug) {
                Model.getInstance().setOpenedFile(file);
                recordRecentFile(file);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            if (!debug)
                Alerts.showErrorDialog(Components.getInstance().getScene(), "Error Reading Automata",
                        "An error occurred while reading the automata.",
                        false);
        }

        return graph;
    }

    /**
     * Returns the temporary folder used by the application.
     * If the folder doesn't exist, it creates a new one.
     * Also, if the recent files file doesn't exist, it creates a new one.
     *
     * @return the temporary folder used by the application
     * @throws IOException if an I/O error occurs while creating the folder or file
     */
    public static File getTmpFolder() throws IOException {
        if (tmpFolder == null) {
            tmpFolder = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
                    + "wordautomata");

            if (!tmpFolder.exists()) {
                Files.createDirectory(tmpFolder.toPath());
            }
        }

        File recentFile = new File(tmpFolder.getAbsolutePath() + System.getProperty("file.separator")
                + Constants.RECENT_FILES_FILENAME);
        if (!recentFile.exists()) {
            recentFile.createNewFile();
        }

        return tmpFolder;
    }

    /**
     * Retrieves a list of recent files.
     *
     * @return A list of recently accessed files.
     */
    @SuppressWarnings("unchecked")
    public static List<File> getRecentFiles() {
        List<File> recentFiles = new ArrayList<>();

        try (
                FileInputStream fileStream = new FileInputStream(getTmpFolder().getAbsolutePath()
                        + System.getProperty("file.separator") + Constants.RECENT_FILES_FILENAME);
                ObjectInputStream in = new ObjectInputStream(fileStream)) {
            recentFiles = (List<File>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (!(e instanceof EOFException))
                e.printStackTrace();
        }

        return recentFiles;
    }

    /**
     * Records the given file as a recent file in the application.
     * If the file already exists in the recent files list, it is removed and added
     * to the top.
     * If the number of recent files exceeds the maximum limit, the oldest file is
     * removed.
     *
     * @param file the file to be recorded as a recent file
     */
    public static synchronized void recordRecentFile(File file) {
        List<File> recentFiles = getRecentFiles();

        if (recentFiles.stream().anyMatch(f -> f.getAbsolutePath().equals(file.getAbsolutePath()))) {
            recentFiles.remove(file);
        }

        recentFiles.add(0, file);

        if (recentFiles.size() > Constants.MAX_RECENT_FILES) {
            recentFiles.remove(recentFiles.size() - 1);
        }

        try (
                FileOutputStream fileStream = new FileOutputStream(getTmpFolder().getAbsolutePath()
                        + System.getProperty("file.separator") + Constants.RECENT_FILES_FILENAME);
                ObjectOutputStream out = new ObjectOutputStream(fileStream)) {
            out.writeObject(recentFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the list of recent files.
     * This method deletes the file that stores the list of recent files.
     * If the file does not exist or cannot be deleted, an IOException is thrown.
     */
    public static void clearRecentFiles() {
        try {
            Files.delete(new File(getTmpFolder().getAbsolutePath() + System.getProperty("file.separator")
                    + Constants.RECENT_FILES_FILENAME).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the currently opened file.
     */
    public static void save() {
        save(Model.getInstance().getOpenedFile(), false);
    }

    /**
     * Reads a DigraphEdgeList from a file.
     *
     * @return the DigraphEdgeList read from the file
     */
    public static DigraphEdgeList<State, Transition> read() {
        return read(new File(Constants.DEFAULT_AUTOMATA_FILENAME + Constants.AUTOMATA_EXTENSION), false);
    }

    /**
     * Represents a file in the file system.
     */
    public static File showOpenDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + Constants.AUTOMATA_EXTENSION + " File");
        fileChooser.setInitialDirectory(Constants.INITIAL_DIRECTORY);
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Automata Files", "*" + Constants.AUTOMATA_EXTENSION));
        return fileChooser.showOpenDialog(Components.getInstance().getStage());
    }

    /**
     * Represents a file in the file system.
     */
    public static File showSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save " + Constants.AUTOMATA_EXTENSION + " File");
        fileChooser.setInitialDirectory(Constants.INITIAL_DIRECTORY);
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Automata Files", "*" + Constants.AUTOMATA_EXTENSION));
        fileChooser.setInitialFileName(Constants.DEFAULT_AUTOMATA_FILENAME + Constants.AUTOMATA_EXTENSION);
        return fileChooser.showSaveDialog(Components.getInstance().getStage());
    }
}
