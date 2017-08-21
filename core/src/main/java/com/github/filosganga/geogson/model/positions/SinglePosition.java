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

import com.github.filosganga.geogson.model.Coordinates;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Positions} instance for a single {@link Coordinates}.
 */
public class SinglePosition extends AbstractPositions<Positions> {

    private static final long serialVersionUID = 2L;

    private static final Iterable<Positions> CHILDREN = new ArrayList<>();

    private final double lon;
    private final double lat;
    private final double alt;

    private Optional<Integer> cachedHashCode = Optional.empty();

    public SinglePosition(double lon, double lat, double alt) {
        super(CHILDREN);
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
    }


    /**
     * Return the underlying {@link Coordinates} instance.
     *
     * @return Coordinates
     */
    public Coordinates coordinates() {
        return Coordinates.of(lon, lat, alt);
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
        if(!cachedHashCode.isPresent()) {
            cachedHashCode = Optional.of(Objects.hash(SinglePosition.class, lon, lat, alt));
        }
        return cachedHashCode.get();
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
