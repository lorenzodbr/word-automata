package it.univr.wordautomata;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.components.MainPanel;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import static it.univr.wordautomata.utils.Utils.Theme.DARK;
import static it.univr.wordautomata.utils.Utils.Theme.LIGHT;
import java.io.IOException;
import javafx.application.Application;
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

    private Theme theme;

    @Override
    public void start(Stage stage) throws IOException {
        init(stage);
        stage.show();
    }

    private void init(Stage stage) throws IOException {
        theme = Theme.DEFAULT; //needed in advance for MainPanel;

        initScene(Utils.MAIN_PANEL_FXML_FILENAME);
        initStage(stage);
        initTheme();
    }

    private void initScene(String fxmlName) throws IOException {
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
    }

    private void initTheme() {
        if (theme == DARK && Utils.SET_MICA) {
            WindowStyler.setMica(stage, scene, root);
        }
        setTheme(theme);
    }

    public void toggleDarkTheme() {
        setTheme(theme = theme.next());
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

    public Theme getTheme() {
        return theme;
    }

    public void show() {
        launch();
    }
}
