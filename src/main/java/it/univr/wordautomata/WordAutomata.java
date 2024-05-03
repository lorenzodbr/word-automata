package it.univr.wordautomata;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.MainPanel;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.stylings.WindowStyler;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.Theme;
import it.univr.wordautomata.utils.Methods;

import java.io.IOException;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main wrapper class
 */
public class WordAutomata extends Application {

    private Stage stage;
    private Scene scene;

    private Model model;

    @Override
    public void start(Stage stage) throws IOException {
        this.model = Model.getInstance();
        init(stage);
        stage.show();
    }

    private void init(Stage stage) throws IOException {
        initScene();
        initStage(stage);
        initTheme();
    }

    private void initScene() throws IOException {
        Methods.loadFonts(Constants.FONT_REGULAR_FILENAME, Constants.FONT_BOLD_FILENAME, Constants.FONT_ITALIC_FILENAME);
        scene = new Scene(new MainPanel(this));
    }

    private void initStage(Stage stage) {
        this.stage = stage;
        stage.setMinHeight(Constants.MIN_HEIGHT);
        stage.setMinWidth(Constants.MIN_WIDTH);
        stage.setHeight(Constants.HEIGHT);
        stage.setWidth(Constants.WIDTH);
        stage.setTitle(Constants.TITLE);
        stage.setScene(scene);
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> {
            e.consume();
            exit();
        });
        Locale.setDefault(Locale.ENGLISH);
    }

    private void initTheme() {
        setTheme(model.getTheme());
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

    public void toggleTheme() {
        setTheme(model.cycleTheme());
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
