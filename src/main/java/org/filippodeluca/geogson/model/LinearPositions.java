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

import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearPositions implements Positions {

    private ImmutableList<Position> positions;

    public LinearPositions(ImmutableList<Position> positions) {
        this.positions = positions;
    }

    public ImmutableList<Position> getPositions() {
        return positions;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {
            SinglePosition that = (SinglePosition) other;
            return new LinearPositions(ImmutableList.<Position>builder().addAll(positions).add(that.getPosition()).build());
        } else if(other instanceof LinearPositions) {
            LinearPositions that = (LinearPositions) other;

            return new AreaPositions(ImmutableList.<LinearPositions>builder().add(this).add(that).build());
        } else {
            return other.merge(this);
        }
    }

    @Override
    public Iterable<Positions> getChildren() {
        return transform(positions, new Function<Position, Positions>() {
            @Override
            public Positions apply(Position input) {
                return new SinglePosition(input);
            }
        });
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LinearPositions other = (LinearPositions) obj;
        return Objects.equal(this.positions, other.positions);
    }
}
