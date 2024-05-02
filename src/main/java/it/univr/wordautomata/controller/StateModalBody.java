package it.univr.wordautomata.controller;

import atlantafx.base.theme.Styles;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

/**
 *
 */
public class StateModalBody extends GridPane {

    @FXML
    private TextField stateLabelTextField;

    @FXML
    private CheckBox markAsFinalCheckbox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addTransitionButton;

    @FXML
    private VBox outboundTransitions;

    private Consumer<String> onTextChange;
    private Consumer<Vertex<State>> onDeleteButtonClicked;

    private SmartGraphVertex<State> vertex;

    // private MainPanel mainPanel;

    public StateModalBody(SmartGraphVertex<State> vertex) {
        Utils.loadAndSetController(Utils.STATE_MODAL_BODY_FXML_FILENAME, this);

        this.vertex = vertex;
        // this.mainPanel = mainPanel;

        setFields();
    }

    private void setFields() {
        State state = vertex.getUnderlyingVertex().element();

        stateLabelTextField.setText(state.toString());
        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);

                state.setLabel(stateLabelTextField.getText());

                if (onTextChange != null) {
                    onTextChange.accept(newValue);
                }
            } else {
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        markAsFinalCheckbox.setSelected(state.isFinal().get());
        state.isFinal().bind(markAsFinalCheckbox.selectedProperty());
        state.isFinal().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertex.addStyleClass("final-state");
            } else {
                vertex.removeStyleClass("final-state");
            }
        });

        deleteButton.setOnAction(e -> {
            if (onDeleteButtonClicked != null) {
                onDeleteButtonClicked.accept(vertex.getUnderlyingVertex());
            }
        });

        // addTransitionButton.setOnAction(e -> mainPanel.getGraphPanel().addEdge());

        List<Edge<Transition, State>> outboundEdges = new ArrayList<>(
                Model.getInstance().getGraph().outboundEdges(vertex.getUnderlyingVertex()));

        if (!outboundEdges.isEmpty()) {
            outboundTransitions.getChildren().clear();
            outboundTransitions.setAlignment(javafx.geometry.Pos.TOP_LEFT);

            HBox header = createTransitionRow("Label", "Ending State");
            header.getStyleClass().addAll("text-muted", "text-bold");

            outboundTransitions.getChildren().add(header);
            outboundTransitions.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }

        for (Edge<Transition, State> edge : outboundEdges) {
            Transition transition = edge.element();
            State target = edge.vertices()[1].element();
            outboundTransitions.getChildren().add(createTransitionRow(transition.toString(), target.toString()));
        }
    }

    private HBox createTransitionRow(String left, String right) {
        HBox row = new HBox();
        row.setSpacing(5);

        Pane separator = new Pane();
        HBox.setHgrow(separator, Priority.ALWAYS);

        row.getChildren().addAll(new Label(left), separator, new Label(right));

        return row;
    }

    public void onTextChange(Consumer<String> onTextChange) {
        this.onTextChange = onTextChange;
    }

    public void onDeleteButtonClicked(Consumer<Vertex<State>> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }
}
