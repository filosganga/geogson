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

import com.google.common.base.Function;
import org.filippodeluca.geogson.model.positions.SinglePosition;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Point extends AbstractGeometry<SinglePosition> {

    public Point(SinglePosition positions) {
        super(positions);
    }

    public static Point from(double lon, double lat) {
        return from(Coordinates.of(lon, lat));
    }

    public static Point from(Coordinates coordinates) {
        return new Point(new SinglePosition(coordinates));
    }

    public static Function<Point, Coordinates> coordinatesFn() {
        return CoordinatesFn.INSTANCE;
    }

    public Coordinates coordinates() {
        return positions().coordinates();
    }

    public double lon() {
        return coordinates().getLon();
    }

    public double lat() {
        return coordinates().getLat();
    }

    public Point withLon(double lon) {
        return from(lon, coordinates().getLat());
    }

    public Point withLat(double lat) {
        return from(coordinates().getLon(), lat);
    }

    @Override
    public Type type() {
        return Type.POINT;
    }

    private static enum CoordinatesFn implements Function<Point, Coordinates> {
        INSTANCE;

        @Override
        public Coordinates apply(Point input) {
            return input.coordinates();
        }
    }

}
