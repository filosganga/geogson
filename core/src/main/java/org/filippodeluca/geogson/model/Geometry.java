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

import org.filippodeluca.geogson.model.positions.Positions;

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
public interface Geometry<P extends Positions> {

    public static enum Type {
        POINT("Point"),
        MULTI_POINT("MultiPoint"),
        LINE_STRING("LineString"),
        LINEAR_RING("LineString"),
        MULTI_LINE_STRING("MultiLineString"),
        POLYGON("Polygon"),
        MULTI_POLYGON("MultiPolygon"),
        GEOMETRY_COLLECTION("GeometryCollection");

        private final String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    Type type();

    P positions();

    int size();
}
