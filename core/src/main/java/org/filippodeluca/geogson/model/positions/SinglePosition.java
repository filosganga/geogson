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

package org.filippodeluca.geogson.model.positions;

import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.filippodeluca.geogson.model.Coordinates;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class SinglePosition implements Positions {

    private static final Iterable<Positions> CHILDREN = new ArrayList<Positions>();

    private final Coordinates coordinates;

    public SinglePosition(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public Positions merge(Positions other) {

        if(other instanceof SinglePosition) {
            SinglePosition that = (SinglePosition)other;
            return new LinearPositions(ImmutableList.of(new SinglePosition(coordinates), that));
        } else {
            return other.merge(this);
        }
    }

    @Override
    public Iterable<Positions> children() {
        return CHILDREN;
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
        return Objects.toStringHelper(this)
                .add("position", coordinates)
                .toString();
    }

    public static Function<SinglePosition, Coordinates> getCoordinatesFn() {
        return new Function<SinglePosition, Coordinates>() {
            @Override
            public Coordinates apply(SinglePosition input) {
                return input.getCoordinates();
            }
        };
    }
}
