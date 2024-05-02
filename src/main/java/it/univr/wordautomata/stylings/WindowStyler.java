package it.univr.wordautomata.stylings;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import it.univr.wordautomata.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class WindowStyler {

    public static void setDarkMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        setImmersiveDarkMode(stage, true);
    }

    public static void setLightMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        setImmersiveDarkMode(stage, false);
    }

    private static void setImmersiveDarkMode(Stage stage, boolean value) {
        try {
            WindowHandle handle = WindowHandle.tryFind(stage);
            handle.dwmSetBooleanValue(DwmAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE, value);
        } catch (HwndLookupException e) {
        }
    }

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
