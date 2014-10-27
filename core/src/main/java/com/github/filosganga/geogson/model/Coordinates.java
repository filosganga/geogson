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

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Coordinates implements Serializable {

    private final double lon;
    private final double lat;

    private Coordinates(double lon, double lat) {

        checkArgument(abs(lon) <= 180, "lon is out of range -180:180: " + lon);
        checkArgument(abs(lat) <= 90, "lat is out of range -90:90: " + lat);

        this.lon = lon;
        this.lat = lat;
    }

    public static Coordinates of(double lon, double lat) {
        return new Coordinates(lon, lat);
    }

    public double getLon() {
        return lon;
    }

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
