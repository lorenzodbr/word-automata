package it.univr.wordautomata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.utils.Utils;
import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.scene.text.Font;

/**
 * JavaFX Main
 */
public class Main extends Application {
   
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        init(stage);
        stage.show();
    }

    private void init(Stage stage) throws IOException {
        initScene(Utils.FXML_FILENAME);
        initStage(stage);
        initTheme();
    }

    private void initScene(String fxmlName) throws IOException {
        loadFonts(Utils.FONT_REGULAR_FILENAME, Utils.FONT_BOLD_FILENAME);
        root = loadFXML(fxmlName);
        scene = new Scene(root);
    }

    private void initStage(Stage stage) {
        this.stage = stage;
        stage.setMinHeight(Utils.MIN_HEIGHT);
        stage.setMinWidth(Utils.MIN_WIDTH);
        stage.setScene(scene);
        stage.setTitle(Utils.TITLE);
    }

    private void initTheme() {
        switch (Utils.THEME) {
            case DARK:
                WindowStyler.setDarkMode(stage);

                if (Utils.SET_MICA) {
                    WindowStyler.setMica(stage, scene, root);
                }
                break;
            case LIGHT:
                WindowStyler.setLightMode(stage);
        }
    }

    private void loadFonts(String... fileNames) {
        for (String fileName : fileNames) {
            Font.loadFont(Main.class.getResourceAsStream(Utils.FONTS_BASE_FOLDER + fileName + Utils.FONTS_EXTENSION), Utils.DEFAULT_FONT_SIZE);
        }
    }

    private void setNameFromPackage(Stage stage) {
        String[] packageNameArray = getClass().getPackage().toString().split("[.]");
        if (packageNameArray.length > 0) {
            String packageName = packageNameArray[packageNameArray.length - 1];
            stage.setTitle(packageName.substring(0, 1).toUpperCase() + packageName.substring(1));
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Utils.FXML_BASE_FOLDER + fxml + Utils.FXML_EXTENSION));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
