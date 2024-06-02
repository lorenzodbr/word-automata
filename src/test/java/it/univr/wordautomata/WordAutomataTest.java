package it.univr.wordautomata;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

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
        // TODO
        assertTrue(true);
    }

    @Test
    public void testSaveAndRead() {
        for (File file : testFiles) {
            performSaveAndReadTest(file);
        }
    }

    /*
     * Check if saving the automata and then reading it back results in the same
     */
    private void performSaveAndReadTest(File file) {
        // TODO
        assertTrue(true);
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
