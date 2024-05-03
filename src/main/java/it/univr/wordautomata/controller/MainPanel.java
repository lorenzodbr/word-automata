package it.univr.wordautomata.controller;

import it.univr.wordautomata.WordAutomata;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.Theme;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Main Panel wrapper
 */
public class MainPanel extends BorderPane {

    private WordAutomata parent;
    private BottomBar bottomBar;
    private GraphPanel graphPanel;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem autoPositioningMenuItem;

    @FXML
    private MenuItem addTransitionMenuItem;

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

    private Model model;

    public MainPanel(WordAutomata parent) {
        Methods.loadAndSetController(Constants.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        this.model = Model.getInstance();

        addGraphPanel();
        addBottomBar();

        styleMenuItems();
        initBindings();
    }

    private void addBottomBar() {
        setBottom(bottomBar = new BottomBar(this));
    }

    private void addGraphPanel() {
        setCenter(graphPanel = new GraphPanel(this));
        Platform.runLater(() -> graphPanel.requestFocus());
    }

    GraphPanel getGraphPanel() {
        return graphPanel;
    }

    BottomBar getBottomBar() {
        return bottomBar;
    }

    @FXML
    private void addState() {
        graphPanel.addVertex();
    }

    @FXML
    private void addTransition() {
        graphPanel.addEdge();
    }

    @FXML
    private void clearGraph() {
        if (Alerts.showConfirmationDialog(getScene(), "Clear graph", "Do you really want to clear the graph?")) {
            bottomBar.clear();
            graphPanel.clearGraph();
            model.setInitialState(null);
        }
    }

    @FXML
    private void setInitialState() {
        graphPanel.setInitialState();
    }

    @FXML
    private void selectState() {
        graphPanel.selectState();
    }
    
    @FXML
    private void selectTransition() {
        graphPanel.selectTransition();
    }

    @FXML
    private void toggleAutoPositioning() {
        model.toggleAutoPositioning();
        styleAutoPositioningMenuItem();
    }

    private void initBindings() {
        BooleanBinding noVertexBinding = model.atLeastOneVertexProperty().not();
        BooleanBinding noEdgeBinding = model.atLeastOneEdgeProperty().not();

        clearGraphMenuItem.disableProperty().bind(noVertexBinding);
        addTransitionMenuItem.disableProperty().bind(noVertexBinding);
        setInitialStateMenuItem.disableProperty().bind(noVertexBinding);
        selectStateMenuItem.disableProperty().bind(noVertexBinding);
        selectTransitionMenuItem.disableProperty().bind(noEdgeBinding);
    }

    private void styleMenuItems() {
        styleAutoPositioningMenuItem();
        styleDarkThemeMenuItem();
    }

    private void styleAutoPositioningMenuItem() {
        autoPositioningMenuItem.setGraphic(model.autoPositionProperty().get()
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    private void styleDarkThemeMenuItem() {
        darkThemeMenuItem.setGraphic(model.getTheme() == Theme.DARK
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    @FXML
    private void toggleDarkTheme() {
        parent.toggleTheme();
        styleDarkThemeMenuItem();
    }

    public double getMenuBarHeight() {
        return menuBar.getHeight();
    }

    @FXML
    private void showInfo() {
        Alerts.showInformationDialog(getScene(), "About", Constants.TITLE + " v" + Constants.VERSION, "Created by Lorenzo Di Berardino, Mateo Gjika and Filippo Milani.");
    }

    @FXML
    private void exit() {
        //TODO: ask to save file, ...
        parent.exit();
    }
}
