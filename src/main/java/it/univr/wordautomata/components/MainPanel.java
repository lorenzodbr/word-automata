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

    private WordAutomata parent;
    private BottomBar bottomBar;
    private GraphPanel graphPanel;
    private SideBar sideBar;

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
        graphPanel.addVertex();
    }

    @FXML
    private void addTransition() {
        graphPanel.addEdge();
    }

    @FXML
    private void clearGraph() {
        if (Utils.showConfirmationDialog(getScene(), "Clear graph", "Do you really want to clear the graph?")) {
            graphPanel.clearGraph();
        }
    }

    @FXML
    private void setInitialState() {

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
        darkThemeMenuItem.setGraphic(parent.getTheme() == Theme.DARK
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

    @FXML
    private void exit() {
        //TODO: ask to save file, ...
        parent.exit();
    }
}
