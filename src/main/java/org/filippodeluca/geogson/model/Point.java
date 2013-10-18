package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Point extends Geometry implements Serializable {

    private final Position position;

    public Point(Position position) {

        checkArgument(position != null, "coordinate must be not null");

        this.position = position;
    }

    public static Point of(double lon, double lat) {
        return of(Position.of(lon, lat));
    }

    public static Point of(Position position) {
        return new Point(position);

    }

    public Position getPosition() {
        return position;
    }

    public double getLon() {
        return position.getLon();
    }

    public double getLat() {
        return position.getLat();
    }

    public Point withPosition(Position position) {
        return of(position);
    }

    public Point withLon(double lon) {
        return of(lon, position.getLat());
    }

    public Point withLat(double lat) {
        return of(position.getLon(), lat);
    }

    @Override
    public Type getType() {
        return Type.POINT;
    }

    @Override
    public SinglePosition getPositions() {
        return new SinglePosition(position);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
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
        return Objects.equal(this.position, other.position);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("coordinate", position).toString();
    }
}
