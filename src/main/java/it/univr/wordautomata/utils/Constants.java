package it.univr.wordautomata.utils;

import java.io.File;
import java.util.Random;

/**
 * Constants used in the application
 */
public class Constants {

    /*
     * Filenames
     */
    public final static String MAIN_PANEL_FXML_FILENAME = "MainPanel";
    public final static String BOTTOM_BAR_FXML_FILENAME = "BottomBar";
    public final static String SIDE_BAR_FXML_FILENAME = "SideBar";
    public final static String GRAPH_PANEL_FXML_FILENAME = "GraphPanel";
    public final static String STATE_MODAL_BODY_FXML_FILENAME = "StateModalBody";
    public final static String TRANSITION_MODAL_BODY_FXML_FILENAME = "TransitionModalBody";
    public final static String ADD_STATE_MODAL_BODY_FXML_FILENAME = "AddStateModalBody";
    public final static String ADD_TRANSITION_MODAL_BODY_FXML_FILENAME = "AddTransitionModalBody";
    public final static String STATISTICS_MODAL_BODY_FXML_FILENAME = "StatisticsModalBody";
    public final static String LEGEND_MODAL_BODY_FXML_FILENAME = "LegendModalBody";
    public final static String WHY_MODAL_BODY_FXML_FILENAME = "WhyModalBody";
    public final static String FONT_REGULAR_FILENAME = "Inter";
    public final static String FONT_BOLD_FILENAME = "Inter-Bold";
    public final static String FONT_ITALIC_FILENAME = "Inter-Italic";
    public final static String FONTS_BASE_FOLDER = "/fonts/";
    public final static String FXML_BASE_FOLDER = "/fxml/";
    public final static String STYLE_BASE_FOLDER = "/styles/";
    public final static String ICON_BASE_FOLDER = "/icons/";
    public final static String STYLE_FILENAME = "style";
    public final static String GRAPH_STYLE_FILENAME = "smartgraph";
    public final static String DEFAULT_AUTOMATA_FILENAME = "Untitled";
    public final static String ICON_FILENAME = "logo";
    public final static String RECENT_FILES_FILENAME = "recentFiles";
    public final static String STYLE_EXTENSION = ".css";
    public final static String GRAPH_STYLE_EXTENSION = ".properties";
    public final static String FXML_EXTENSION = ".fxml";
    public final static String FONTS_EXTENSION = ".ttf";
    public final static String ICON_EXTENSION = ".png";
    public final static String AUTOMATA_EXTENSION = ".automata";

    /*
     * Application properties
     */
    public final static String VERSION = "1.0.2";
    public final static String TITLE = "Word Automata";
    public final static boolean DEFAULT_AUTO_POSITION = false;
    public final static String GITHUB_URL = "https://github.com/lorenzodbr/word-automata";

    /*
     * CSS classes
     */
    public final static String SPEED_CIRCLE_CLASS = "speed-circle";
    public final static String ACTIVE_SPEED_CIRCLE_CLASS = "speed-circle-active";
    public final static String INITIAL_STATE_CLASS = "initial-state";
    public final static String FINAL_STATE_CLASS = "final-state";
    public final static String ROUNDED_CORNERS_CLASS = "rounded-corners";
    public final static String ACTIVE_EDGE_CLASS = "active-edge";
    public final static String ACTIVE_BUTTON_CLASS = "active-button";
    public final static String MENU_ITEM_DANGER_CLASS = "menu-item-danger";
    public final static String ZOOM_LABEL_CLASS = "zoom-label";
    public final static String NO_PATH_FOUND_TEXT_CLASS = "no-path-found-label";
    public final static String NO_PATH_FOUND_PANEL_CLASS = "no-path-found-panel";
    public final static String CIRCLE_SUCCESS_CLASS = "circle-success";
    public final static String CIRCLE_FAILURE_CLASS = "circle-failure";

