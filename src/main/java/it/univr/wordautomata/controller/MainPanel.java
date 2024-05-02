package it.univr.wordautomata.controller;

import it.univr.wordautomata.WordAutomata;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import javafx.application.Platform;
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

    public MainPanel(WordAutomata parent) {
        Utils.loadAndSetController(Utils.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
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
            graphPanel.clearGraph();
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
        graphPanel.toggleAutoPositioning();
        styleAutoPositioningMenuItem();
    }

    private void initBindings() {
        clearGraphMenuItem.disableProperty().bind(graphPanel.atLeastOneVertexProperty().not());
        addTransitionMenuItem.disableProperty().bind(graphPanel.atLeastOneVertexProperty().not());
        setInitialStateMenuItem.disableProperty().bind(graphPanel.atLeastOneVertexProperty().not());
        selectStateMenuItem.disableProperty().bind(graphPanel.atLeastOneVertexProperty().not());
        selectTransitionMenuItem.disableProperty().bind(graphPanel.atLeastOneEdgeProperty().not());
    }

    private void styleMenuItems() {
        styleAutoPositioningMenuItem();
        styleDarkThemeMenuItem();
    }

    private void styleAutoPositioningMenuItem() {
        autoPositioningMenuItem.setGraphic(graphPanel.autoPositionProperty().get()
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    private void styleDarkThemeMenuItem() {
        darkThemeMenuItem.setGraphic(Model.getInstance().getTheme() == Theme.DARK
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    @FXML
    private void toggleDarkTheme() {
        parent.toggleDarkTheme();
        styleDarkThemeMenuItem();
    }

    public double getMenuBarHeight() {
        return menuBar.getHeight();
    }

    @FXML
    private void showInfo() {
        Alerts.showInformationDialog(getScene(), "About", "Created by Lorenzo Di Berardino, Mateo Gjika and Filippo Milani.");
    }

    @FXML
    private void exit() {
        //TODO: ask to save file, ...
        parent.exit();
    }
}
