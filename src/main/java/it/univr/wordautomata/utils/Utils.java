package it.univr.wordautomata.utils;

/**
 *
 * @author Lorenzo
 */
public class Utils {

    public enum Theme {
        LIGHT,
        DARK;
    }

    public final static Theme THEME = Theme.LIGHT;
    public final static boolean SET_MICA = false;

    public final static String FXML_FILENAME = "MainPanel";
    public final static String FONT_REGULAR_FILENAME = "Inter";
    public final static String FONT_BOLD_FILENAME = "Inter-Bold";
    public final static String FONTS_BASE_FOLDER = "/fonts/";
    public final static String FXML_BASE_FOLDER = "/fxml/";
    public final static String FXML_EXTENSION = ".fxml";
    public final static String FONTS_EXTENSION = ".ttf";
    public final static String TITLE = "Word Automata";

    public final static double MIN_HEIGHT = 500;
    public final static double MIN_WIDTH = 700;
    public final static int DEFAULT_FONT_SIZE = 12;
}
