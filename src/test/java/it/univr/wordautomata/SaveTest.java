package it.univr.wordautomata;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;

/**
 * SaveTest
 */
public class SaveTest {
    
    /**
     * Modifies automata and check if it has been saved correctly
     */
    @Test
    public void testSaveAndRead() {
        for (File file : Globals.testFiles) {
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
        System.out.println("Performing path test for file: " + file.getName());

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
}