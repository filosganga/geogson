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

import static com.github.filosganga.geogson.model.LinearGeometry.toLinearRingFn;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.google.common.collect.ImmutableList;

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

        checkArgument(src.size() >= 1);

        for (LinearPositions child : src.children()) {
            checkArgument(child.isClosed());
        }

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
        return Polygon.of(perimeter, asList(holes));
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

        AreaPositions positions = new AreaPositions(ImmutableList.<LinearPositions>builder()
                .add(perimeter.positions())
                .addAll(transform(holes, positionsFn(LinearPositions.class)))
                .build());

        return new Polygon(positions);
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
        return transform(lineStrings(), toLinearRingFn());
    }

    /**
     * Returns the perimeter {@link LinearRing}.
     *
     * @return LinearRing
     */
    public LinearRing perimeter() {
        return getFirst(linearRings(), null);
    }

    /**
     * Returns the holes {@link LinearRing}s.
     *
     * @return a Guava lazy Iterable of {@link LinearRing}.
     */
    public Iterable<LinearRing> holes() {
        return skip(linearRings(), 1);
    }
}
