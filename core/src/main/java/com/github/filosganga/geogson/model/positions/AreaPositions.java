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
 *  a {@link Positions} instance to represent an area Geometry.
 */
public class AreaPositions extends AbstractPositions<LinearPositions> {

    public static class Builder implements PositionsBuilder {

        private LinkedList<LinearPositions> linearPositions = new LinkedList<>();
        private boolean allChildrenAreClosed = true;

        public AreaPositions.Builder addLinearPosition(LinearPositions lp) {
            linearPositions.add(lp);
            allChildrenAreClosed = allChildrenAreClosed && lp.isClosed();
            return this;
        }

        public AreaPositions.Builder addLinearPositions(Iterable<LinearPositions> lps) {
            lps.forEach(this::addLinearPosition);
            return this;
        }

        @Override
        public PositionsBuilder addChild(Positions p) {
            if(p instanceof LinearPositions) {
                return addLinearPosition((LinearPositions)p);
            } else if (p instanceof SinglePosition) {
                return addLinearPosition(LinearPositions.builder().addSinglePosition((SinglePosition) p).build());
            } else if (p instanceof AreaPositions) {
                return MultiDimensionalPositions.builder().addAreaPosition(this.build()).addChild(p);
            } else {
                throw new IllegalArgumentException("The position " + p +  "cannot be a child of AreaPosition");
            }
        }

        public AreaPositions build() {
            return new AreaPositions(linearPositions, allChildrenAreClosed);
        }

    }

    private static final long serialVersionUID = 1L;

    private final Boolean allChildrenAreClosed;

    private AreaPositions(List<LinearPositions> children, Boolean allChildrenAreClosed) {
        super(children);
        this.allChildrenAreClosed = allChildrenAreClosed;
    }

    public static AreaPositions.Builder builder() {
        return new AreaPositions.Builder();
    }

    public static AreaPositions.Builder builder(AreaPositions positions) {
        return builder().addLinearPositions(positions.children);
    }

    public Boolean areAllChildrenClosed() {
        return allChildrenAreClosed;
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
            return builder().addLinearPosition(that).build();
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return MultiDimensionalPositions.builder().addAreaPosition(this).addAreaPosition(that).build();
        } else {

            return other.merge(this);
        }
    }

}
