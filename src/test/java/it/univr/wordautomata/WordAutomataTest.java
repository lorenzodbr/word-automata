package it.univr.wordautomata;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;

import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.backend.PathFinder;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * Test class for {@link WordAutomata}.
 */
public class WordAutomataTest {

    /**
     * An instance of the WordAutomata class.
     */
    private static final WordAutomata instance = new WordAutomata();

    /**
     * A list of test files.
     */
    private static final List<File> testFiles = List.of(
            Methods.getResource(WordAutomataTest.class, "tests", "deterministic.automata"),
            Methods.getResource(WordAutomataTest.class, "tests", "rejected.automata"),
            Methods.getResource(WordAutomataTest.class, "tests", "stats.automata"));

    /**
     * A list of test words.
     */
    private static final List<String> testWords = List.of(
            "xxxy",
            "xy",
            "yxy");

    /**
     * A list of test results.
     */
    private static final List<Boolean> testResults = List.of(
            true,
            true,
            false);

    /**
     * A list of paths to test.
     */
    private static final List<List<String>> testPaths = List.of(
            List.of("xxx", "y"),
            List.of("x", "y"),
            List.of("yxy"));

    /**
     * Test method for performing path tests.
     * Iterates through the test files and performs path tests for each file.
     */
    @Test
    public void testPath() {
        for (int i = 0; i < testFiles.size(); i++)
            performPathTest(testFiles.get(i), testWords.get(i), testResults.get(i));
    }

    /**
     * Test method for performing path tests.
     * 
     * @param file  the file to test
     * @param word  the word to test
     * @param found the expected result
     */
    private void performPathTest(File file, String word, boolean found) {
        DigraphEdgeList<State, Transition> graph = AutomataSaver.read(file, true);
        Assertions.assertNotNull(graph);
        Model.getInstance().updateGraph(graph);

        List<Edge<Transition, State>> path = PathFinder.getPath(word, graph);

        if (found) {
            Assertions.assertNotNull(path);
            Assertions.assertTrue(PathFinder.consumedAllWordProperty().get());
            Assertions.assertTrue(PathFinder.endedOnFinalStateProperty().get());
        } else {
            Assertions.assertNull(path);
        }
    }

    /*
     * Modifies automata and check if it has been saved correctly
     */
    @Test
    public void testSaveAndRead() {
        for (File file : testFiles) {
            performSaveAndReadTest(file);
        }
    }

    /**
     * Performs the save and read test for a given file.
     * Reads a DigraphEdgeList from the file, updates the graph in the Model,
     * modifies the final state of each vertex, saves the modified graph to the
     * file,
     * reads the modified graph from the file, and asserts that the original and
     * modified graphs are equal.
     *
     * @param file the file to perform the save and read test on
     */
    private void performSaveAndReadTest(File file) {
        DigraphEdgeList<State, Transition> graph = AutomataSaver.read(file, true);
        Assertions.assertNotNull(graph);
        Model.getInstance().updateGraph(graph);

        graph.vertices().forEach(v -> {
            v.element().setFinal(!v.element().isFinal());
        });

        AutomataSaver.save(file, true);

        DigraphEdgeList<State, Transition> newGraph = AutomataSaver.read(file, true);
        Assertions.assertNotNull(newGraph);
        Assertions.assertEquals(graph, newGraph);

        Model.getInstance().updateGraph(graph);

        graph.vertices().forEach(v -> {
            v.element().setFinal(!v.element().isFinal());
        });

        AutomataSaver.save(file, true);
    }

    /**
     * Test case for the path GUI.
     * This method starts the platform, runs the instance, and performs path GUI
     * tests
     * for each test file and test path.
     */
    @Test
    public void testPathGUI() {
        Platform.startup(() -> {
            instance.run();

            for (int i = 0; i < testFiles.size(); i++)
                performPathGUITest(testFiles.get(i), testPaths.get(i));
        });
    }

    /**
     * Check, when the path is found, if the GUI is updated correctly;
     * otherwise, check if the GUI is updated correctly when the path is not found
     * 
     * @param file     the file to test
     * @param expected the expected result
     */
    private void performPathGUITest(File file, List<String> expected) {
        DigraphEdgeList<State, Transition> graph = AutomataSaver.read(file, true);
        Assertions.assertNotNull(graph);
        Model.getInstance().updateGraph(graph);

        var children = instance.getComponents().getBottomBar().getTransitionsPanelHBox().getChildren();

        Assertions.assertNotNull(children);
        Assertions.assertEquals(children.size(), expected.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i), ((Button) children.get(i)).getText());
        }
    }
}
