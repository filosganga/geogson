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

import java.io.Serializable;

/**
 * The Positions represent the set of coordinates.
 *
 * GeoJson reference: {@see http://geojson.org/geojson-spec.html#positions}.
 */
public interface Positions extends Serializable {

    /**
     * Merge this Position with another one returning a new Position resulting of this merge.
     *
     * @param other Positions instance.
     * @return new Positions instance.
     */
    Positions merge(Positions other);

    /**
     * Return this position children Positions.
     * @return Iterable of Positions.
     */
    Iterable<? extends Positions> children();

    /**
     * The size of this positions. The semantic changes between different implementation of Positions.
     *
     * @return int.
     */
    int size();

}