    public final static String TRANSITION_CSS_HORIZONTAL_ALREADY_COLORED = "-fx-stroke: linear-gradient(to %s, -color-danger-4 %d%%, -color-neutral-emphasis %d%%, -color-danger-4 %d%%, -color-danger-4);";
    public final static String TRANSITION_CSS_HORIZONTAL = "-fx-stroke: linear-gradient(from %d%% 0%% to %d%% 0%%, -color-danger-4, -color-neutral-emphasis);";
    public final static String TRANSITION_CSS_ALREADY_COLORED = "-fx-stroke: linear-gradient(to %s,-color-danger-4 %d%%, -color-neutral-emphasis %d%%, -color-danger-4 %d%%, -color-danger-4);";
    public final static String TRANSITION_CSS = "-fx-stroke: linear-gradient(from 0%% %d%% to 0%% %d%%, -color-danger-4, -color-neutral-emphasis);";

    /*
     * Numerical Constants
     */
    public final static double HEIGHT = 689;
    public final static double WIDTH = 1030;
    public final static double MIN_HEIGHT = 400;
    public final static double MIN_WIDTH = 700;
    public final static int DEFAULT_FONT_SIZE = 10;
    public final static int SIDEBAR_MAX_WIDTH = 250;
    public final static int TRANSITION_ROW_MIN_WIDTH = 20;
    public final static int DEFAULT_PLAYBACK_DURATION_MILLIS = 750;
    public final static int MAX_RECENT_FILES = 20;

    /*
     * Global Objects
     */
    public static final Random RANDOM = new Random();
    public static final File DEFAULT_AUTOMATA_FILE = new File(
            System.getProperty("user.home") + System.getProperty("file.separator")
                    + DEFAULT_AUTOMATA_FILENAME + AUTOMATA_EXTENSION);
    public static final File INITIAL_DIRECTORY = new File(System.getProperty("user.home"));

    /*
     * Enums
     */

    /**
     * The different speed at which the timeline can be played
     */
    public enum PlayBackSpeed {
        SLOW,
        REGULAR,
        FAST;

        /*
         * Get the next playback speed
         * 
         * @return the next playback speed
         */
        public PlayBackSpeed next() {
            PlayBackSpeed[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }

        /**
         * Return the value of the playback speed
         * 
         * @return the value of the playback speed
         */
        public double getValue() {
            return this.ordinal() == 0 ? 0.5 : this.ordinal();
        }

        public static PlayBackSpeed DEFAULT = REGULAR;

        public String toString() {
            String s = "0.5";
            int ordinal = this.ordinal();

            if (ordinal > 0) {
                s = String.valueOf(ordinal);
            }

            return s + "×";
        }
    }

    /**
     * The different playback states of the timeline
     */
    public enum PlayBackState {
        PAUSED,
        PLAYING;

        /**
         * Get the next playback state
         *
         * @return the next playback state
         */
        public PlayBackState next() {
            PlayBackState[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static PlayBackState DEFAULT = PAUSED;
    }

    /**
     * Application themes
     */
    public enum Theme {
        LIGHT,
        DARK;

        /**
         * Get the opposite theme
         *
         * @return the opposite theme
         */
        public Theme next() {
            return this == LIGHT ? DARK : LIGHT;
        }

        public static Theme DEFAULT = LIGHT;
    }

    /**
     * Edge orientations
     */
    public enum Orientation {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST;

        /**
         * Returns the CSS orientation corresponding to the enum value.
         *
         * @return the CSS orientation as a string ("top", "bottom", "right", or "left")
         */
        public String getCssOrientation() {
            switch (this) {
                case NORTH:
                    return "top";
                case SOUTH:
                    return "bottom";
                case EAST:
                case NORTH_EAST:
                case SOUTH_EAST:
                    return "right";
                case WEST:
                case NORTH_WEST:
                case SOUTH_WEST:
                    return "left";
                default:
                    return "right";
            }
        }
    }
}
