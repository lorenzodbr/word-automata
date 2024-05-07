package it.univr.wordautomata;

import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.controller.Components;
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
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main wrapper class
 */
public class WordAutomata extends Application {

    private Stage stage;
    private Scene scene;

    private Model model;
    private Components controllers;

    @Override
    public void start(Stage stage) throws IOException {
        this.model = Model.getInstance();
        this.controllers = Components.getInstance();

        controllers.setStage((this.stage = stage));
        initComponents();
        stage.show();
    }

    private void initComponents() throws IOException {
        initTheme();
        initScene();
        initStage();
    }

    private void initScene() throws IOException {
        Methods.loadFonts(Constants.FONT_REGULAR_FILENAME, Constants.FONT_BOLD_FILENAME,
                Constants.FONT_ITALIC_FILENAME);

        MainPanel mainPanel = new MainPanel(this);
        controllers.setMainPanel(mainPanel);
        controllers.setScene((this.scene = new Scene(mainPanel)));
    }

    private void initStage() {
        stage.setMinHeight(Constants.MIN_HEIGHT);
        stage.setMinWidth(Constants.MIN_WIDTH);
        stage.setHeight(Constants.HEIGHT);
        stage.setWidth(Constants.WIDTH);
        stage.titleProperty().bind(Bindings.concat(
                Constants.TITLE,
                Bindings.when(model.openedFileProperty().isNull())
                        .then("")
                        .otherwise(Bindings.concat(" - ", model.openedFileProperty().asString())),
                Bindings.when(model.savedProperty())
                        .then("")
                        .otherwise("*")));
        Locale.setDefault(Locale.ENGLISH);
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
        if (!model.isSaved()) {
            String message = "Do you want to save the current automata";
            if (model.getOpenedFile() != null)
                message += " at " + model.getOpenedFile().getAbsolutePath();
            message += " before exiting?";

            if (Alerts.showConfirmationDialog(scene, "Exit", message)) {
                Methods.save();

                if (!model.isSaved())
                    return;
            }
        } else if (!Alerts.showConfirmationDialog(scene, "Exit", "Do you really want to exit the application?")) {
            return;
        }

        Platform.exit();
    }
}
