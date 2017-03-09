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

package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.Positions;

/**
 * A Geometry is a definition of a shape (A collection of coordinates).
 *
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#geometry-objects.
 *
 * @param <P> The Position type param.
 */
public interface Geometry<P extends Positions> {

    /**
     * Define the type of the Geometry. As defined in the GeoJson specifications.
     */
    enum Type {
        POINT("Point"),
        MULTI_POINT("MultiPoint"),
        LINE_STRING("LineString"),
        LINEAR_RING("LineString"),
        MULTI_LINE_STRING("MultiLineString"),
        POLYGON("Polygon"),
        MULTI_POLYGON("MultiPolygon"),
        GEOMETRY_COLLECTION("GeometryCollection");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Type forValue(String value) {
            for (Type type : values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Cannot build a geometry for type: " + value);
        }
    }

    /**
     * Returns the Geometry type.
     *
     * @return Type
     */
    Type type();

    /**
     * Returns the Position underlying instance.
     *
     * @return Position
     */
    P positions();

    /**
     * Returns the size of this Geometry. Depending of the type of this Geometry, it may have different meaning.
     * eg: For a LineString it is the number of points, for a MultiPolygon it is the number of polygons.
     *
     * @return Int
     */
    int size();
}
