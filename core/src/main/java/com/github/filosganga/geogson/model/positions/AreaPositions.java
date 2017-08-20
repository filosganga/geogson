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

import com.google.common.collect.ImmutableList;

/**
 *  a {@link Positions} instance to represent an area Geometry.
 */
public class AreaPositions extends AbstractPositions<LinearPositions> {

    private static final long serialVersionUID = 1L;

    public AreaPositions(Iterable<LinearPositions> children) {
        super(children);
    }

    /**
     * Merge this Positions with another one. If the given {@link Positions} is:
     *  - SinglePosition, it will raise an IllegalArgumentException.
     *  - LinearPositions, it will return a new AreaPosition by appending the given LinearPositions to this.
     *  - AreaPositions, it will return a new MultiDimensionalPositions composed by this and the given AreaPositions,
     *    in order.
     *  - Any other, it delegates to the other the merge logic.
     *
     * @param other Positions instance to merge with.
     *
     * @return Positions results of merging.
     */
    @Override
    public Positions merge(Positions other) {

        if (other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and area children");
        } else if (other instanceof LinearPositions) {

            LinearPositions that = (LinearPositions) other;
            return new AreaPositions(ImmutableList.<LinearPositions>builder().addAll(children).add(that).build());
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.of(this, that));
        } else {

            return other.merge(this);
        }
    }

}
