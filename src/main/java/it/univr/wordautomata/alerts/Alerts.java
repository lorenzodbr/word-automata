package it.univr.wordautomata.alerts;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import it.univr.wordautomata.Main;
import it.univr.wordautomata.stylings.WindowStyler;
import it.univr.wordautomata.utils.Constants;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
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

    public static void showErrorDialog(Scene scene, String title, String body, boolean showAndWait) {
        showErrorDialog(scene, title, null, body, showAndWait);
    }

    public static void showErrorDialog(Scene scene, String title, String header, String body, boolean showAndWait) {
        Alert alert = createAlert(Alert.AlertType.ERROR, scene, title, header, body, ButtonType.OK);

        if (showAndWait) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    public static Alert createAlert(Alert.AlertType type, Scene scene, String title, String header, String body,
            ButtonType... buttons) {
        Alert alert = new Alert(type);

        if (type == null) {
            alert.setGraphic(new ImageView(new Image(Main.class
                    .getResourceAsStream(
                            Constants.ICON_BASE_FOLDER + Constants.ICON_FILENAME + Constants.ICON_EXTENSION),
                    50, 50, true, true)));
        }

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);

        DialogPane alertDialog = alert.getDialogPane();
        Scene alertScene = alertDialog.getScene();
        Stage alertStage = (Stage) alertScene.getWindow();

        WindowStyler.setTheme(alertStage);
        if (scene != null) {
            alertDialog.getStylesheets().addAll(scene.getRoot().getStylesheets());
            alert.initOwner(scene.getWindow());
        }
        alert.getButtonTypes().setAll(buttons);
        alertDialog.setMinHeight(Region.USE_PREF_SIZE);

        return alert;
    }
}
