package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.filosganga.geogson.util.Preconditions.checkArgument;
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
        super(checkArgument(
                positions,
                LinearPositions::isClosed,
                "LinearRing must be composed by a minimum of 4 points with the first and the last that are the same."
        ));
    }

    /**
     * Create a LinearRing from the given points.
     *
     * @param points Point sequence composed at least by 4 points, with the first and the last that are the same.
     * @return a LinearRing
     */
    public static LinearRing of(Point... points) {
        return of(Arrays.stream(points));
    }

    /**
     * Create a LinearRing from the given points.
     *
     * @param points Point Iterable composed at least by 4 points, with the first and the last that are the same.
     * @return a LinearRing
     */
    public static LinearRing of(Iterable<Point> points) {
        LinearPositions.Builder builder = LinearPositions.builder();
        for(Point point : points) {
            builder.addSinglePosition(point.positions());
        }
        return new LinearRing(builder.build());
    }

    /**
     * Create a LinearRing from the given points.
     *
     * @param points Point Iterable composed at least by 4 points, with the first and the last that are the same.
     * @return a LinearRing
     */
    public static LinearRing of(Stream<Point> points) {

        return of(points::iterator);
    }

    @Override
    public Type type() {
        return Type.LINEAR_RING;
    }

}
