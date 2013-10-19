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

import java.util.ArrayList;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class SinglePosition implements Positions {

    private final Position position;

    public SinglePosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {
            Position that = ((SinglePosition)other).getPosition();
            return new LinearPositions(ImmutableList.of(position, that));
        } else {
            return other.merge(this);
        }
    }

    @Override
    public Iterable<Positions> getChildren() {
        return new ArrayList<Positions>();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SinglePosition other = (SinglePosition) obj;
        return Objects.equal(this.position, other.position);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("position", position)
                .toString();
    }
}
