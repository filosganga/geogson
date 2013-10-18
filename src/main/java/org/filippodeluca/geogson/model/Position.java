package org.filippodeluca.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Position implements Serializable {

    private final double lon;

    private final double lat;

    private Position(double lon, double lat) {

        checkArgument(abs(lon) <= 180, "lon is out of range -180:180: " + lon);
        checkArgument(abs(lat) <= 90, "lat is out of range -90:90: " + lon);

        this.lon = lon;
        this.lat = lat;
    }

    public static Position of(double lon, double lat) {
        return new Position(lon, lat);
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public Position withLon(double lon) {
        return Position.of(lon, lat);
    }

    public Position withLat(double lat) {
        return Position.of(lon, lat);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Position.class, lon, lat);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Position other = (Position) obj;
            return Objects.equal(this.lon, other.lon) && Objects.equal(this.lat, other.lat);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("lon", lon).add("lat", lat).toString();
    }
}
