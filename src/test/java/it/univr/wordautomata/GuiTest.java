package it.univr.wordautomata;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class GuiTest extends ApplicationTest {
    /**
     * An instance of the WordAutomata class.
     */
    private static final WordAutomata instance = new WordAutomata();

    @Override
    public void start(javafx.stage.Stage stage) {
        try {
            instance.start(stage);
        } catch (Exception e) {
        }
    }

    /**
     * Test case for the path GUI.
     * This method starts the platform, runs the instance, and performs path GUI
     * tests
     * for each test file and test path.
     */
    @Test
    public void testPathGUI() {
        Platform.runLater(() -> {
            for (int i = 0; i < Globals.testFiles.size(); i++)
                performPathGUITest(Globals.testFiles.get(i), Globals.testPaths.get(i));
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
