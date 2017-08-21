/*
 * Copyright 2013 Filippo De Luca - me@filippodeluca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;

import java.util.stream.StreamSupport;

/**
 * An abstract Geometry that is composed by a sequence of points.
 */
public abstract class LinearGeometry extends AbstractGeometry<LinearPositions> {

    private static final long serialVersionUID = 1L;

    LinearGeometry(LinearPositions positions) {
        super(positions);
    }


    /**
     * Converts to a MultiPoint.
     *
     * @return MultiPoint
     */
    public MultiPoint toMultiPoint() {
        return new MultiPoint(positions());
    }

    /**
     * Converts to a LineString.
     *
     * @return LineString
     */
    public LineString toLineString() {
        return new LineString(positions());
    }

    /**
     * Convert to a LinearRing.
     *
     * @return LinearRing
     */
    public LinearRing toLinearRing() {
        return new LinearRing(positions());
    }

    /**
     * Returns the points composing this Geometry.
     *
     * @return {@code Iterable<Point>} a Guava lazy Iterable.
     */
    public Iterable<Point> points() {
        return StreamSupport.stream(positions().children().spliterator(), false)
                .map(SinglePosition::coordinates)
                .map(Point::from)::iterator;

    }

}
