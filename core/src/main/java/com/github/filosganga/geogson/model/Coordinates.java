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

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * A couple of coordinates on the X and Y axis. If your map is in a geographic projection they will be Longitute and
 * Latitude.
 *
 * eg:
 * {@code
 *     Coordinates cc = Coordinates.of(12, 34);
 *     cc1 = cc.withLon(9).withLat(13);
 * }
 *
 * TODO Consider rename long to x and lat to y.
 */
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double lon;

    private final double lat;

    private Coordinates(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Create a new coordinate (a location on a map).
     *
     * @param lon
     *          longitude, x-axis coordinate, see {@link Coordinates#lon lon}
     * @param lat
     *          latitude, y-axis coordinate, see {@link Coordinates#lat lat}
     */
    public static Coordinates of(double lon, double lat) {
        return new Coordinates(lon, lat);
    }

    /**
     * The x-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Longitude. Otherwise, it will be the x coordinate of the map location in
     * your map units.
     */
    public double getLon() {
        return lon;
    }

    /**
     * The y-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Latitude. Otherwise, it will be the y coordinate of the map location in
     * your map units.
     */
    public double getLat() {
        return lat;
    }

    public Coordinates withLon(double lon) {
        return Coordinates.of(lon, lat);
    }

    public Coordinates withLat(double lat) {
        return Coordinates.of(lon, lat);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Coordinates.class, lon, lat);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Coordinates other = (Coordinates) obj;
            return Objects.equal(this.lon, other.lon) && Objects.equal(this.lat, other.lat);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("lon", lon).add("lat", lat).toString();
    }
}
