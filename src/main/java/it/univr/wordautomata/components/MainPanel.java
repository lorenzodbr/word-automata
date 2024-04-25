package it.univr.wordautomata.components;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.WordAutomata;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.boxicons.BoxiconsLogos;
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
    private MenuItem clearGraphMenuItem;

    @FXML
    private MenuItem darkThemeMenuItem;

    public MainPanel(WordAutomata parent) {
        Utils.loadAndSetController(Utils.MAIN_PANEL_FXML_FILENAME, this);

        this.parent = parent;
        addGraphPanel();
        addBottomBar();
        addSideBar();

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
    private void addNode() {
        graphPanel.addNode();
    }

    @FXML
    private void clearGraph() {
        graphPanel.clearGraph();
        setClearGraphMenuItemEnabled(false);
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

    private void styleDarkThemeMenuItem() {
        darkThemeMenuItem.setGraphic(parent.getTheme() == Theme.DARK
                ? new FontIcon(BoxiconsRegular.CHECK) : null);
    }

    private void setAutoPositioningMenuItemEnabled(boolean value) {
        autoPositioningMenuItem.setDisable(!value);
    }

    void setClearGraphMenuItemEnabled(boolean value) {
        clearGraphMenuItem.setDisable(!value);
    }

    @FXML
    private void exit() {
        //TODO: ask to save file, ...

        System.exit(0);
    }
}
