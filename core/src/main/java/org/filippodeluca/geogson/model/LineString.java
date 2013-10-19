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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import org.filippodeluca.geogson.model.positions.LinearPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LineString extends LinearGeometry {

    public LineString(LinearPositions positions) {
        super(checkPositions(positions));
    }

    private static LinearPositions checkPositions(LinearPositions toCheck) {
        checkArgument(toCheck.size() >= 2);

        return toCheck;
    }

    public static LineString of(Point... points) {
        return of(ImmutableList.copyOf(points));
    }

    public static LineString of(Iterable<Point> points) {
        return new LineString(new LinearPositions(transform(points, Point.positionsFn())));
    }

    @Override
    public Type type() {
        return Type.LINE_STRING;
    }

    public boolean isClosed() {
        return positions().isClosed();
    }

}
