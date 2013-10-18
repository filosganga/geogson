package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static java.util.Arrays.asList;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LineString extends LinearGeometry {

    public LineString(ImmutableList<Position> coordinates) {
        super(coordinates);

        checkArgument(coordinates.size() >= 2);
    }

    public static LineString of(Position...positions) {
        return new LineString(ImmutableList.copyOf(positions));
    }

    public static LineString of(Point...points) {
        return new LineString(ImmutableList.copyOf(FluentIterable.from(asList(points)).transform(new Function<Point, Position>() {
            @Override
            public Position apply(Point input) {
                return input.getPosition();
            }
        })));
    }

    @Override
    public Type getType() {
        return Type.LINE_STRING;
    }

    public boolean isClosed() {
        return coordinates.size() >= 4 && getLast(coordinates).equals(getFirst(coordinates, null));
    }

    public boolean isLinearRing() {
        return isClosed();
    }

    public MultiPoint toMultiPoint() {
        return new MultiPoint(coordinates);
    }

}
