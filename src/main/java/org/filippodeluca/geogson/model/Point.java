package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.google.common.base.Function;
import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Point extends Geometry implements Serializable {

    private final SinglePosition coordinates;

    public Point(SinglePosition positions) {

        checkArgument(positions != null, "coordinate must be not null");

        this.coordinates = positions;
    }

    public static Point of(double lon, double lat) {
        return of(Position.of(lon, lat));
    }

    public static Point of(Position position) {
        return new Point(new SinglePosition(position));

    }

    public Position getPosition() {
        return coordinates.getPosition();
    }

    public double getLon() {
        return getPosition().getLon();
    }

    public double getLat() {
        return getPosition().getLat();
    }

    public Point withPosition(Position position) {
        return of(position);
    }

    public Point withLon(double lon) {
        return of(lon, getPosition().getLat());
    }

    public Point withLat(double lat) {
        return of(getPosition().getLon(), lat);
    }

    @Override
    public Type getType() {
        return Type.POINT;
    }

    @Override
    public SinglePosition getPositions() {
        return coordinates;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        return Objects.equal(this.coordinates, other.coordinates);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("coordinates", coordinates).toString();
    }

    public static Function<Point, Position> getPositionFn() {
        return new Function<Point, Position>() {
            @Override
            public Position apply(Point input) {
                return input.getPosition();
            }
        };
    }
}
