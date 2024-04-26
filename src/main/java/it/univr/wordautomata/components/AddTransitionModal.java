package it.univr.wordautomata.components;

import com.brunomnsilva.smartgraph.graph.Vertex;
import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.TransitionWrapper;
import java.util.Collection;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 *
 */
public class AddTransitionModal extends Dialog<TransitionWrapper> {
    public AddTransitionModal(Scene scene, Collection<State> vertices) {
        setTitle("Add transition");
        AddTransitionModalBody body = new AddTransitionModalBody(vertices);

        getDialogPane().setContent(body);
        initOwner(scene.getWindow());
        setResultConverter(c -> {
            if (c == ButtonType.OK) {
                return body.buildTransitionWrapper();
            }

            return null;
        });
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());

        WindowStyler.setTheme(((MainPanel) scene.getRoot()).getTheme(), (Stage) getDialogPane().getScene().getWindow());

    }
}
