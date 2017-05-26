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
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * A couple of coordinates on the X and Y axis. If your map is in a geographic projection they will be Longitude and
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

    private final double alt;

    private Coordinates(double lon, double lat, double alt) {
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
    }

    /**
     * Create a new coordinate (a location on a map).
     *
     * @param lon
     *          longitude, x-axis coordinate, see {@link Coordinates#lon lon}
     * @param lat
     *          latitude, y-axis coordinate, see {@link Coordinates#lat lat}
     *
     * @return a Coordinates instances with the given longitude and latitude.
     */
    public static Coordinates of(double lon, double lat) {
        return new Coordinates(lon, lat, Double.NaN);
    }

    /**
     * Create a new coordinate (a location on a map).
     *
     * @param lon
     *          longitude, x-axis coordinate, see {@link Coordinates#lon lon}
     * @param lat
     *          latitude, y-axis coordinate, see {@link Coordinates#lat lat}
     * @param alt
     *          altitude, z-axis coordinate, see {@link Coordinates#alt alt}
     *
     * @return a Cordinates instances with the given longitude, latitude, and altitude.
     */
    public static Coordinates of(double lon, double lat, double alt) {
        return new Coordinates(lon, lat, alt);
    }

    /**
     * The x-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Longitude. Otherwise, it will be the x coordinate of the map location in
     * your map units.
     *
     * @return a double x-axis value.
     */
    public double getLon() {
        return lon;
    }

    /**
     * The y-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Latitude. Otherwise, it will be the y coordinate of the map location in
     * your map units.
     *
     * @return a double y-axis value.
     */
    public double getLat() {
        return lat;
    }

    /**
     * The z-axis coordinate in map units (degree, kilometer, meter, mile, foot
     * and inch). If your map is in a geographic projection, this will be the
     * Altitude. Otherwise, it will be the z coordinate of the map location in
     * your map units.
     *
     * @return a double z-axis value.
     */
    public double getAlt() {
        return alt;
    }

    public Coordinates withLon(double lon) {
        return Coordinates.of(lon, lat);
    }

    public Coordinates withLat(double lat) {
        return Coordinates.of(lon, lat);
    }

    public Coordinates withAlt(double alt) {
        return Coordinates.of(lon, lat, alt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Coordinates.class, lon, lat, alt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Coordinates other = (Coordinates) obj;
            return Objects.equals(this.lon, other.lon) &&
                    Objects.equals(this.lat, other.lat) &&
                    Objects.equals(this.alt, other.alt);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("lon", lon).add("lat", lat).add("alt", alt).toString();
    }
}
