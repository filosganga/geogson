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

import static com.google.common.collect.Iterables.concat;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiDimensionalPositions implements Positions {

    private ImmutableList<AreaPositions> positions;


    public MultiDimensionalPositions(ImmutableList<AreaPositions> positions) {
        this.positions = positions;
    }

    public MultiDimensionalPositions(Iterable<AreaPositions> positions) {
        this(ImmutableList.copyOf(positions));
    }

    public ImmutableList<AreaPositions> getPositions() {
        return positions;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and multidimensionl positions");
        } else if(other instanceof LinearPositions) {

            throw new IllegalArgumentException("Cannot merge linear position and multidimensionl positions");
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.<AreaPositions>builder().addAll(positions).add(that).build());
        } else {

            throw new RuntimeException("Cannot merge wtih: " + other);
        }
    }

    @Override
    public Iterable<AreaPositions> getChildren() {
        return positions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MultiDimensionalPositions.class, positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MultiDimensionalPositions other = (MultiDimensionalPositions) obj;
        return Objects.equal(this.positions, other.positions);
    }
}
