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

    public LineString(LinearPositions coordinates) {
        super(coordinates);

        checkArgument(getSize() >= 2);
    }

    public static LineString of(Position...positions) {
        return new LineString(new LinearPositions(ImmutableList.copyOf(positions)));
    }

    public static LineString of(Point...points) {
        return new LineString(new LinearPositions(ImmutableList.copyOf(FluentIterable.from(asList(points)).transform(new Function<Point, Position>() {
            @Override
            public Position apply(Point input) {
                return input.getPosition();
            }
        }))));
    }

    @Override
    public Type getType() {
        return Type.LINE_STRING;
    }

    public boolean isClosed() {
        return getCoordinates().size() >= 4 && getLast(getCoordinates()).equals(getFirst(getCoordinates(), null));
    }

    public boolean isLinearRing() {
        return isClosed();
    }

    public MultiPoint toMultiPoint() {
        return new MultiPoint(coordinates);
    }

}
