package it.univr.wordautomata;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.components.MainPanel;
import it.univr.wordautomata.utils.Utils;
import static it.univr.wordautomata.utils.Utils.Theme.DARK;
import static it.univr.wordautomata.utils.Utils.Theme.LIGHT;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main wrapper class
 */
public class WordAutomata extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override

    public void start(Stage stage) throws IOException {
        init(stage);
        stage.show();
    }

    private void init(Stage stage) throws IOException {
        initScene(Utils.MAIN_PANEL_FXML_FILENAME);
        initStage(stage);
        initTheme();
    }

    private void initScene(String fxmlName) throws IOException {
        loadFonts(Utils.FONT_REGULAR_FILENAME, Utils.FONT_BOLD_FILENAME);
        scene = new Scene(new MainPanel());
    }

    private void initStage(Stage stage) {
        this.stage = stage;
        stage.setMinHeight(Utils.MIN_HEIGHT);
        stage.setMinWidth(Utils.MIN_WIDTH);
        stage.setHeight(Utils.HEIGHT);
        stage.setWidth(Utils.WIDTH);
        stage.setTitle(Utils.TITLE);
        stage.setScene(scene);
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

    public void show() {
        launch();
    }
}
