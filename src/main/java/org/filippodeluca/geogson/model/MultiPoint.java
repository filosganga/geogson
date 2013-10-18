package org.filippodeluca.geogson.model;

import static java.util.Arrays.asList;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPoint extends LinearGeometry {

    public MultiPoint(ImmutableList<Position> coordinates) {
        super(coordinates);
    }

    public static MultiPoint of(Position...positions) {
        return new MultiPoint(ImmutableList.copyOf(positions));
    }

    public static MultiPoint of(Point...points) {
        return new MultiPoint(ImmutableList.copyOf(FluentIterable.from(asList(points)).transform(new Function<Point, Position>() {
            @Override
            public Position apply(Point input) {
                return input.getPosition();
            }
        })));
    }

    public LineString toLineString() {
        return new LineString(coordinates);
    }

    @Override
    public Type getType() {
        return Type.MULTI_POINT;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MultiPoint other = (MultiPoint) obj;
        return Objects.equal(this.coordinates, other.coordinates);
    }
}
