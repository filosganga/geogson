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
 * It represent a collection of Area coordinates.
 */
public class MultiDimensionalPositions extends AbstractPositions<AreaPositions> {

    public MultiDimensionalPositions(ImmutableList<AreaPositions> children) {
        super(children);
    }

    public MultiDimensionalPositions(Iterable<AreaPositions> children) {
        this(ImmutableList.copyOf(children));
    }

    @Override
    public Positions merge(Positions other) {
        if (other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and multidimensional positions");
        } else if (other instanceof LinearPositions) {
            // It can happen when a Polygon does not have holes and is represented by linear position see bug #19
            return merge(new AreaPositions(ImmutableList.of((LinearPositions) other)));

        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.<AreaPositions>builder().addAll(children).add(that).build());
        } else {

            throw new RuntimeException("Cannot merge with: " + other);
        }
    }

}
