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
 * The main class of the application. It is responsible for starting the
 * application and initializing its base components.
 */
public class WordAutomata extends Application {

    private Stage stage;
    private Scene scene;

    private Model model;
    private Components components;

    @Override
    public void start(Stage stage) throws IOException {
        this.model = Model.getInstance();
        this.components = Components.getInstance();

        components.setStage((this.stage = stage));
        initComponents();
        stage.show();
    }

    /**
     * Initializes the components of the application.
     *
     * @throws IOException if an I/O error occurs
     */
    private void initComponents() throws IOException {
        initTheme();
        initScene();
        initStage();
    }

    /**
     * Initializes the scene of the application.
     *
     * @throws IOException if an I/O error occurs
     */
    private void initScene() throws IOException {
        Methods.loadFonts(Constants.FONT_REGULAR_FILENAME, Constants.FONT_BOLD_FILENAME,
                Constants.FONT_ITALIC_FILENAME);

        MainPanel mainPanel = new MainPanel(this);
        components.setMainPanel(mainPanel);
        components.setScene((this.scene = new Scene(mainPanel)));
        this.scene.getStylesheets()
                .add(Main.class
                        .getResource(Constants.STYLE_BASE_FOLDER + Constants.STYLE_FILENAME + Constants.STYLE_EXTENSION)
                        .toExternalForm());
    }

    /**
     * Initializes the stage of the application.
     */
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

    /**
     * Initializes the theme of the application.
     */
    private void initTheme() {
        setTheme(model.getTheme());
    }

    /**
     * Sets the theme of the application.
     *
     * @param theme the theme to set
     */
    private void setTheme(Theme theme) {
        switch (theme) {
            case DARK:
                WindowStyler.setDarkMode(stage);
                break;
            case LIGHT:
                WindowStyler.setLightMode(stage);
        }
    }

    /**
     * Toggles the theme of the application.
     */
    public void toggleTheme() {
        setTheme(model.cycleTheme());
    }

    /**
     * Launches the application.
     */
    public void run() {
        launch();
    }

    /**
     * Exits the application.
     */
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
