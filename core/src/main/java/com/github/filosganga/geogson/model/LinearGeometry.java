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

import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class LinearGeometry extends AbstractGeometry<LinearPositions> {

    protected LinearGeometry(LinearPositions positions) {
        super(positions);
    }

    public static <T extends LinearGeometry> Function<T, MultiPoint> toMultiPointFn() {
        return new Function<T, MultiPoint>() {
            @Override
            public MultiPoint apply(T input) {
                return input.toMultiPoint();
            }
        };
    }

    public static <T extends LinearGeometry> Function<T, LineString> toLineStringFn() {
        return new Function<T, LineString>() {
            @Override
            public LineString apply(T input) {
                return input.toLineString();
            }
        };
    }

    public static <T extends LinearGeometry> Function<T, LinearRing> toLinearRingFn() {
        return new Function<T, LinearRing>() {
            @Override
            public LinearRing apply(T input) {
                return input.toLinearRing();
            }
        };
    }

    public MultiPoint toMultiPoint() {
        return new MultiPoint(positions());
    }

    public LineString toLineString() {
        return new LineString(positions());
    }

    public LinearRing toLinearRing() {
        return new LinearRing(positions());
    }

    public Iterable<Point> points() {
        return FluentIterable.from(positions().children())
                .transform(SinglePosition.coordinatesFn())
                .transform(new Function<Coordinates, Point>() {
                    @Override
                    public Point apply(com.github.filosganga.geogson.model.Coordinates input) {
                        return Point.from(input);
                    }
                });
    }

}
