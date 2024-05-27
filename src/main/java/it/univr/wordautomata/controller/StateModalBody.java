package it.univr.wordautomata.controller;

import java.util.ArrayList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import atlantafx.base.controls.Popover;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The body of the dialog that shows the details of a state.
 */
public class StateModalBody extends GridPane {

    @FXML
    private TextField stateLabelTextField;

    @FXML
    private CheckBox markAsFinalCheckbox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button setAsInitialStateButton;

    @FXML
    private Button addTransitionButton;

    @FXML
    private VBox outboundTransitions;

    @FXML
    private ScrollPane outboundTransitionsScrollPane;

    private Popover popover;

    private SmartGraphVertex<State> vertex;
    private ModalBox dialog;
    private Components components;

    private Model model;

    /**
     * Creates a new state modal body.
     *
     * @param dialog the dialog
     * @param vertex the vertex to show
     */
    public StateModalBody(ModalBox dialog, SmartGraphVertex<State> vertex) {
        Methods.loadAndSetController(Constants.STATE_MODAL_BODY_FXML_FILENAME, this);

        this.vertex = vertex;
        this.dialog = dialog;
        this.components = Components.getInstance();
        this.model = Model.getInstance();

        setFields();

        prefHeightProperty().bind(Components.getInstance().getGraphPanel().heightProperty());
    }

    /**
     * Sets the fields of the dialog.
     */
    private void setFields() {
        Text body = new Text("Label must be unique and not blank.");
        body.setTextAlignment(javafx.scene.text.TextAlignment.JUSTIFY);
        this.popover = new Popover(body);

        Vertex<State> underlyingVertex = vertex.getUnderlyingVertex();
        State state = underlyingVertex.element();

        stateLabelTextField.setText(state.toString());
        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean invalid = newValue == null || newValue.isBlank() || Methods.existsState(newValue);

            if (!invalid) {
                popover.hide();
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                model.setSaved(false);
                state.setLabel(stateLabelTextField.getText());
                components.getGraphPanel().update();
            } else {
                popover.show(stateLabelTextField);
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        markAsFinalCheckbox.setSelected(state.isFinal());
        markAsFinalCheckbox.selectedProperty()
                .addListener((observable, oldValue, newValue) -> state.setFinal(newValue));
        markAsFinalCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertex.addStyleClass(Constants.FINAL_STATE_CLASS);
            } else {
                vertex.removeStyleClass(Constants.FINAL_STATE_CLASS);
            }

            model.setSaved(false);
            components.getBottomBar().computePath();
        });

        deleteButton.setOnAction(e -> {
            if (components.getGraphPanel().queryRemoveVertex(underlyingVertex)) {
                dialog.close();
            }

            requestFocus();
        });

        addTransitionButton.setOnAction(e -> {
            components.getGraphPanel().addEdge(state);
            requestFocus();
        });

        if (Model.getInstance().getInitialState() != state) {
            setAsInitialStateButton.setOnAction(e -> {
                components.getGraphPanel().setInitialState(state);
                components.getBottomBar().computePath();
                requestFocus();
                setAsInitialStateButton.setDisable(true);
            });
        } else {
            setAsInitialStateButton.setDisable(true);
        }

        List<Edge<Transition, State>> outboundEdges = new ArrayList<>(
                Model.getInstance().getGraph().outboundEdges(underlyingVertex));

        if (!outboundEdges.isEmpty()) {
            outboundTransitions.getChildren().clear();
            outboundTransitions.setAlignment(javafx.geometry.Pos.TOP_LEFT);

            HBox header = createTransitionRow("Label", "Ending State");
            header.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_BOLD);

            outboundTransitions.getChildren().add(header);
            outboundTransitions.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }

        for (Edge<Transition, State> edge : outboundEdges) {
            Transition transition = edge.element();
            State target = edge.vertices()[1].element();
            outboundTransitions.getChildren().add(createTransitionRow(transition.toString(), target.toString()));
        }
    }

    /**
     * Creates a row for a transition.
     * 
     * @param left  the left text
     * @param right the right text
     * @return the text field
     */
    private HBox createTransitionRow(String left, String right) {
        HBox row = new HBox();
        row.setSpacing(5);

        Pane separator = new Pane();
        HBox.setHgrow(separator, Priority.ALWAYS);

        Label leftLabel = new Label(left);
        leftLabel.setMinWidth(Constants.TRANSITION_ROW_MIN_WIDTH);

        Label rightLabel = new Label(right);
        rightLabel.setMinWidth(Constants.TRANSITION_ROW_MIN_WIDTH);
        rightLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        row.getChildren().addAll(leftLabel, separator, rightLabel);

        return row;
    }
}
