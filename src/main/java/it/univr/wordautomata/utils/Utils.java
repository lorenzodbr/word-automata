package it.univr.wordautomata.utils;

import it.univr.wordautomata.Main;
import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;

/**
 * Constants used in the application
 */
public class Utils {

    //Themes
    public enum Theme {
        LIGHT,
        DARK;

        public Theme next() {
            return this == LIGHT ? DARK : LIGHT;
        }

        public static Theme DEFAULT = LIGHT;
    }
    public final static boolean SET_MICA = false;

    //Filenames
    public final static String MAIN_PANEL_FXML_FILENAME = "MainPanel";
    public final static String BOTTOM_BAR_FXML_FILENAME = "BottomBar";
    public final static String SIDE_BAR_FXML_FILENAME = "SideBar";
    public final static String GRAPH_PANEL_FXML_FILENAME = "GraphPanel";
    public final static String ADD_STATE_MODAL_BODY_FXML_FILENAME = "AddStateModalBody";
    public final static String ADD_TRANSITION_MODAL_BODY_FXML_FILENAME = "AddTransitionModalBody";
    public final static String FONT_REGULAR_FILENAME = "Inter";
    public final static String FONT_BOLD_FILENAME = "Inter-Bold";
    public final static String FONT_ITALIC_FILENAME = "Inter-Italic";
    public final static String FONTS_BASE_FOLDER = "/fonts/";
    public final static String FXML_BASE_FOLDER = "/fxml/";
    public final static String STYLE_BASE_FOLDER = "/styles/";
    public final static String STYLE_FILENAME = "style";
    public final static String GRAPH_STYLE_FILENAME = "smartgraph";
    public final static String STYLE_EXTENSION = ".css";
    public final static String GRAPH_STYLE_EXTENSION = ".properties";
    public final static String FXML_EXTENSION = ".fxml";
    public final static String FONTS_EXTENSION = ".ttf";

    //Application properties
    public final static String TITLE = "Word Automata";

    //CSS classes
    public final static String SPEED_CIRCLE_CLASS = "speedCircle";
    public final static String ACTIVE_SPEED_CIRCLE_CLASS = "speedCircleActive";

    //Sizes
    public final static double HEIGHT = 700;
    public final static double WIDTH = 1030;
    public final static double MIN_HEIGHT = 500;
    public final static double MIN_WIDTH = 700;
    public final static int DEFAULT_FONT_SIZE = 12;

    //Global Objects
    public static final Random random = new Random(/* seed to reproduce*/);

    //Global methods
    public static void loadFonts(String... fileNames) {
        for (String fileName : fileNames) {
            Font.loadFont(Main.class
                    .getResourceAsStream(
                            Utils.FONTS_BASE_FOLDER
                            + fileName
                            + Utils.FONTS_EXTENSION
                    ),
                    Utils.DEFAULT_FONT_SIZE
            );
        }
    }

    public static boolean showConfirmationDialog(Scene scene, String title, String body) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(body);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        alert.initOwner(scene.getWindow());

        return ButtonType.YES.equals(alert.showAndWait().orElse(null));
    }

    //FXML methods
    public static void loadAndSetController(String path, Node n) {
        FXMLLoader loader = getLoader(path);
        loader.setRoot(n);
        loader.setController(n);

        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while loading component's FXML (" + path + ")");
        }
    }

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Utils.FXML_BASE_FOLDER + fxml + Utils.FXML_EXTENSION));
        return fxmlLoader;
    }

    //Misc
    public enum PlayBackSpeed {
        SLOW,
        REGULAR,
        FAST;

        public PlayBackSpeed next() {
            PlayBackSpeed[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static PlayBackSpeed DEFAULT = REGULAR;

        public String toString() {
            String s = "0.5";
            int ordinal = this.ordinal();

            if (ordinal > 0) {
                s = ordinal + "";
            }

            return s + "×";
        }
    }

    public enum PlayBackState {
        PAUSED,
        PLAYING;

        public PlayBackState next() {
            PlayBackState[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static PlayBackState DEFAULT = PAUSED;
    }
}
