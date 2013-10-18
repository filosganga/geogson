package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Point extends Shape implements Serializable {

    private final Coordinate coordinate;

    public Point(Coordinate coordinate) {

        checkArgument(coordinate != null, "coordinate must be not null");

        this.coordinate = coordinate;
    }

    public static Point of(double lon, double lat) {
        return of(Coordinate.of(lon, lat));
    }

    public static Point of(Coordinate coordinate) {
        return new Point(coordinate);

    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getLon() {
        return coordinate.getLon();
    }

    public double getLat() {
        return coordinate.getLat();
    }

    public Point withCoordinate(Coordinate coordinate) {
        return of(coordinate);
    }

    public Point withLon(double lon) {
        return of(lon, coordinate.getLat());
    }

    public Point withLat(double lat) {
        return of(coordinate.getLon(), lat);
    }

    @Override
    public boolean isPoint() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coordinate);
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
        return Objects.equal(this.coordinate, other.coordinate);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("coordinate", coordinate).toString();
    }
}
