package it.univr.wordautomata.controller;

import java.io.File;
import java.util.List;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import org.kordamp.ikonli.javafx.FontIcon;

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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

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

    @FXML
    private MenuItem showStatsMenuItem;

    @FXML
    private Menu openRecentMenu;

    private Model model;
    private WordAutomata parent;
    private Components components;

    /**
     * Creates a new MainPanel.
     * 
     * @param parent The WordAutomata instance that owns the MainPanel
     */
    public MainPanel(WordAutomata parent) {
        Methods.loadAndSetController(Constants.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        this.model = Model.getInstance();
        this.components = Components.getInstance();

        // order is important, GraphPanel expects BottomBar to be initialized before it
        addBottomBar();
        addGraphPanel();

        initMenuItems();
        initBindings();
    }

    /**
     * Adds the bottom bar to the MainPanel.
     */
    private void addBottomBar() {
        components.setBottomBar(new BottomBar());
        setBottom(components.getBottomBar());
    }

    /**
     * Adds the graph panel to the MainPanel.
     */
    private void addGraphPanel() {
        components.setGraphPanel(new GraphPanel());
        setCenter(components.getGraphPanel());
        Platform.runLater(() -> components.getGraphPanel().requestFocus());
    }

    /**
     * Adds a new state to the graph.
     */
    @FXML
    private void addState() {
        components.getGraphPanel().addVertex();
    }

    /**
     * Adds a new transition to the graph.
     */
    @FXML
    private void addTransition() {
        components.getGraphPanel().addEdge();
    }

    /**
     * Clears the graph.
     */
    @FXML
    public void clearGraph() {
        if (Alerts.showConfirmationDialog(getScene(), "Clear", "Do you really want to clear the graph?")) {
            components.getBottomBar().clear();
            components.getGraphPanel().clear();
        }
    }

    /**
     * Sets the initial state of the automaton.
     */
    @FXML
    private void setInitialState() {
        components.getGraphPanel().chooseInitialState();
    }

    /**
     * Selects a state in the graph.
     */
    @FXML
    private void selectState() {
        components.getGraphPanel().selectState();
    }

    /**
     * Selects a transition in the graph.
     */
    @FXML
    private void selectTransition() {
        components.getGraphPanel().selectTransition();
    }

    /**
     * Toggles the auto positioning of the graph.
     */
    @FXML
    private void toggleAutoPositioning() {
        model.toggleAutoPositioning();
    }

    /**
     * Opens an automata file.
     */
    @FXML
    private void openAutomata() {
        // open file dialog
        File file = AutomataSaver.showOpenDialog();

        if (file != null) {
            if (file.exists() && file.isFile() && file.getPath().endsWith(Constants.AUTOMATA_EXTENSION)) {
                loadAutomata(file);
                loadRecentFiles();
            }
        }

    }

    /**
     * Loads an automata from a file.
     * 
     * @param file The file from which to load the automata
     */
    public void loadAutomata(File file) {
        if (!model.isSaved() && !Alerts.showConfirmationDialog(getScene(), "Open automata",
                "Do you really want to open a new automata? You will lose any unsaved changes.")) {
            return;
        }

        model.updateGraph(AutomataSaver.read(file));
        Components.getInstance().getBottomBar().computePath();
    }

    /**
     * Loads the sample automata.
     */
    @FXML
    private void loadSampleAutomata() {

        String message = "Do you really want to load the sample automata?";

        if (!model.isSaved()) {
            message += " You will lose any unsaved changes.";
        }

        if ((model.getOpenedFile() != null || !model.isSaved())
                && !Alerts.showConfirmationDialog(getScene(), "Load sample automata",
                        message)) {
            return;
        }

        model.updateGraph(model.initSampleGraph());
        model.setOpenedFile(null);
        model.setSaved(true);
        components.getGraphPanel().closeSideBar();
    }

    /**
     * Saves the automata to a file.
     */
    @FXML
    public void saveAutomata() {
        Methods.save();
    }

    /**
     * Saves the automata to a new file.
     */
    @FXML
    public void saveAsAutomata() {
        Methods.saveAs();
    }

    /**
     * Initializes the bindings of the MainPanel.
     */
    private void initBindings() {
        BooleanBinding noVertexBinding = model.atLeastOneVertexProperty().not();
        BooleanBinding noEdgeBinding = model.atLeastOneEdgeProperty().not();

        clearGraphMenuItem.disableProperty().bind(noVertexBinding);
        addTransitionMenuItem.disableProperty().bind(noVertexBinding);
        setInitialStateMenuItem.disableProperty().bind(noVertexBinding);
        autoPositioningMenuItem.disableProperty().bind(noVertexBinding);
        selectStateMenuItem.disableProperty().bind(noVertexBinding);
        selectTransitionMenuItem.disableProperty().bind(noEdgeBinding);
        showStatsMenuItem.disableProperty().bind(noEdgeBinding);
        closeMenuItem.disableProperty().bind(model.openedFileProperty().isNull());
    }

    /**
     * Initializes the menu items of the MainPanel.
     */
    private void initMenuItems() {
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

        loadRecentFiles();
    }

    /**
     * Loads the recent files.
     */
    private void loadRecentFiles() {
        List<File> recentFiles = AutomataSaver.getRecentFiles();

        if (recentFiles != null && !recentFiles.isEmpty()) {
            openRecentMenu.getItems().clear();
        }

        for (File file : recentFiles) {
            MenuItem item = new MenuItem();

            item.textProperty()
                    .bind(Bindings.concat(file.getAbsolutePath(),
                            Bindings.when(model.openedFileProperty().isEqualTo(file))
                                    .then(" (Current)")
                                    .otherwise(""),
                            Bindings.when(
                                    Bindings.createBooleanBinding(() -> file.exists(), model.openedFileProperty()))
                                    .then("").otherwise(" (Not found)")));

            item.setOnAction(e -> loadAutomata(file));
            openRecentMenu.getItems().add(item);

            item.disableProperty().bind(Bindings.createBooleanBinding(() -> (model.getOpenedFile() != null
                    && model.getOpenedFile().getAbsolutePath().equals(file.getAbsolutePath())) || !file.exists(),
                    model.openedFileProperty()));
        }

        if (!recentFiles.isEmpty()) {
            openRecentMenu.getItems().addAll(new SeparatorMenuItem(), new MenuItem("Clear recent files") {
                {
                    setOnAction(e -> {
                        if (Alerts.showConfirmationDialog(getScene(), "Clear recent files",
                                "Do you really want to clear the recent files list?")) {
                            AutomataSaver.clearRecentFiles();
                            openRecentMenu.getItems().clear();
                            openRecentMenu.getItems().add(new MenuItem("No recent files") {
                                {
                                    setDisable(true);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    /**
     * Toggles the dark theme.
     */
    @FXML
    private void toggleDarkTheme() {
        parent.toggleTheme();
    }

    /**
     * Gets the height of the menu bar.
     * 
     * @return the height of the menu bar
     */
    public double getMenuBarHeight() {
        return menuBar.getHeight();
    }

    /**
     * Shows the info about the application.
     */
    @FXML
    private void showInfo() {
        Alerts.createAlert(null, getScene(), "About", Constants.TITLE + " v" + Constants.VERSION,
                "Created by Lorenzo Di Berardino, Mateo Gjika and Filippo Milani.", ButtonType.CLOSE).showAndWait();
    }

    /**
     * Closes the current automata.
     */
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

    /**
     * Shows the legend of the automaton.
     */
    @FXML
    private void showLegend() {
        Alert alert = Alerts.createAlert(null, getScene(), "Legend", null, null, ButtonType.CLOSE);
        alert.getDialogPane().setContent(new LegendModalBody());

        alert.showAndWait();
    }

    /**
     * Shows the statistics of the automaton.
     */
    @FXML
    private void showStats() {
        Alert alert = Alerts.createAlert(null, getScene(), "Statistics", null, null, ButtonType.CLOSE);
        alert.getDialogPane().setContent(new StatisticsModalBody());
        alert.showAndWait();
    }

    /**
     * Exits the application.
     */
    @FXML
    private void openGitHub() {
        Methods.openLink(Constants.GITHUB_URL);
    }

    @FXML
    private void exit() {
        parent.exit();
    }
}
