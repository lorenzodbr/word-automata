/*
 * The MIT License
 *
 * JavaFXSmartGraph | Copyright 2019-2024  brunomnsilva@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.brunomnsilva.smartgraph.graphview;

import com.brunomnsilva.smartgraph.graph.Edge;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.Orientation;
import it.univr.wordautomata.utils.Methods;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Implementation of a straight line edge.
 *
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 *
 * @author brunomnsilva
 */
public class SmartGraphEdgeLine<E, V> extends Line implements SmartGraphEdgeBase<E, V> {

    private final Edge<E, V> underlyingEdge;

    private final SmartGraphVertexNode<V> inbound;
    private final SmartGraphVertexNode<V> outbound;

    private SmartLabel attachedLabel = null;
    private SmartArrow attachedArrow = null;

    /* Styling proxy */
    private final SmartStyleProxy styleProxy;

    private final ContextMenu contextMenu;

    /**
     * Constructs a SmartGraphEdgeLine representing an edge between two
     * SmartGraphVertexNodes.
     *
     * @param edge     the edge associated with this line
     * @param inbound  the inbound SmartGraphVertexNode
     * @param outbound the outbound SmartGraphVertexNode
     */
    public SmartGraphEdgeLine(Edge<E, V> edge, SmartGraphVertexNode<V> inbound, SmartGraphVertexNode<V> outbound) {
        if (inbound == null || outbound == null) {
            throw new IllegalArgumentException("Cannot connect null vertices.");
        }

        this.inbound = inbound;
        this.outbound = outbound;

        this.underlyingEdge = edge;

        styleProxy = new SmartStyleProxy(this);
        styleProxy.addStyleClass("edge");

        // bind start and end positions to vertices centers through properties
        this.startXProperty().bind(outbound.centerXProperty());
        this.startYProperty().bind(outbound.centerYProperty());
        this.endXProperty().bind(inbound.centerXProperty());
        this.endYProperty().bind(inbound.centerYProperty());

        propagateHoverEffectToArrow();

        contextMenu = Methods.buildContextMenu(this);

        setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });
    }

    @Override
    public void setStyleInline(String css) {
        styleProxy.setStyleInline(css);
        if (attachedArrow != null) {
            // attachedArrow.setStyleInline(css);
        }
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
        if (attachedArrow != null) {
            attachedArrow.setStyleClass(cssClass);
        }
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
        if (attachedArrow != null) {
            attachedArrow.addStyleClass(cssClass);
        }
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean result = styleProxy.removeStyleClass(cssClass);
        if (attachedArrow != null) {
            attachedArrow.removeStyleClass(cssClass);
        }
        return result;
    }

    @Override
    public void attachLabel(SmartLabel label) {
        this.attachedLabel = label;
        this.attachedLabel.setMouseTransparent(true);

        label.xProperty().bind(startXProperty().add(endXProperty()).divide(2)
                .subtract(Bindings.divide(label.layoutWidthProperty(), 2))
                .multiply(Constants.RANDOM.nextDouble(1, 1.02)));
        label.yProperty().bind(
                startYProperty().add(endYProperty()).divide(2).add(Bindings.divide(label.layoutHeightProperty(), 1.5)));
    }

    @Override
    public SmartLabel getAttachedLabel() {
        return attachedLabel;
    }

    @Override
    public Edge<E, V> getUnderlyingEdge() {
        return underlyingEdge;
    }

    @Override
    public void attachArrow(SmartArrow arrow) {
        this.attachedArrow = arrow;

        /* attach arrow to line's endpoint */
        arrow.translateXProperty().bind(endXProperty());
        arrow.translateYProperty().bind(endYProperty());

        /* rotate arrow around itself based on this line's angle */
        Rotate rotation = new Rotate();
        rotation.pivotXProperty().bind(translateXProperty());
        rotation.pivotYProperty().bind(translateYProperty());
        rotation.angleProperty().bind(UtilitiesBindings.toDegrees(
                UtilitiesBindings.atan2(endYProperty().subtract(startYProperty()),
                        endXProperty().subtract(startXProperty()))));

        arrow.getTransforms().add(rotation);

        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(0, 0);
        t.xProperty().bind(inbound.radiusProperty().negate());

        arrow.getTransforms().add(t);
    }

    @Override
    public SmartArrow getAttachedArrow() {
        return this.attachedArrow;
    }

    @Override
    public SmartStylableNode getStylableArrow() {
        return this.attachedArrow;
    }

    @Override
    public SmartStylableNode getStylableLabel() {
        return this.attachedLabel;
    }

    private void propagateHoverEffectToArrow() {
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (attachedArrow != null && newValue) {

                attachedArrow.fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, MouseButton.NONE, 0, true,
                        true, true, true, true, true, true, true, true, true, null));

            } else if (attachedArrow != null) { // newValue is false, hover ended

                attachedArrow.fireEvent(new MouseEvent(MouseEvent.MOUSE_EXITED, 0, 0, 0, 0, MouseButton.NONE, 0, true,
                        true, true, true, true, true, true, true, true, true, null));

            }
        });
    }

    @Override
    public Orientation getOrientation() {
        int endX = (int) endXProperty().get();
        int startX = (int) startXProperty().get();
        int endY = (int) endYProperty().get();
        int startY = (int) startYProperty().get();

        if (startY == endY) {
            if (startX < endX) {
                return Constants.Orientation.EAST;
            } else {
                return Constants.Orientation.WEST;
            }
        }

        if (startX == endX) {
            if (startY < endY) {
                return Constants.Orientation.SOUTH;
            } else {
                return Constants.Orientation.NORTH;
            }
        }

        if (endX > startX && endY < startY) {
            return Constants.Orientation.NORTH_EAST;
        } else if (endX > startX && endY > startY) {
            return Constants.Orientation.SOUTH_EAST;
        } else if (endX < startX && endY > startY) {
            return Constants.Orientation.SOUTH_WEST;
        } else {
            return Constants.Orientation.NORTH_WEST;
        }
    }

    @Override
    public boolean hasStyleClass(String cssClass) {
        return styleProxy.hasStyleClass(cssClass);
    }
}
