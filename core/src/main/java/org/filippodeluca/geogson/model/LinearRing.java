package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import static com.google.common.collect.Lists.asList;
import static com.google.common.collect.Lists.newArrayList;

import org.filippodeluca.geogson.model.positions.LinearPositions;
import org.filippodeluca.geogson.model.positions.SinglePosition;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearRing extends LineString {

    public LinearRing(LinearPositions positions) {
        super(checkPositions(positions));

        checkArgument(positions.isClosed());
    }

    private static LinearPositions checkPositions(LinearPositions toCheck) {
        checkArgument(toCheck.isClosed(), "LinearRing must be composed by a minimum of 4 points with the first and the last that are the same.");

        return toCheck;
    }

    public static LinearRing of(Point... points) {

        return LinearRing.of(ImmutableList.copyOf(newArrayList(points)));
    }

    public static LinearRing of(Iterable<Point> points) {
        return new LinearRing(new LinearPositions(transform(points, positionsFn(SinglePosition.class))));
    }

    @Override
    public Type type() {
        return Type.LINEAR_RING;
    }

}
