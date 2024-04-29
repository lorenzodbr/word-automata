package io.github.mimoguz.customwindow;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Utils.Theme;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowStyler {

    public static void setMica(Stage stage, Scene scene, Parent root) {
        root.getStyleClass().add("transparent-background");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.UNIFIED);

        try {
            WindowHandle handle = WindowHandle.tryFind(stage);
            if (!handle.dwmSetIntValue(
                    DwmAttribute.DWMWA_SYSTEMBACKDROP_TYPE,
                    DwmAttribute.DWMSBT_TABBEDWINDOW.value
            )) {
                handle.dwmSetBooleanValue(DwmAttribute.DWMWA_MICA_EFFECT, true);
            }
        } catch (HwndLookupException e) {
        }
    }

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
