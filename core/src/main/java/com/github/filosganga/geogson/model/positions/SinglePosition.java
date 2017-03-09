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
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * A {@link Positions} instance for a single {@link Coordinates}.
 */
public class SinglePosition extends AbstractPositions<Positions> {

    private static final long serialVersionUID = 1L;

    private static final ImmutableList<Positions> CHILDREN = ImmutableList.of();

    private final Coordinates coordinates;

    public SinglePosition(Coordinates coordinates) {
        super(CHILDREN);

        this.coordinates = coordinates;
    }

    /**
     * Guava Function to extract the {@link Coordinates} from a SinglePosition.
     *
     * @return The {@link CoordinatesFn} instance.
     */
    public static Function<SinglePosition, Coordinates> coordinatesFn() {
        return CoordinatesFn.INSTANCE;
    }

    /**
     * Return the underlying {@link Coordinates} instance.
     *
     * @return Coordinates
     */
    public Coordinates coordinates() {
        return coordinates;
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
            return new LinearPositions(ImmutableList.of(this, that));
        } else {
            return other.merge(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(SinglePosition.class, coordinates);
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
        return Objects.equal(this.coordinates, other.coordinates);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("coordinates", coordinates)
                .toString();
    }

    private enum CoordinatesFn implements Function<SinglePosition, Coordinates> {
        INSTANCE;

        @Override
        public Coordinates apply(SinglePosition input) {
            return input.coordinates();
        }
    }
}
