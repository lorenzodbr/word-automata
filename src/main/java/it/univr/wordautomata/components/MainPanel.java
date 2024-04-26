package it.univr.wordautomata.components;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.WordAutomata;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Main Panel wrapper
 */
public class MainPanel extends BorderPane {

    private BottomBar bottomBar;
    private GraphPanel graphPanel;
    private SideBar sideBar;

    private WordAutomata parent;

    @FXML
    private MenuItem autoPositioningMenuItem;

    @FXML
    private MenuItem addTransitionMenuItem;

    @FXML
    private MenuItem setInitialStateMenuItem;

    @FXML
    private MenuItem clearGraphMenuItem;

    @FXML
    private MenuItem darkThemeMenuItem;

    public MainPanel(WordAutomata parent) {
        Utils.loadAndSetController(Utils.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        addGraphPanel();
        addBottomBar();

        styleAutoPositioningMenuItem();
        styleDarkThemeMenuItem();
    }

    private void addBottomBar() {
        setBottom(bottomBar = new BottomBar(this));
    }

    private void addGraphPanel() {
        setCenter(graphPanel = new GraphPanel(this));
        Platform.runLater(() -> graphPanel.requestFocus());
    }

    private void addSideBar() {
        setLeft(sideBar = new SideBar());
    }

    GraphPanel getGraphPanel() {
        return graphPanel;
    }

    BottomBar getBottomBar() {
        return bottomBar;
    }

    SideBar getSideBar() {
        return sideBar;
    }

    @FXML
    private void addState() {
        if (graphPanel.addVertex()) {
            setAddTransitionMenuItemEnabled(true);
            setInitialStateMenuItemEnabled(true);
        }
    }

    @FXML
    private void addTransition() {
        graphPanel.addEdge();
    }

    @FXML
    private void clearGraph() {
        if (Utils.showConfirmationDialog(getScene(), "Clear graph", "Do you really want to clear the graph?")) {
            graphPanel.clearGraph();
            setClearGraphMenuItemEnabled(false);
            setAddTransitionMenuItemEnabled(false);
            setInitialStateMenuItemEnabled(false);
        }
    }
    
    @FXML
    private void setInitialState(){
        
    }

    @FXML
    private void toggleAutoPositioning() {
        graphPanel.setAutoPositioning(!graphPanel.getAutoPositioningEnabled());
        styleAutoPositioningMenuItem();
    }

    private void styleAutoPositioningMenuItem() {
        autoPositioningMenuItem.setGraphic(graphPanel.getAutoPositioningEnabled()
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    @FXML
    private void toggleDarkTheme() {
        parent.toggleDarkTheme();
        styleDarkThemeMenuItem();
    }

    public Theme getTheme() {
        return parent.getTheme();
    }

    private void styleDarkThemeMenuItem() {
        darkThemeMenuItem.setGraphic(parent.getTheme() == Theme.DARK
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    private void setAutoPositioningMenuItemEnabled(boolean value) {
        autoPositioningMenuItem.setDisable(!value);
    }

    void setAddTransitionMenuItemEnabled(boolean value) {
        addTransitionMenuItem.setDisable(!value);
    }

    void setInitialStateMenuItemEnabled(boolean value) {
        setInitialStateMenuItem.setDisable(!value);
    }

    void setClearGraphMenuItemEnabled(boolean value) {
        clearGraphMenuItem.setDisable(!value);
    }

    void showVertexDetails(SmartGraphVertex<State> v) {

    }

    void showEdgeDetails(SmartGraphEdge<Transition, State> e) {

    }

    @FXML
    private void exit() {
        //TODO: ask to save file, ...
        if (Utils.showConfirmationDialog(getScene(), "Exit", "Do you really want to exit the application?")) {
            System.exit(0);
        }
    }
}
