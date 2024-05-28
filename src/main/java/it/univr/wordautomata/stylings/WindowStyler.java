package it.univr.wordautomata.stylings;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import it.univr.wordautomata.model.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The WindowStyler class provides methods to set the styling and theme of a JavaFX Stage window.
 */
public class WindowStyler {

    /**
     * Sets the dark mode styling for the specified stage.
     *
     * @param stage the stage to apply the dark mode styling to
     */
    public static void setDarkMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        setImmersiveDarkMode(stage, true);
    }

    /**
     * Sets the light mode for the specified stage.
     *
     * @param stage the stage to set the light mode for
     */
    public static void setLightMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        setImmersiveDarkMode(stage, false);
    }

    /**
     * Sets the immersive dark mode for the specified stage.
     *
     * @param stage the stage for which to set the immersive dark mode
     * @param value the value indicating whether to enable or disable immersive dark mode
     */
    private static void setImmersiveDarkMode(Stage stage, boolean value) {
        Platform.runLater(() -> {
            FXWinUtil.setImmersiveDarkMode(stage, value);
        });
    }

    /**
     * Sets the theme of the specified stage based on the current theme selected in the model.
     *
     * @param stage the stage to set the theme for
     */
    public static void setTheme(Stage stage) {
        switch (Model.getInstance().getTheme()) {
            case DARK:
                setDarkMode(stage);
                break;
            case LIGHT:
                setLightMode(stage);
                break;
        }
    }
}
