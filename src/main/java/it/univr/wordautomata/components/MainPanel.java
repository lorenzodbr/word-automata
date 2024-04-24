package it.univr.wordautomata.components;

import it.univr.wordautomata.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * Main Panel wrapper
 */
public class MainPanel extends BorderPane {
    private BottomBar bottomBar;
    private GraphPanel graphPanel;
    private SideBar sideBar;
    
    public MainPanel() {
        if(Utils.loadAndSetController(Utils.MAIN_PANEL_FXML_FILENAME, this)){
            addBottomBar();
            addGraphPanel();
            addSideBar();
        }
    }
    
    private void addBottomBar(){
        bottomBar = new BottomBar();
        setBottom(bottomBar);
    }
    
    private void addGraphPanel(){
        graphPanel = new GraphPanel();
        setCenter(graphPanel);
    }

    private void addSideBar() {
        sideBar = new SideBar();
        setLeft(sideBar);
    }
    
    @FXML
    private void addNode(){
        graphPanel.addNode();
    }
    
    @FXML
    private void toggleAutoPositioning(){
        graphPanel.toggleAutoPositioning();
    }
}
