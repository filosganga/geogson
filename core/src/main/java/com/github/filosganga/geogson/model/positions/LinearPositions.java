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

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;

import com.google.common.collect.ImmutableList;

/**
 * A {@link Positions} implementation for linear geometries. It is composed by a sequence of SinglePosition (points).
 */
public class LinearPositions extends AbstractPositions<SinglePosition> {

    private static final long serialVersionUID = 1L;

    public LinearPositions(ImmutableList<SinglePosition> children) {
        super(children);
    }

    /**
     * Create a LinearPositions from the given {@link SinglePosition} Iterable.
     * @param children Iterable of {@link SinglePosition}.
     */
    public LinearPositions(Iterable<SinglePosition> children) {
        this(ImmutableList.copyOf(children));
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
            return new LinearPositions(ImmutableList.<SinglePosition>builder().addAll(children).add(that).build());
        } else if (other instanceof LinearPositions) {
            LinearPositions that = (LinearPositions) other;

            return new AreaPositions(ImmutableList.<LinearPositions>builder().add(this).add(that).build());
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
        return size() >= 4 && getLast(children).equals(getFirst(children, null));
    }

}
