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

import java.util.LinkedList;
import java.util.List;

/**
 * It represent a collection of {@link AreaPositions} used to represent composed Are geometries like the
 * {@link com.github.filosganga.geogson.model.MultiPolygon}.
 */
public class MultiDimensionalPositions extends AbstractPositions<AreaPositions> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a MultiDimensionalPositions from a sequence of {@link AreaPositions}.
     *
     * @param children Iterable of AreaPositions.
     */
    private MultiDimensionalPositions(List<AreaPositions> children) {
        super(children);
    }

    public static MultiDimensionalPositions.Builder builder() {
        return new MultiDimensionalPositions.Builder();
    }

    public static MultiDimensionalPositions.Builder builder(MultiDimensionalPositions positions) {
        return builder().addAreaPositions(positions.children);
    }

    /**
     * Merge this Positions with another one. If the given {@link Positions} is:
     *  - SinglePosition, it will raise an IllegalArgumentException.
     *  - LinearPositions, it is a special case in which the given LinearPositions represent a
     *    {@link com.github.filosganga.geogson.model.Polygon} with no holes. This is probably not compliant with the
     *    GeoJson specifications, but it seems to happen quite frequently. It will be handled as the AreaPositions case.
     *  - AreaPositions, it will return a new MultiDimensionalPositions by appending the given AreaPositions to this.
     *  - Any other, it will raise an IllegalArgumentException
     *
     * @param other Positions instance to merge with.
     *
     * @return Positions results of merging.
     */
    @Override
    public Positions merge(Positions other) {
        if (other instanceof SinglePosition) {
            throw new IllegalArgumentException("Cannot merge single position and multidimensional positions");
        } else if (other instanceof LinearPositions) {
            // It can happen when a Polygon does not have holes and is represented by linear position see bug #19
            return merge(AreaPositions.builder().addLinearPosition((LinearPositions) other).build());

        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions.Builder().addAreaPositions(children).addAreaPosition(that).build();
        } else {
            throw new IllegalArgumentException("Cannot merge with: " + other);
        }
    }

    public static class Builder implements PositionsBuilder {

        private LinkedList<AreaPositions> areaPositions = new LinkedList<>();

        public MultiDimensionalPositions.Builder addAreaPosition(AreaPositions ap) {
            areaPositions.add(ap);
            return this;
        }

        public MultiDimensionalPositions.Builder addAreaPositions(Iterable<AreaPositions> aps) {
            aps.forEach(this::addAreaPosition);
            return this;
        }

        @Override
        public MultiDimensionalPositions.Builder addChild(Positions p) {
            if(p instanceof AreaPositions) {
                return addAreaPosition((AreaPositions) p);
            } else if (p instanceof LinearPositions) {
                return addAreaPosition(AreaPositions.builder().addLinearPosition((LinearPositions) p).build());
            } else {
                throw new IllegalArgumentException("The position " + p +  "cannot be a child of MultiDimensionalPositions");
            }
        }

        @Override
        public MultiDimensionalPositions build() {
            return new MultiDimensionalPositions(areaPositions);
        }

    }


}
