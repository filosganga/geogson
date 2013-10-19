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

package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import org.filippodeluca.geogson.model.positions.SinglePosition;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Point implements Geometry, Serializable {

    private final SinglePosition positions;

    public Point(SinglePosition positions) {

        checkArgument(positions != null, "coordinate must be not null");

        this.positions = positions;
    }

    public static Point from(double lon, double lat) {
        return from(Coordinates.of(lon, lat));
    }

    public static Point from(Coordinates coordinates) {
        return new Point(new SinglePosition(coordinates));
    }

    public static Function<Point, SinglePosition> getPositionsFn() {
        return GetPositionFn.INSTANCE;
    }

    public static Function<Point, Coordinates> getCoordinatesFn() {
        return GetCoordinatesFn.INSTANCE;
    }

    public Coordinates getCoordinates() {
        return positions.coordinates();
    }

    public double getLon() {
        return getCoordinates().getLon();
    }

    public double getLat() {
        return getCoordinates().getLat();
    }

    public Point withLon(double lon) {
        return from(lon, getCoordinates().getLat());
    }

    public Point withLat(double lat) {
        return from(getCoordinates().getLon(), lat);
    }

    @Override
    public Type type() {
        return Type.POINT;
    }

    @Override
    public SinglePosition positions() {
        return positions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        return Objects.equal(this.positions, other.positions);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("positions", positions).toString();
    }

    private static enum GetPositionFn implements Function<Point, SinglePosition> {
        INSTANCE;

        @Override
        public SinglePosition apply(Point input) {
            return input.positions;
        }
    }

    private static enum GetCoordinatesFn implements Function<Point, Coordinates> {
        INSTANCE;

        @Override
        public Coordinates apply(Point input) {
            return input.getCoordinates();
        }
    }

}
