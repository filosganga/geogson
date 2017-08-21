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

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.filosganga.geogson.util.Preconditions.checkArgument;

/**
 * A Geometry composed by a sequence of {@link LinearRing}s (or closed {@link LineString}s). The first one is the
 * external perimeter, the followers are the holes.
 *
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#polygon.
 */
public class Polygon extends MultiLineString {

    private static final long serialVersionUID = 1L;

    public Polygon(AreaPositions positions) {
        super(checkPositions(positions));
    }

    private static AreaPositions checkPositions(AreaPositions src) {

        checkArgument(src, s -> s.size() >= 1, "A polygon should have at least one perimeter");
        src.children().forEach(child -> checkArgument(child, LinearPositions::isClosed, "The Polygon perimeters and holes must be closed"));

        return src;
    }

    /**
     * Creates a Polygon from the given perimeter and holes.
     *
     * @param perimeter The perimeter {@link LinearRing}.
     * @param holes The holes {@link LinearRing} sequence.
     *
     * @return Polygon
     */
    public static Polygon of(LinearRing perimeter, LinearRing... holes) {
        return Polygon.of(perimeter, Arrays.stream(holes));
    }

    /**
     * Creates a Polygon from the given perimeter and holes.
     *
     * @param perimeter The perimeter {@link LinearRing}.
     * @param holes The holes {@link LinearRing} Iterable.
     *
     * @return Polygon
     */
    public static Polygon of(LinearRing perimeter, Iterable<LinearRing> holes) {

        return of(perimeter, StreamSupport.stream(holes.spliterator(), false));
    }

    /**
     * Creates a Polygon from the given perimeter and holes.
     *
     * @param perimeter The perimeter {@link LinearRing}.
     * @param holes The holes {@link LinearRing} Stream.
     *
     * @return Polygon
     */
    public static Polygon of(LinearRing perimeter, Stream<LinearRing> holes) {

        return new Polygon(AreaPositions.builder()
                .addLinearPosition(perimeter.positions())
                .addLinearPositions(holes
                        .map(LinearRing::positions)::iterator)
                .build());
    }


    @Override
    public Type type() {
        return Type.POLYGON;
    }

    /**
     * Returns the {@link LinearRing}s composing this Polygon.
     *
     * @return a Guava lazy Iterable of {@link LinearRing}.
     */
    public Iterable<LinearRing> linearRings() {
        return StreamSupport.stream(lineStrings().spliterator(), false)
                .map(LineString::toLinearRing)::iterator;
    }

    /**
     * Returns the perimeter {@link LinearRing}.
     *
     * @return LinearRing
     */
    public LinearRing perimeter() {
        return linearRings().iterator().next();
    }

    /**
     * Returns the holes {@link LinearRing}s.
     *
     * @return a Guava lazy Iterable of {@link LinearRing}.
     */
    public Iterable<LinearRing> holes() {
        return StreamSupport.stream(linearRings().spliterator(), false).skip(1)::iterator;
    }
}
