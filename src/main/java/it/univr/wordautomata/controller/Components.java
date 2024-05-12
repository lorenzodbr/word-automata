package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;

import javafx.scene.Scene;
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

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void setMainPanel(MainPanel mainPanel){
        this.mainPanel = mainPanel;
    }

    public void setBottomBar(BottomBar bottomBar){
        this.bottomBar = bottomBar;
    }

    public void setGraphPanel(GraphPanel graphPanel){
        this.graphPanel = graphPanel;
    }

    public void setContentZoomScrollPane(ContentZoomScrollPane contentZoomScrollPane){
        this.contentZoomScrollPane = contentZoomScrollPane;
    }

    public Stage getStage(){
        return stage;
    }

    public Scene getScene(){
        return scene;
    }

    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public BottomBar getBottomBar(){
        return bottomBar;
    }

    public GraphPanel getGraphPanel(){
        return graphPanel;
    }

    public ContentZoomScrollPane getContentZoomScrollPane(){
        return contentZoomScrollPane;
    }
}
