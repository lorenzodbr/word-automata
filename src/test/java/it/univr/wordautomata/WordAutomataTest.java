package it.univr.wordautomata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class WordAutomataTest {
    private static final WordAutomata instance = new WordAutomata();
    private static final List<File> testFiles = List.of(
            Methods.getResource(WordAutomataTest.class, "tests", "deterministic.automata"),
            Methods.getResource(WordAutomataTest.class, "tests", "rejected.automata"),
            Methods.getResource(WordAutomataTest.class, "tests", "stats.automata"));
    private static final String[] testWords = {
            "xxxy",
            "xy",
            "yxy"
    };
    private static final String[][] testPaths = {
            { "xxx", "y" },
            { "x", "y" },
            { "yxy" }
    };

    @Test
    public void testPath() {
        for (File file : testFiles) {
            performPathTest(file);
        }
    }

    /*
     * Check if the path is (not) found correctly
     */
    private void performPathTest(File file) {
        DigraphEdgeList<State, Transition> graph = AutomataSaver.read(file, true);
        assertNotNull(graph);
        Model.getInstance().updateGraph(graph);

        for (String word : testWords) {
            List<Edge<Transition, State>> path = PathFinder.getPath(word, graph);
            if (path != null) {
                assertTrue(PathFinder.consumedAllWordProperty().get());
                assertTrue(PathFinder.endedOnFinalStateProperty().get());
            }
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

    private void performSaveAndReadTest(File file) {
        DigraphEdgeList<State, Transition> graph = AutomataSaver.read(file, true);
        assertNotNull(graph);
        Model.getInstance().updateGraph(graph);

        graph.vertices().forEach(v -> {
            v.element().setFinal(!v.element().isFinal());
        });

        AutomataSaver.save(file, true);

        DigraphEdgeList<State, Transition> newGraph = AutomataSaver.read(file, true);
        assertNotNull(newGraph);
        assertEquals(graph, newGraph);
    }

    @Test
    public void testPathGUI() {
        for (File file : testFiles) {
            performPathGUITest(file);
        }
    }

    /*
     * Check, when the path is found, if the GUI is updated correctly;
     * otherwise, check if the GUI is updated correctly when the path is not found
     */
    private void performPathGUITest(File file) {
        // TODO
        assertTrue(true);
    }
}
