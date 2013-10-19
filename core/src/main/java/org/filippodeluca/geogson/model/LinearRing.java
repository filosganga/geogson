package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import org.filippodeluca.geogson.model.positions.LinearPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearRing extends LineString {

    public LinearRing(LinearPositions positions) {
        super(positions);

        checkArgument(positions.isClosed());
    }

    public static LinearRing of(Point...points) {
        return of(ImmutableList.copyOf(points));
    }

    public static LinearRing of(Iterable<Point> points) {
        return new LinearRing(new LinearPositions(transform(points, Point.getPositionsFn())));
    }

    @Override
    public Type getType() {
        return Type.LINEAR_RING;
    }

}
