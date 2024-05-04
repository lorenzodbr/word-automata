package it.univr.wordautomata.controller;

/*
 * This class is used to gather the main controllers of the application.
 */
public class Controllers {

    private MainPanel mainPanel;
    private BottomBar bottomBar;
    private GraphPanel graphPanel;

    private static Controllers instance;

    public static Controllers getInstance(){
        if(instance == null){
            instance = new Controllers();
        }
        return instance;
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

    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public BottomBar getBottomBar(){
        return bottomBar;
    }

    public GraphPanel getGraphPanel(){
        return graphPanel;
    }
}
