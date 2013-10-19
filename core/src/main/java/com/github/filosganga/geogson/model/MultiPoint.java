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

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPoint extends LinearGeometry {

    public MultiPoint(LinearPositions coordinates) {
        super(coordinates);
    }

    public static MultiPoint of(Point... points) {
        return of(ImmutableList.copyOf(points));
    }

    public static MultiPoint of(Iterable<Point> points) {
        return new MultiPoint(new LinearPositions(transform(points, positionsFn(SinglePosition.class))));
    }

    @Override
    public Type type() {
        return Type.MULTI_POINT;
    }

}
