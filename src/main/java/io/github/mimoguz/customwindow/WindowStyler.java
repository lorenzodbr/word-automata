package io.github.mimoguz.customwindow;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowStyler {

    public static void setMica(Stage stage, Scene scene, Parent root) {
        root.setStyle("-fx-background-color: transparent");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.UNIFIED);

        Platform.runLater(() -> {
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
        });
    }

    public static void setDarkMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        
        Platform.runLater(() -> {
            try {
                WindowHandle handle = WindowHandle.tryFind(stage);
                handle.dwmSetBooleanValue(DwmAttribute.DWMWA_USE_IMMERSIVE_DARK_MODE, true);
            } catch (HwndLookupException e) {
            }
        });
    }
    
    public static void setLightMode(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }
}