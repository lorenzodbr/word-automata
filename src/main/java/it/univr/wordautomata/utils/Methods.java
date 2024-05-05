package it.univr.wordautomata.utils;

import it.univr.wordautomata.Main;
import java.util.function.Consumer;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Methods used in the application
 */
public class Methods {

    public static void loadFonts(String... fileNames) {
        for (String fileName : fileNames) {
            Font.loadFont(Main.class
                    .getResourceAsStream(
                            Constants.FONTS_BASE_FOLDER
                                    + fileName
                                    + Constants.FONTS_EXTENSION),
                    Constants.DEFAULT_FONT_SIZE);
        }
    }

    public static void loadAndSetController(String path, Node n) {
        FXMLLoader loader = getLoader(path);
        loader.setRoot(n);
        loader.setController(n);

        try {
            loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while loading component's FXML (" + path + ")");
        }
    }

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource(Constants.FXML_BASE_FOLDER + fxml + Constants.FXML_EXTENSION));
        return fxmlLoader;
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().add(new Image(Main.class
                .getResourceAsStream(Constants.ICON_BASE_FOLDER + Constants.ICON_FILENAME + Constants.ICON_EXTENSION)));
    }

    public static ContextMenu buildContextMenu(Consumer<ActionEvent> onOpen, Consumer<ActionEvent> onDelete) {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem item1 = new MenuItem("Details", new FontIcon(BoxiconsRegular.INFO_CIRCLE));
        item1.setOnAction(e -> onOpen.accept(e));

        MenuItem item2 = new MenuItem("Delete", new FontIcon(BoxiconsRegular.TRASH));
        item2.setOnAction(e -> onDelete.accept(e));

        contextMenu.getItems().clear();
        contextMenu.getItems().addAll(item1, new SeparatorMenuItem(), item2);

        return contextMenu;
    }
}
