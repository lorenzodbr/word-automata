package it.univr.wordautomata.alerts;

import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Alerts {
    public static boolean showConfirmationDialog(Scene scene, String title, String body) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(body);
        WindowStyler.setTheme((Stage) alert.getDialogPane().getScene().getWindow());
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        alert.initOwner(scene.getWindow());

        return ButtonType.YES.equals(alert.showAndWait().orElse(null));
    }

    public static void showInformationDialog(Scene scene, String title, String body) {
        showInformationDialog(scene, title, null, body);
    }
    
    public static void showInformationDialog(Scene scene, String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        WindowStyler.setTheme((Stage) alert.getDialogPane().getScene().getWindow());
        alert.initOwner(scene.getWindow());
        alert.showAndWait();
    }
}
