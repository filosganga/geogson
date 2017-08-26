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
 * A {@link Positions} implementation for linear geometries. It is composed by a sequence of SinglePosition (points).
 */
public class LinearPositions extends AbstractPositions<SinglePosition> {

    private static final long serialVersionUID = 1L;

    private final Boolean isClosed;

    private LinearPositions(List<SinglePosition> children, boolean isClosed) {
        super(children);
        this.isClosed = isClosed;
    }

    public static LinearPositions.Builder builder() {
        return new LinearPositions.Builder();
    }

    public static LinearPositions.Builder builder(LinearPositions positions) {
        return builder().addSinglePositions(positions.children);
    }

    /**
     * Merge this LinearPositions with another one. If the given {@link Positions} is:
     *  - SinglePosition, it will return a new LinearPositions with the given SinglePosition appended.
     *  - LinearPositions, it will return a new AreaPosition composed by this and the given LinearPositions.
     *  - Any other, it delegates to the other the merge logic.
     *
     * @param other Positions instance to merge with.
     *
     * @return Positions results of merging.
     */
    @Override
    public Positions merge(Positions other) {
        if (other instanceof SinglePosition) {
            SinglePosition that = (SinglePosition) other;
            return builder().addSinglePosition(that).build();
        } else if (other instanceof LinearPositions) {
            LinearPositions that = (LinearPositions) other;
            return AreaPositions.builder().addLinearPosition(this).addLinearPosition(that).build();
        } else {
            return other.merge(this);
        }
    }

    /**
     * Returns if this LinearPosition:
     *  - Is composed by at least 4 points
     *  - The first and the last are the same.
     *
     * @return true if it is closed, false otherwise.
     */
    public boolean isClosed() {
        return isClosed;
    }

    public static class Builder implements PositionsBuilder {

        private LinkedList<SinglePosition> singlePositions = new LinkedList<>();

        private SinglePosition first = null;
        private SinglePosition last = null;
        private int size = 0;

        public Builder addSinglePosition(SinglePosition sp) {
            singlePositions.add(sp);
            if(first == null) {
                first = sp;
            }
            last = sp;
            size++;

            return this;
        }

        public Builder addSinglePositions(Iterable<SinglePosition> sps) {
            sps.forEach(this::addSinglePosition);
            return this;
        }

        @Override
        public PositionsBuilder addChild(Positions p) {
            if(p instanceof SinglePosition) {
                return addSinglePosition((SinglePosition) p);
            } else if (p instanceof LinearPositions) {
                return AreaPositions.builder().addChild(this.build()).addChild(p);
            } else {
                throw new IllegalArgumentException("The position " + p +  "cannot be a child of LinearPositions");
            }
        }

        @Override
        public LinearPositions build() {
            Boolean isClosed = size >= 4 && first.equals(last);
            return new LinearPositions(singlePositions, isClosed);
        }

    }


}
