package it.univr.wordautomata.utils;

import it.univr.wordautomata.Main;
import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
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

    public static ContextMenu buildContextMenu(Node node) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem detailsItem = new MenuItem("Details", new FontIcon(BoxiconsRegular.INFO_CIRCLE));
        MenuItem deleteItem = new MenuItem("Delete", new FontIcon(BoxiconsRegular.TRASH));
        deleteItem.getStyleClass().add(Constants.MENU_ITEM_DANGER_CLASS);

        List<MenuItem> items = new ArrayList<>();

        if (node instanceof SmartGraphVertex nAsV) {
            MenuItem setAsInitialStateItem = new MenuItem("Set as initial state");
            MenuItem addTransitionMenuItem = new MenuItem("Add transition",
                    new FontIcon(BoxiconsRegular.LOG_IN_CIRCLE));

            items.add(detailsItem);
            items.add(new SeparatorMenuItem());
            items.add(addTransitionMenuItem);
            items.add(setAsInitialStateItem);
            items.add(new SeparatorMenuItem());
            items.add(deleteItem);

            detailsItem.setOnAction(
                    e -> {
                        contextMenu.hide();
                        Components.getInstance().getGraphPanel().showStateSideBar(nAsV);
                    });
            deleteItem.setOnAction(
                    e -> {
                        contextMenu.hide();
                        Components.getInstance().getGraphPanel().queryRemoveVertex(nAsV.getUnderlyingVertex());
                    });
            setAsInitialStateItem.setOnAction(
                    e -> {
                        contextMenu.hide();
                        Components components = Components.getInstance();
                        components.getGraphPanel().setInitialState((State) nAsV.getUnderlyingVertex().element());
                        components.getBottomBar().computePath();
                    });
            setAsInitialStateItem.disableProperty()
                    .bind(Model.getInstance().initialStateProperty().isEqualTo(nAsV.getUnderlyingVertex().element()));

            addTransitionMenuItem.setOnAction(
                    e -> Components.getInstance().getGraphPanel()
                            .addEdge((State) nAsV.getUnderlyingVertex().element()));
        } else if (node instanceof SmartGraphEdge nAsE) {
            items.add(detailsItem);
            items.add(new SeparatorMenuItem());
            items.add(deleteItem);

            detailsItem.setOnAction(
                    e -> Components.getInstance().getGraphPanel().showTransitionSideBar(nAsE));
            deleteItem.setOnAction(
                    e -> Components.getInstance().getGraphPanel().queryRemoveEdge(nAsE.getUnderlyingEdge()));
        } else if (node instanceof SmartGraphPanel) {
            Menu menu = new Menu("New");

            MenuItem addStateMenuItem = new MenuItem("State", new FontIcon(BoxiconsRegular.PLUS_CIRCLE));
            MenuItem addTransitionMenuItem = new MenuItem("Transition", new FontIcon(BoxiconsRegular.LOG_IN_CIRCLE));
            MenuItem clearGraphMenuItem = new MenuItem("Clear", new FontIcon(BoxiconsRegular.TRASH));

            clearGraphMenuItem.getStyleClass().add(Constants.MENU_ITEM_DANGER_CLASS);

            addStateMenuItem.setOnAction(
                    e -> Components.getInstance().getGraphPanel().addVertex());
            addTransitionMenuItem.setOnAction(
                    e -> Components.getInstance().getGraphPanel().addEdge());
            clearGraphMenuItem.setOnAction(
                    e -> Components.getInstance().getMainPanel().clearGraph());

            menu.getItems().addAll(addStateMenuItem, addTransitionMenuItem);
            items.add(menu);
            items.add(new SeparatorMenuItem());
            items.add(clearGraphMenuItem);
        }

        contextMenu.getItems().addAll(items);

        return contextMenu;
    }

    public static void save() {
        if (Model.getInstance().getOpenedFile() == null) {
            saveAs();
            return;
        }

        AutomataSaver.save();
    }

    public static void saveAs() {
        File file = AutomataSaver.showSaveDialog(Components.getInstance().getStage());

        if (file != null) {
            Model.getInstance().setOpenedFile(file);
            AutomataSaver.save(file);
        }
    }

    public static boolean existsState(String label) {
        return Model.getInstance().getGraph().objectsInVertices().contains(new State(label));
    }

    public static boolean existsTransitionFromVertex(String state, String transition) {
        return Model.getInstance().getGraph()
                .edges()
                .stream()
                .filter(e -> e.element().getLabel().equals(transition))
                .anyMatch(e -> {
                    return e.vertices()[0].element().getLabel().get().equals(state)
                            || e.vertices()[1].element().getLabel().get().equals(state);
                });
    }
}
