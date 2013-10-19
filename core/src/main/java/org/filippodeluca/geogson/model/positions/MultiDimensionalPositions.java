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

import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
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
        if(other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and multidimensionl positions");
        } else if(other instanceof LinearPositions) {

            throw new IllegalArgumentException("Cannot merge linear position and multidimensionl positions");
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.<AreaPositions>builder().addAll(children).add(that).build());
        } else {

            throw new RuntimeException("Cannot merge wtih: " + other);
        }
    }

}
