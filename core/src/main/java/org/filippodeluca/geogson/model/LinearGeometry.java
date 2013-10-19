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
import static org.filippodeluca.geogson.model.positions.SinglePosition.getCoordinatesFn;

import java.io.Serializable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import org.filippodeluca.geogson.model.positions.LinearPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class LinearGeometry implements Geometry, Serializable {

    protected final LinearPositions positions;

    protected LinearGeometry(LinearPositions positions) {
        this.positions = positions;
    }

    @Override
    public LinearPositions positions() {
        return positions;
    }

    public Iterable<Coordinates> getCoordinates() {
        return transform(positions.children(), getCoordinatesFn());
    }

    public int getSize() {
        return positions.getSize();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LinearGeometry other = (LinearGeometry) obj;
        return Objects.equal(this.positions, other.positions);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("positions", positions)
                .toString();
    }

    public static Function<LinearGeometry, LinearPositions> getPositionsFn() {
        return new Function<LinearGeometry, LinearPositions>() {
            @Override
            public LinearPositions apply(LinearGeometry input) {
                return input.positions();
            }
        };
    }
}
