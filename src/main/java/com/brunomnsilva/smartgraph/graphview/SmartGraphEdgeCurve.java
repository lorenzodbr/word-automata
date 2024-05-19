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
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Concrete implementation of a curved edge.
 * <br>
 * The edge binds its start point to the <code>outbound</code>
 * {@link SmartGraphVertexNode} center and its end point to the
 * <code>inbound</code> {@link SmartGraphVertexNode} center. As such, the curve
 * is updated automatically as the vertices move.
 * <br>
 * Given there can be several curved edges connecting two vertices, when calling
 * the constructor {@link #SmartGraphEdgeCurve(Edge,
 * SmartGraphVertexNode,
 * SmartGraphVertexNode, int) } the <code>edgeIndex</code>
 * can be specified as to create non-overlapping curves.
 *
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 *
 * @author brunomnsilva
 */
public class SmartGraphEdgeCurve<E, V> extends CubicCurve implements SmartGraphEdgeBase<E, V> {

    private static final double MAX_EDGE_CURVE_ANGLE = 50;
    private static final double MIN_EDGE_CURVE_ANGLE = 5;

    /** Distance (in pixels) that establishes the maximum curve threshold */
    public static final int DISTANCE_THRESHOLD = 400;

    /** Radius applied to loop curves */
    public static final double LOOP_RADIUS_FACTOR = 3;

    public static final int SELF_LOOP_FACTOR = 12;
    public static final int LABEL_Y_SHIFT = 5;

    private final Edge<E, V> underlyingEdge;

    private final SmartGraphVertexNode<V> inbound;
    private final SmartGraphVertexNode<V> outbound;

    private SmartLabel attachedLabel = null;
    private SmartArrow attachedArrow = null;

    private double randomAngleFactor;

    /* Styling proxy */
    private final SmartStyleProxy styleProxy;

    private final int edgeIndex;

    private final ContextMenu contextMenu;

    /**
     * Constructs a SmartGraphEdgeCurve representing a curved edge between two
     * SmartGraphVertexNodes.
     *
     * @param edge     the edge associated with this curve
     * @param inbound  the inbound SmartGraphVertexNode
     * @param outbound the outbound SmartGraphVertexNode
     */
    public SmartGraphEdgeCurve(Edge<E, V> edge, SmartGraphVertexNode<V> inbound, SmartGraphVertexNode<V> outbound) {
        this(edge, inbound, outbound, 0);
    }

