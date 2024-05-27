package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;

import it.univr.wordautomata.Main;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.utils.Constants;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This class is used to gather the main components of the application.
 */
public class Components {

    private Stage stage;
    private Scene scene;

    private MainPanel mainPanel;
    private BottomBar bottomBar;
    private GraphPanel graphPanel;
    private ContentZoomScrollPane contentZoomScrollPane;

    private static Components instance;

    private Components() {
    }

    /**
     * Returns the instance of the class.
     * 
     * @return the instance of the class
     */
    public static Components getInstance() {
        if (instance == null) {
            instance = new Components();
        }
        return instance;
    }

    /**
     * Sets the stage of the application.
     * 
     * @param stage the stage of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private static int p = 0;
    private static String peter = "PETER";

    /**
     * Shows an alert with Peter Griffin image if the user types "PETER".
     * 
     * @param e the key event
     */
    void peterAlert(KeyEvent e) {
        char c = e.getCode().getChar().charAt(0);

        if (peter.charAt(p) != c) {
            p = c == 'P' ? 1 : 0;
        } else {
            p++;
            if (p == peter.length()) {
                Alert peterAlert = Alerts.createAlert(null, scene, "Peter Alert", "", "", ButtonType.OK);
                peterAlert.setGraphic(new ImageView(new Image(Main.class
                        .getResourceAsStream(
                                Constants.ICON_BASE_FOLDER + "Petergriffin" + Constants.ICON_EXTENSION),
                        100, 100, true, true)));
                peterAlert.initOwner(stage.getOwner());
                peterAlert.showAndWait();
                p = 0;
            }
        }
    }

    /**
     * Sets the scene of the application.
     * 
     * @param scene the scene of the application
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        scene.setOnKeyPressed(this::peterAlert);
    }

    /**
     * Sets the main panel of the application.
     * 
     * @param mainPanel the main panel of the application
     */
    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    /**
     * Sets the bottom bar of the application.
     * 
     * @param bottomBar the bottom bar of the application
     */
    public void setBottomBar(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    /**
     * Sets the graph panel of the application.
     * 
     * @param graphPanel the graph panel of the application
     */
    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    /**
     * Sets the content zoom scroll pane of the application.
     * 
     * @param contentZoomScrollPane the content zoom scroll pane of the application
     */
    public void setContentZoomScrollPane(ContentZoomScrollPane contentZoomScrollPane) {
        this.contentZoomScrollPane = contentZoomScrollPane;
    }

    /**
     * Returns the stage of the application.
     * 
     * @return the stage of the application
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the scene of the application.
     * 
     * @return the scene of the application
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Returns the main panel of the application.
     * 
     * @return the main panel of the application
     */
    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns the bottom bar of the application.
     * 
     * @return the bottom bar of the application
     */
    public BottomBar getBottomBar() {
        return bottomBar;
    }

    /**
     * Returns the graph panel of the application.
     * 
     * @return the graph panel of the application
     */
    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * Returns the content zoom scroll pane of the application.
     * 
     * @return the content zoom scroll pane of the application
     */
    public ContentZoomScrollPane getContentZoomScrollPane() {
        return contentZoomScrollPane;
    }
}
