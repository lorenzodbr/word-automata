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

/**
 * Test class for {@link WordAutomata}.
 */
public class PathTest {

    /**
     * Test method for performing path tests.
     * Iterates through the test files and performs path tests for each file.
     */
    @Test
    public void testPath() {
        for (int i = 0; i < Globals.testFiles.size(); i++)
            performPathTest(Globals.testFiles.get(i), Globals.testWords.get(i), Globals.testResults.get(i));
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
}
