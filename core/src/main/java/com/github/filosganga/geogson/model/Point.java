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

import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Function;

/**
 * A Point is identified by a {@link Coordinates}.
 *
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#point.
 *
 * eg: {@code Point p = Point.from(1,2)}
 */
public class Point extends AbstractGeometry<SinglePosition> {

    private static final long serialVersionUID = 1L;

    public Point(SinglePosition positions) {
        super(positions);
    }

    /**
     * Create a Point from the given coordinates.
     *
     * @param lon The x axis value. Longitude in a geographic projection.
     * @param lat The y axis value. Latitude in a geographic projection.
     *
     * @return Point instance.
     */
    public static Point from(double lon, double lat) {
        return from(Coordinates.of(lon, lat));
    }

    /**
     * Create a Point from the given coordinates.
     *
     * @param lon The x axis value. Longitude in a geographic projection.
     * @param lat The y axis value. Latitude in a geographic projection.
     * @param alt The z axis value. Altitude in a geographic projection.
     *
     * @return Point instance.
     */
    public static Point from(double lon, double lat, double alt) {
        return from(Coordinates.of(lon, lat, alt));
    }

    /**
     * Create a Point from the given coordinates.
     *
     * @param coordinates The Coordinate instance.
     *
     * @return Point instance.
     */
    public static Point from(Coordinates coordinates) {
        return new Point(new SinglePosition(coordinates));
    }

    /**
     * The Guava function that extracts Coordinates instance from a Point.
     *
     * @return  {@link CoordinatesFn} function instance.
     */
    public static Function<Point, Coordinates> coordinatesFn() {
        return CoordinatesFn.INSTANCE;
    }

    /**
     * Returns the underlying {@link Coordinates} instance.
     *
     * @return The Coordinate instance represented by thi Point.
     */
    public Coordinates coordinates() {
        return positions().coordinates();
    }

    /**
     * The x-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Longitude. Otherwise, it will be the x coordinate of the map location in
     * your map units.
     *
     * @return the x-axis value of this point.
     */
    public double lon() {
        return coordinates().getLon();
    }

    /**
     * The y-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Latitude. Otherwise, it will be the y coordinate of the map location in
     * your map units.
     *
     * @return the y-axis value of this point.
     */
    public double lat() {
        return coordinates().getLat();
    }

    /**
     * The z-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Altitude. Otherwise, it will be the z coordinate of the map location in
     * your map units.
     *
     * @return the z-axis value of this point.
     */
    public double alt() {
        return coordinates().getAlt();
    }

    /**
     * Returns a new Point instance with the given x-axis value.
     *
     * @param lon The new longitude value.
     * @return A Point instance with this instance latitude and the given longitude
     */
    public Point withLon(double lon) {
        return from(lon, coordinates().getLat(), coordinates().getAlt());
    }

    /**
     * Returns a new Point instance with the given x-axis value.
     *
     * @param lat The new latitude value.
     * @return A Point instance with this instance longitude and the given latitude.
     */
    public Point withLat(double lat) {
        return from(coordinates().getLon(), lat, coordinates().getAlt());
    }

    /**
     * Returns a new Point instance with the given z-axis value.
     *
     * @param alt The new latitude value.
     * @return A Point instance with this instance longitude and the given latitude.
     */
    public Point withAlt(double alt) {
        return from(coordinates().getLon(), coordinates().getLat(), alt());
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
