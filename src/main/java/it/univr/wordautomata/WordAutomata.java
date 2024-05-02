package it.univr.wordautomata;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.MainPanel;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.stylings.WindowStyler;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        initScene();
        initStage(stage);
        initTheme();
    }

    private void initScene() throws IOException {
        Utils.loadFonts(Utils.FONT_REGULAR_FILENAME, Utils.FONT_BOLD_FILENAME, Utils.FONT_ITALIC_FILENAME);
        scene = new Scene(root = new MainPanel(this));
    }

    private void initStage(Stage stage) {
        this.stage = stage;
        stage.setMinHeight(Utils.MIN_HEIGHT);
        stage.setMinWidth(Utils.MIN_WIDTH);
        stage.setHeight(Utils.HEIGHT);
        stage.setWidth(Utils.WIDTH);
        stage.setTitle(Utils.TITLE);
        stage.setScene(scene);
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> {
            e.consume();
            exit();
        });
    }

    private void initTheme() {
        setTheme(Model.getInstance().getTheme());
    }
    
    private void setTheme(Theme theme) {
        switch (theme) {
            case DARK:
                WindowStyler.setDarkMode(stage);
                break;
            case LIGHT:
                WindowStyler.setLightMode(stage);
        }
    }

    public void toggleDarkTheme() {
        setTheme(Model.getInstance().cycleTheme());
    }

    public void run() {
        launch();
    }

    public void exit() {
        if (Alerts.showConfirmationDialog(scene, "Exit", "Do you really want to exit the application?")) {
            Platform.exit();
        }
    }
}
