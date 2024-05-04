package it.univr.wordautomata.alerts;

import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Alerts {
    public static boolean showConfirmationDialog(Scene scene, String title, String body) {

        return ButtonType.YES.equals(
                createAlert(Alert.AlertType.CONFIRMATION,
                        scene, title, null, body,
                        ButtonType.YES, ButtonType.NO)
                        .showAndWait()
                        .orElse(null));
    }

    public static void showInformationDialog(Scene scene, String title, String body) {
        showInformationDialog(scene, title, null, body);
    }

    public static void showInformationDialog(Scene scene, String title, String header, String body) {
        createAlert(Alert.AlertType.INFORMATION, scene, title, header, body, ButtonType.OK).showAndWait();
    }

    public static void showErrorDialog(Scene scene, String title, String body) {
        createAlert(Alert.AlertType.ERROR, scene, title, null, body, ButtonType.OK).showAndWait();
    }

    private static Alert createAlert(Alert.AlertType type, Scene scene, String title, String header, String body,
            ButtonType... buttons) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        
        Parent alertDialog = alert.getDialogPane();
        Scene alertScene = alertDialog.getScene();
        Stage alertStage = (Stage) alertScene.getWindow();

        WindowStyler.setTheme(alertStage);
        alertDialog.getStylesheets().addAll(scene.getRoot().getStylesheets());
        alert.initOwner(scene.getWindow());
        alert.getButtonTypes().setAll(buttons);

        return alert;
    }
}
