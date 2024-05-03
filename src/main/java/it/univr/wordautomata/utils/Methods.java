package it.univr.wordautomata.utils;

import it.univr.wordautomata.Main;
import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Font;

/**
 * Constants used in the application
 */
public class Methods {
    
    //Global methods
    public static void loadFonts(String... fileNames) {
        for (String fileName : fileNames) {
            Font.loadFont(Main.class
                    .getResourceAsStream(
                            Constants.FONTS_BASE_FOLDER
                            + fileName
                            + Constants.FONTS_EXTENSION
                    ),
                    Constants.DEFAULT_FONT_SIZE
            );
        }
    }

    //FXML methods
    public static void loadAndSetController(String path, Node n) {
        FXMLLoader loader = getLoader(path);
        loader.setRoot(n);
        loader.setController(n);

        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while loading component's FXML (" + path + ")");
        }
    }

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Constants.FXML_BASE_FOLDER + fxml + Constants.FXML_EXTENSION));
        return fxmlLoader;
    }
}
