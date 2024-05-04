package it.univr.wordautomata;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.Controllers;
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
        this.stage = stage;
        
        initComponents();
        stage.show();
    }

    private void initComponents() throws IOException {
        initTheme();
        initScene();
        initStage();
    }

    private void initScene() throws IOException {
        Methods.loadFonts(Constants.FONT_REGULAR_FILENAME, Constants.FONT_BOLD_FILENAME, Constants.FONT_ITALIC_FILENAME);
        
        MainPanel mainPanel = new MainPanel(this);
        Controllers.getInstance().setMainPanel(mainPanel);
        scene = new Scene(mainPanel);
    }

    private void initStage() {
        stage.setMinHeight(Constants.MIN_HEIGHT);
        stage.setMinWidth(Constants.MIN_WIDTH);
        stage.setHeight(Constants.HEIGHT);
        stage.setWidth(Constants.WIDTH);
        
        Locale.setDefault(Locale.ENGLISH);
        stage.setTitle(Constants.TITLE);
        Methods.setIcon(stage);
        stage.setScene(scene);

        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> {
            e.consume();
            exit();
        });
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
            // AutomataSaver.save();
            Platform.exit();
        }
    }
}
