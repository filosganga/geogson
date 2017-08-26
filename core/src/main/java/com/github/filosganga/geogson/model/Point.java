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

/**
 * A Point is identified by a lon,lat and alt coordinate..
 * <p>
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#point.
 * <p>
 * eg: {@code
 * Point p1 = Point.from(1,2);
 * Point p2 = Point.from(1, 2, 3);
 * <p>
 * }
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
     * @return Point instance.
     */
    public static Point from(double lon, double lat) {
        return from(lon, lat, Double.NaN);
    }

    /**
     * Create a Point from the given coordinates.
     *
     * @param lon The x axis value. Longitude in a geographic projection.
     * @param lat The y axis value. Latitude in a geographic projection.
     * @param alt The z axis value. Altitude in a geographic projection.
     * @return Point instance.
     */
    public static Point from(double lon, double lat, double alt) {
        return new Point(new SinglePosition(lon, lat, alt));
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
        return positions().lon();
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
        return positions().lat();
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
        return positions().alt();
    }

    /**
     * Returns a new Point instance with the given x-axis value.
     *
     * @param lon The new longitude value.
     * @return A Point instance with this instance latitude and the given longitude
     */
    public Point withLon(double lon) {
        return from(lon, lat(), alt());
    }

    /**
     * Returns a new Point instance with the given x-axis value.
     *
     * @param lat The new latitude value.
     * @return A Point instance with this instance longitude and the given latitude.
     */
    public Point withLat(double lat) {
        return from(lon(), lat, alt());
    }

    /**
     * Returns a new Point instance with the given z-axis value.
     *
     * @param alt The new latitude value.
     * @return A Point instance with this instance longitude and the given latitude.
     */
    public Point withAlt(double alt) {
        return from(lon(), lat(), alt);
    }


    @Override
    public Type type() {
        return Type.POINT;
    }


}
