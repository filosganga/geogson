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

package com.github.filosganga.geogson.model.positions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@link Positions} instance for a single point.
 */
public class SinglePosition extends AbstractPositions<Positions> {

    private static final long serialVersionUID = 2L;

    private static final List<Positions> EMPTY_CHILDREN = new ArrayList<>();

    private final double lon;
    private final double lat;
    private final double alt;

    private transient Integer cachedHashCode = null;

    public SinglePosition(double lon, double lat, double alt) {
        super(EMPTY_CHILDREN);
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
    }

    public double lon() {
        return lon;
    }

    public double lat() {
        return lat;
    }

    public double alt() {
        return alt;
    }


    /**
     * Merge this SinglePosition with another {@link Positions} instance. If the given {@link Positions} is:
     *  - a SinglePosition, it returns a {@link LinearPositions} composed by this and the given positions, in order.
     *  - any other {@link Positions}, it delegates to the given {@link Positions} merge.
     *
     * @param other Positions instance to merge with.
     *
     * @return Positions instance result of merge.
     */
    @Override
    public Positions merge(Positions other) {

        if (other instanceof SinglePosition) {
            SinglePosition that = (SinglePosition) other;
            return LinearPositions.builder().addSinglePosition(this).addSinglePosition(that).build();
        } else {
            return other.merge(this);
        }
    }

    @Override
    public int hashCode() {
        if(cachedHashCode == null) {
            cachedHashCode = Objects.hash(SinglePosition.class, lon, lat, alt);
        }
        return cachedHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SinglePosition other = (SinglePosition) obj;
        return Objects.equals(this.lon, other.lon) &&
                Objects.equals(this.lat, other.lat) &&
                Objects.equals(this.alt, other.alt);
    }

    @Override
    public String toString() {
        return "SinglePosition{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", alt=" + alt +
                '}';
    }
}
