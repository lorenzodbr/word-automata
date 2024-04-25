package it.univr.wordautomata.utils;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import it.univr.wordautomata.Main;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.stage.Window;

/**
 * Constants used in the application
 */
public class Utils {

    //FXML methods
    public static void loadAndSetController(String path, Node n) {
        FXMLLoader loader = getLoader(path);
        loader.setRoot(n);
        loader.setController(n);

        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while loading component's FXML");
        }
    }

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Utils.FXML_BASE_FOLDER + fxml + Utils.FXML_EXTENSION));
        return fxmlLoader;
    }

    public static String showInputModal(Scene scene, String title, String header, String body) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(body);
        dialog.initOwner(scene.getWindow());

        try {
            for (var pc : scene.getRoot().getPseudoClassStates()) {
                dialog.getDialogPane().pseudoClassStateChanged(pc, true);
                dialog.getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
            }
        } catch (Exception ignored) {
        }

        return dialog.showAndWait().orElse(null);
    }

    //Themes
    public enum Theme {
        LIGHT,
        DARK;

        public Theme next() {
            return this == LIGHT ? DARK : LIGHT;
        }

        public static Theme getDefault() {
            return LIGHT;
        }
    }
    public final static Theme THEME = Theme.LIGHT;
    public final static boolean SET_MICA = true;

    //Filenames
    public final static String MAIN_PANEL_FXML_FILENAME = "MainPanel";
    public final static String BOTTOM_BAR_FXML_FILENAME = "BottomBar";
    public final static String SIDE_BAR_FXML_FILENAME = "SideBar";
    public final static String GRAPH_PANEL_FXML_FILENAME = "GraphPanel";
    public final static String FONT_REGULAR_FILENAME = "Inter";
    public final static String FONT_BOLD_FILENAME = "Inter-Bold";
    public final static String FONT_ITALIC_FILENAME = "Inter-Italic";
    public final static String FONTS_BASE_FOLDER = "/fonts/";
    public final static String FXML_BASE_FOLDER = "/fxml/";
    public final static String STYLE_BASE_FOLDER = "/styles/";
    public final static String STYLE_FILENAME = "style";
    public final static String STYLE_EXTENSION = ".css";
    public final static String FXML_EXTENSION = ".fxml";
    public final static String FONTS_EXTENSION = ".ttf";

    //Application properties
    public final static String TITLE = "Word Automata";

    //CSS classes
    public final static String SPEED_CIRCLE_CLASS = "speedCircle";
    public final static String ACTIVE_SPEED_CIRCLE_CLASS = "speedCircleActive";

    //Sizes
    public final static double HEIGHT = 700;
    public final static double WIDTH = 1100;
    public final static double MIN_HEIGHT = 500;
    public final static double MIN_WIDTH = 700;
    public final static int DEFAULT_FONT_SIZE = 12;

    //Global Objects
    public static final Random random = new Random(/* seed to reproduce*/);

    //Global methods
    public static void loadFonts(String... fileNames) {
        for (String fileName : fileNames) {
            Font.loadFont(Main.class
                    .getResourceAsStream(Utils.FONTS_BASE_FOLDER + fileName + Utils.FONTS_EXTENSION), Utils.DEFAULT_FONT_SIZE);
        }
    }

    //Graph methods
    public static Vertex<State> getRandomVertex(Graph<State, Transition> g) {

        int size = g.numVertices();
        if (size <= 0) {
            return null;
        }

        int rand = random.nextInt(size);
        Vertex<State> existing = null;
        int i = 0;
        for (Vertex<State> v : g.vertices()) {
            existing = v;
            if (i++ == rand) {
                break;
            }
        }
        return existing;

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

        public static PlayBackSpeed getDefault() {
            return REGULAR;
        }

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

        public static PlayBackState getDefault() {
            return PAUSED;
        }
    }
}
