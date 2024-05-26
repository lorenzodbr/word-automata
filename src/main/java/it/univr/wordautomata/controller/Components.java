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

/*
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

    private Components(){}

    public static Components getInstance(){
        if(instance == null){
            instance = new Components();
        }
        return instance;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private static int p = 0;
    private static String peter = "PETER";

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

    public void setScene(Scene scene) {
        this.scene = scene;
        scene.setOnKeyPressed(this::peterAlert);
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setBottomBar(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void setContentZoomScrollPane(ContentZoomScrollPane contentZoomScrollPane) {
        this.contentZoomScrollPane = contentZoomScrollPane;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    public ContentZoomScrollPane getContentZoomScrollPane() {
        return contentZoomScrollPane;
    }
}
