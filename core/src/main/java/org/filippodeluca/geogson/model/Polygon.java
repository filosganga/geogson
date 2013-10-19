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

import static java.util.Arrays.asList;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Polygon extends Geometry {

    public static class Builder {

        private ImmutableList.Builder<LinearPositions> coordinates;

        public Builder(Iterable<Position> perimeter) {
            this.coordinates = ImmutableList.<LinearPositions>builder().add(
                    new LinearPositions(ImmutableList.copyOf(perimeter))
            );
        }

        public Builder withHole(Iterable<Position> hole) {
            coordinates.add(new LinearPositions(ImmutableList.copyOf(hole)));
            return this;
        }

        public Polygon build() {
            return new Polygon(this);
        }
    }

    private final AreaPositions coordinates;

    public Polygon(Builder builder) {
        this(new AreaPositions(
                builder.coordinates.build()
        ));
    }

    public Polygon(AreaPositions coordinates) {
        this.coordinates = coordinates;
    }

    public static Polygon of(Position...perimeter) {
        return of(asList(perimeter));
    }

    public static Polygon of(Iterable<Position> perimeter) {
        return new Builder(perimeter).build();
    }

    public static Polygon of(Iterable<Position> perimeter, Iterable<Position>...holes) {
        Builder builder = new Builder(perimeter);
        for(Iterable<Position> hole : holes) {
            builder.withHole(hole);
        }
        return builder.build();
    }

    @Override
    public Type getType() {
        return Type.POLYGON;
    }

    @Override
    public AreaPositions getPositions() {
        return coordinates;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Polygon other = (Polygon) obj;
        return Objects.equal(this.coordinates, other.coordinates);
    }
}
