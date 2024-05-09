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
 * The MainPanel class represents the main panel of the WordAutomata
 * application.
 * It extends the BorderPane class and serves as the container for the
 * application's UI components.
 * 
 * The MainPanel class is responsible for initializing and managing the menu
 * bar, menu items, and other UI elements.
 * It also handles user interactions and delegates the actions to the
 * appropriate components.
 * 
 * The MainPanel class interacts with the Model and Components classes to update
 * and retrieve data.
 * It also communicates with the GraphPanel and BottomBar components to display
 * and manipulate the graph.
 * 
 * The MainPanel class provides methods for adding states and transitions,
 * clearing the graph, setting the initial state,
 * selecting states and transitions, toggling auto positioning, opening and
 * saving automata files, and managing the theme.
 * 
 * The MainPanel class also provides methods for initializing bindings, styling
 * menu items, and handling various events.
 * 
 * @param parent The WordAutomata instance that owns the MainPanel.
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
    private Components components;

    public MainPanel(WordAutomata parent) {
        Methods.loadAndSetController(Constants.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        this.model = Model.getInstance();
        this.components = Components.getInstance();

        // order is important, GraphPanel expects BottomBar to be initialized before it
        addBottomBar();
        addGraphPanel();

        styleMenuItems();
        initBindings();
    }

    private void addBottomBar() {
        components.setBottomBar(new BottomBar());
        setBottom(components.getBottomBar());
    }

    private void addGraphPanel() {
        components.setGraphPanel(new GraphPanel());
        setCenter(components.getGraphPanel());
        Platform.runLater(() -> components.getGraphPanel().requestFocus());
    }

    @FXML
    private void addState() {
        components.getGraphPanel().addVertex();
    }

    @FXML
    private void addTransition() {
        components.getGraphPanel().addEdge();
    }

    @FXML
    public void clearGraph() {
        if (Alerts.showConfirmationDialog(getScene(), "Clear graph", "Do you really want to clear the graph?")) {
            components.getBottomBar().clear();
            components.getGraphPanel().clear();
        }
    }

    @FXML
    private void setInitialState() {
        components.getGraphPanel().chooseInitialState();
    }

    @FXML
    private void selectState() {
        components.getGraphPanel().selectState();
    }

    @FXML
    private void selectTransition() {
        components.getGraphPanel().selectTransition();
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
        Components.getInstance().getBottomBar().computePath();
    }

    @FXML
    private void loadSampleAutomata() {

        String message = "Do you really want to load the sample automata?";

        if (!model.isSaved()) {
            message += " You will lose any unsaved changes.";
        }

        if ((model.getOpenedFile() != null || !model.isSaved()) && !Alerts.showConfirmationDialog(getScene(), "Load sample automata",
                message)) {
            return;
        }

        model.updateGraph(model.initSampleGraph());
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

        components.getGraphPanel().clear();
        components.getBottomBar().clear();
        model.setSaved(true);
        model.setOpenedFile(null);
    }

    @FXML
    private void showLegend() {
        
    }

    @FXML
    private void exit() {
        parent.exit();
    }
}
