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

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;

import static javafx.beans.binding.Bindings.createDoubleBinding;

/**
 * Some {@link Math} operations implemented as bindings.
 * 
 * @author brunomnsilva
 */
public class UtilitiesBindings {
    
    /**
     * Binding for {@link java.lang.Math#atan2(double, double)}
     *
     * @param   y   the ordinate coordinate
     * @param   x   the abscissa coordinate
     * @return  the <i>theta</i> component of the point
     *          (<i>r</i>,&nbsp;<i>theta</i>)
     *          in polar coordinates that corresponds to the point
     *          (<i>x</i>,&nbsp;<i>y</i>) in Cartesian coordinates.
     */
    public static DoubleBinding atan2(ObservableDoubleValue y, ObservableDoubleValue x) {
        return createDoubleBinding(() -> Math.atan2(y.get(), x.get()), y, x);
    }

    public static DoubleBinding abs(ObservableDoubleValue x) {
        return createDoubleBinding(() -> Math.abs(x.get()), x);
    }
    
    /**
     * Binding for {@link java.lang.Math#toDegrees(double)}
     *
     * @param   angRad   an angle, in radians
     * @return  the measurement of the angle {@code angRad}
     *          in degrees.
     */
    public static DoubleBinding toDegrees(ObservableDoubleValue angRad) {
        return createDoubleBinding(() -> Math.toDegrees(angRad.get()), angRad);
    }
}