    /**
     * Constructs a SmartGraphEdgeCurve representing an edge curve between two
     * SmartGraphVertexNodes.
     *
     * @param edge      the edge associated with this curve
     * @param inbound   the inbound SmartGraphVertexNode
     * @param outbound  the outbound SmartGraphVertexNode
     * @param edgeIndex the edge index (>=0)
     */
    public SmartGraphEdgeCurve(Edge<E, V> edge, SmartGraphVertexNode<V> inbound, SmartGraphVertexNode<V> outbound,
            int edgeIndex) {
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

        this.edgeIndex = edgeIndex;

        // TODO: improve this solution taking into account even indices, etc.
        randomAngleFactor = edgeIndex == 0 ? 0 : edgeIndex == 2 ? 1.0 : 1.0 / edgeIndex;

        // update();
        enableListeners();

        propagateHoverEffectToArrow();

        toBack();

        contextMenu = Methods.buildContextMenu(this);

        setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });
    }

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

    private void update() {
        if (inbound == outbound) {
            /* Make a loop using the control points proportional to the vertex radius */

            // TODO: take into account several "self-loops" with randomAngleFactor
            int isSelfLoop = (inbound == outbound ? 1 : 0);

            double radius = inbound.getRadius() + isSelfLoop * SELF_LOOP_FACTOR * edgeIndex;

            double midpointX1 = inbound.getCenterX() + radius * LOOP_RADIUS_FACTOR;
            double midpointY1 = inbound.getCenterY() - radius * LOOP_RADIUS_FACTOR;
            double midpointX2 = inbound.getCenterX() - radius * LOOP_RADIUS_FACTOR;
            double midpointY2 = inbound.getCenterY() - radius * LOOP_RADIUS_FACTOR;

            setControlX1(midpointX1);
            setControlY1(midpointY1);
            setControlX2(midpointX2);
            setControlY2(midpointY2);

        } else {
            /*
             * Make a curved edge. The curvature is bounded and proportional to the
             * distance;
             * higher curvature for closer vertices
             */

            Point2D startpoint = new Point2D(inbound.getCenterX(), inbound.getCenterY());
            Point2D endpoint = new Point2D(outbound.getCenterX(), outbound.getCenterY());

            double distance = startpoint.distance(endpoint);

            double angle = linearDecay(MAX_EDGE_CURVE_ANGLE, MIN_EDGE_CURVE_ANGLE, distance, DISTANCE_THRESHOLD);

            Point2D midpoint = UtilitiesPoint2D.calculateTriangleBetween(startpoint, endpoint,
                    (edgeIndex % 2 == 0 && edgeIndex != 0 ? -1 : 1) * ((-angle) + randomAngleFactor * 2 * angle));

            setControlX1(midpoint.getX());
            setControlY1(midpoint.getY());
            setControlX2(midpoint.getX());
            setControlY2(midpoint.getY());
        }
    }

    /**
     * Provides the decreasing linear function decay.
     * 
     * @param initialValue      initial value
     * @param finalValue        maximum value
     * @param distance          current distance
     * @param distanceThreshold distance threshold (maximum distance -> maximum
     *                          value)
     * @return the decay function value for <code>distance</code>
     */
    private static double linearDecay(double initialValue, double finalValue, double distance,
            double distanceThreshold) {
        // Args.requireNonNegative(distance, "distance");
        // Args.requireNonNegative(distanceThreshold, "distanceThreshold");
        // Parameters are internally guaranteed to be positive. We avoid two method
        // calls.

        if (distance >= distanceThreshold)
            return finalValue;

        return initialValue + (finalValue - initialValue) * distance / distanceThreshold;
    }

    private void enableListeners() {
        // With a curved edge we need to continuously update the control points.
        // TODO: Maybe we can achieve this solely with bindings? Maybe there's no
        // performance gain in doing so.

        this.startXProperty().addListener((ov, oldValue, newValue) -> update());
        this.startYProperty().addListener((ov, oldValue, newValue) -> update());
        this.endXProperty().addListener((ov, oldValue, newValue) -> update());
        this.endYProperty().addListener((ov, oldValue, newValue) -> update());
    }

    @Override
    public void attachLabel(SmartLabel label) {
        this.attachedLabel = label;
        this.attachedLabel.setMouseTransparent(true);

        label.xProperty().bind(controlX1Property().add(controlX2Property()).divide(2)
                .subtract(Bindings.divide(label.layoutWidthProperty(), 2)));
        label.yProperty()
                .bind(controlY1Property().add(controlY2Property()).divide(2)
                        .add(Bindings.divide(label.layoutHeightProperty(), 2))
                        .add(outbound == inbound ? LABEL_Y_SHIFT * (edgeIndex + 2) : 0));

        update();
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
                UtilitiesBindings.atan2(endYProperty().subtract(controlY2Property()),
                        endXProperty().subtract(controlX2Property()))));

        arrow.getTransforms().add(rotation);

        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(0, outbound == inbound ? -3 / (edgeIndex + 1) : 0);

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
        if (outbound == inbound) {
            return Constants.Orientation.WEST;
        }

        int endX = (int) endXProperty().get();
        int startX = (int) startXProperty().get();
        int endY = (int) endYProperty().get();
        int startY = (int) startYProperty().get();

        if (Math.abs(startY - endY) < 10) {
            if (startX < endX) {
                return Constants.Orientation.EAST;
            } else {
                return Constants.Orientation.WEST;
            }
        }

        if (Math.abs(startX - endX) < 10) {
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