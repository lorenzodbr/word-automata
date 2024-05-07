package it.univr.wordautomata.controller;

import it.univr.wordautomata.WordAutomata;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.Theme;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * The MainPanel class represents the outermost container of the application's
 * UI.
 * It contains the {@link GraphPanel} and {@link BottomBar} components.
 */
public class MainPanel extends BorderPane {

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem autoPositioningMenuItem;

    @FXML
    private MenuItem addTransitionMenuItem;

    @FXML
    private MenuItem addStateMenuItem;

    @FXML
    private MenuItem setInitialStateMenuItem;

    @FXML
    private MenuItem selectStateMenuItem;

    @FXML
    private MenuItem selectTransitionMenuItem;

    @FXML
    private MenuItem clearGraphMenuItem;

    @FXML
    private MenuItem darkThemeMenuItem;

    @FXML
    private MenuItem saveAutomataMenuItem;

    @FXML
    private MenuItem openAutomataMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem saveAsAutomataMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem legendMenuItem;

    private Model model;
    private WordAutomata parent;
    private Components controllers;

    /**
     * Constructs a new MainPanel object.
     *
     * @param parent the parent WordAutomata object
     */
    public MainPanel(WordAutomata parent) {
        Methods.loadAndSetController(Constants.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        this.model = Model.getInstance();
        this.controllers = Components.getInstance();

        addGraphPanel();
        addBottomBar();

        styleMenuItems();
        initBindings();
    }

    private void addBottomBar() {
        controllers.setBottomBar(new BottomBar());
        setBottom(controllers.getBottomBar());
    }

    private void addGraphPanel() {
        controllers.setGraphPanel(new GraphPanel());
        setCenter(controllers.getGraphPanel());
        Platform.runLater(() -> controllers.getGraphPanel().requestFocus());
    }

    @FXML
    private void addState() {
        controllers.getGraphPanel().addVertex();
    }

    @FXML
    private void addTransition() {
        controllers.getGraphPanel().addEdge();
    }

    @FXML
    private void clearGraph() {
        if (Alerts.showConfirmationDialog(getScene(), "Clear graph", "Do you really want to clear the graph?")) {
            controllers.getBottomBar().clear();
            controllers.getGraphPanel().clear();
        }
    }

    @FXML
    private void setInitialState() {
        controllers.getGraphPanel().chooseInitialState();
    }

    @FXML
    private void selectState() {
        controllers.getGraphPanel().selectState();
    }

    @FXML
    private void selectTransition() {
        controllers.getGraphPanel().selectTransition();
    }

    @FXML
    private void toggleAutoPositioning() {
        model.toggleAutoPositioning();
    }

    @FXML
    private void openAutomata() {
        // open file dialog
        File file = AutomataSaver.showOpenDialog((Stage) getScene().getWindow());

        if (file != null) {
            if (file.exists() && file.isFile() && file.getPath().endsWith(Constants.AUTOMATA_EXTENSION)) {
                loadAutomata(file);
            }
        }

    }

    public void loadAutomata(File file) {
        if (!model.isSaved() && !Alerts.showConfirmationDialog(getScene(), "Open automata",
                "Do you really want to open a new automata? You will lose any unsaved changes.")) {
            return;
        }

        model.updateGraph(AutomataSaver.read(file));
    }

    @FXML
    public void saveAutomata() {
        Methods.save();
    }

    @FXML
    public void saveAsAutomata() {
        Methods.saveAs();
    }

    private void initBindings() {
        BooleanBinding noVertexBinding = model.atLeastOneVertexProperty().not();
        BooleanBinding noEdgeBinding = model.atLeastOneEdgeProperty().not();

        clearGraphMenuItem.disableProperty().bind(noVertexBinding);
        addTransitionMenuItem.disableProperty().bind(noVertexBinding);
        setInitialStateMenuItem.disableProperty().bind(noVertexBinding);
        autoPositioningMenuItem.disableProperty().bind(noVertexBinding);
        selectStateMenuItem.disableProperty().bind(noVertexBinding);
        selectTransitionMenuItem.disableProperty().bind(noEdgeBinding);
        closeMenuItem.disableProperty().bind(model.openedFileProperty().isNull());
    }

    private void styleMenuItems() {
        autoPositioningMenuItem.graphicProperty().bind(Bindings.when(model.autoPositionProperty())
                .then(new FontIcon(BoxiconsRegular.CHECK))
                .otherwise(new FontIcon(BoxiconsSolid.MAGIC_WAND)));
        darkThemeMenuItem.graphicProperty().bind(Bindings.when(model.themeProperty().isEqualTo(Theme.DARK))
                .then(new FontIcon(BoxiconsRegular.CHECK))
                .otherwise(new FontIcon(BoxiconsRegular.MOON)));
        saveAutomataMenuItem.disableProperty().bind(model.savedProperty());

        openAutomataMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+O"));
        saveAutomataMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+S"));
        saveAsAutomataMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+SHIFT+S"));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+Q"));
        autoPositioningMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+A"));
        addStateMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+N"));
        addTransitionMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+SHIFT+N"));
        clearGraphMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+SHIFT+C"));
        setInitialStateMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+I"));
        legendMenuItem.setAccelerator(KeyCombination.keyCombination("CTRL+L"));
    }

    @FXML
    private void toggleDarkTheme() {
        parent.toggleTheme();
    }

    public double getMenuBarHeight() {
        return menuBar.getHeight();
    }

    @FXML
    private void showInfo() {
        Alerts.showInformationDialog(getScene(), "About", Constants.TITLE + " v" + Constants.VERSION,
                "Created by Lorenzo Di Berardino, Mateo Gjika and Filippo Milani.");
    }

    @FXML
    private void close() {
        if (!model.isSaved()) {
            if (Alerts.showConfirmationDialog(getScene(), "Exit",
                    "Do you want to save the current automata before closing it?")) {
                Methods.save();
            }
        } else {
            if (!Alerts.showConfirmationDialog(getScene(), "Exit",
                    "Do you really want to close the current automata?")) {
                return;
            }
        }

        controllers.getGraphPanel().clear();
        controllers.getBottomBar().clear();
        model.setSaved(true);
        model.setOpenedFile(null);
    }

    @FXML
    private void exit() {
        parent.exit();
    }
}
