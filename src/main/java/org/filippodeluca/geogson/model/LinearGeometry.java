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

import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class LinearGeometry extends Geometry {

    protected final LinearPositions coordinates;

    public LinearGeometry(LinearPositions coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public LinearPositions getPositions() {
        return coordinates;
    }

    public ImmutableList<Position> getCoordinates() {
        return coordinates.getPositions();
    }

    public int getSize() {
        return coordinates.getPositions().size();
    }
}
