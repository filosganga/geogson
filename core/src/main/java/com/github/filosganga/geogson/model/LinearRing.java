package com.github.filosganga.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.collect.ImmutableList;

/**
 * A closed {@link LineString}.
 *
 * JeoGson reference: @see http://geojson.org/geojson-spec.html#linestring.
 *
 * eg: {@code
 *     LinearRing lr = LinearRing.of(
 *         Point.of(1,1),
 *         Point.of(1,2),
 *         Point.of(2,2),
 *         Point.of(2,1),
 *         Point.of(1,1)
 *     )
 * }
 */
public class LinearRing extends LineString {

    private static final long serialVersionUID = 1L;

    public LinearRing(LinearPositions positions) {
        super(checkPositions(positions));

        checkArgument(positions.isClosed());
    }

    private static LinearPositions checkPositions(LinearPositions toCheck) {
        checkArgument(toCheck.isClosed(), "LinearRing must be composed by a minimum of 4 points with the first and the last that are the same.");

        return toCheck;
    }

    /**
     * Create a LinearRing from the given points.
     *
     * @param points Point sequence composed at least by 4 points, with the first and the last that are the same.
     * @return a LinearRing
     */
    public static LinearRing of(Point... points) {

        return LinearRing.of(ImmutableList.copyOf(newArrayList(points)));
    }

    /**
     * Create a LinearRing from the given points.
     *
     * @param points Point Iterable composed at least by 4 points, with the first and the last that are the same.
     * @return a LinearRing
     */
    public static LinearRing of(Iterable<Point> points) {
        return new LinearRing(new LinearPositions(transform(points, positionsFn(SinglePosition.class))));
    }

    @Override
    public Type type() {
        return Type.LINEAR_RING;
    }

}
