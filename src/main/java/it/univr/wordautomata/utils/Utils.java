package it.univr.wordautomata.utils;

import it.univr.wordautomata.Main;
import it.univr.wordautomata.components.SideBar;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Constants used in the application
 */
public class Utils {

    public static boolean loadAndSetController(String path, Node n) {
        FXMLLoader loader = Utils.getLoader(path);
        loader.setRoot(n);
        loader.setController(n);

        try {
            loader.load();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    //Themes
    public enum Theme {
        LIGHT,
        DARK;
        
        Theme next(){
            return this == LIGHT ? DARK : LIGHT;
        }
    }
    public final static Theme THEME = Theme.LIGHT;
    public final static boolean SET_MICA = false;

    //Filenames
    public final static String MAIN_PANEL_FXML_FILENAME = "MainPanel";
    public final static String BOTTOM_BAR_FXML_FILENAME = "BottomBar";
    public final static String SIDE_BAR_FXML_FILENAME = "SideBar";
    public final static String GRAPH_PANEL_FXML_FILENAME = "GraphPanel";
    public final static String FONT_REGULAR_FILENAME = "Inter";
    public final static String FONT_BOLD_FILENAME = "Inter-Bold";
    public final static String FONTS_BASE_FOLDER = "/fonts/";
    public final static String FXML_BASE_FOLDER = "/fxml/";
    public final static String FXML_EXTENSION = ".fxml";
    public final static String FONTS_EXTENSION = ".ttf";
    
    //Application properties
    public final static String TITLE = "Word Automata";

    //Sizes
    public final static double HEIGHT = 700;
    public final static double WIDTH = 1100;
    public final static double MIN_HEIGHT = 500;
    public final static double MIN_WIDTH = 700;
    public final static int DEFAULT_FONT_SIZE = 12;

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Utils.FXML_BASE_FOLDER + fxml + Utils.FXML_EXTENSION));
        return fxmlLoader;
    }
}
