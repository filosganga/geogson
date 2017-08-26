package com.github.filosganga.geogson.gson.utils;


import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonUtils {

    public static class Coordinates implements Serializable {

        private static final long serialVersionUID = 1L;

        private final double lon;

        private final double lat;

        private final double alt;

        private Coordinates(double lon, double lat, double alt) {
            this.lon = lon;
            this.lat = lat;
            this.alt = alt;
        }

        /**
         * Create a new coordinate (a location on a map).
         *
         * @param lon
         *          longitude, x-axis coordinate, see {@link Coordinates#lon lon}
         * @param lat
         *          latitude, y-axis coordinate, see {@link Coordinates#lat lat}
         *
         * @return a Coordinates instances with the given longitude and latitude.
         */
        public static Coordinates of(double lon, double lat) {
            return new Coordinates(lon, lat, Double.NaN);
        }

        /**
         * Create a new coordinate (a location on a map).
         *
         * @param lon
         *          longitude, x-axis coordinate, see {@link Coordinates#lon lon}
         * @param lat
         *          latitude, y-axis coordinate, see {@link Coordinates#lat lat}
         * @param alt
         *          altitude, z-axis coordinate, see {@link Coordinates#alt alt}
         *
         * @return a Cordinates instances with the given longitude, latitude, and altitude.
         */
        public static Coordinates of(double lon, double lat, double alt) {
            return new Coordinates(lon, lat, alt);
        }

        /**
         * The x-axis coordinate in map units (degree, kilometer, meter, mile, foot
         * and inch). If your map is in a geographic projection, this will be the
         * Longitude. Otherwise, it will be the x coordinate of the map location in
         * your map units.
         *
         * @return a double x-axis value.
         */
        public double lon() {
            return lon;
        }

        /**
         * The y-axis coordinate in map units (degree, kilometer, meter, mile, foot
         * and inch). If your map is in a geographic projection, this will be the
         * Latitude. Otherwise, it will be the y coordinate of the map location in
         * your map units.
         *
         * @return a double y-axis value.
         */
        public double lat() {
            return lat;
        }

        /**
         * The z-axis coordinate in map units (degree, kilometer, meter, mile, foot
         * and inch). If your map is in a geographic projection, this will be the
         * Altitude. Otherwise, it will be the z coordinate of the map location in
         * your map units.
         *
         * @return a double z-axis value.
         */
        public double alt() {
            return alt;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Coordinates.class, lon, lat, alt);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null || getClass() != obj.getClass()) {
                return false;
            } else {
                final Coordinates other = (Coordinates) obj;
                return Objects.equals(this.lon, other.lon) &&
                        Objects.equals(this.lat, other.lat) &&
                        Objects.equals(this.alt, other.alt);
            }
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() +
                    '{' +
                    "lon: " + lon +
                    ", " +
                    "lat: " + lat +
                    ", " +
                    "alt: " + alt +
                    "}";
        }
    }



    public static String givenMultiDimensionalPositionsJson(Iterable<Iterable<? extends Iterable<Coordinates>>> areas) {

        return StreamSupport.stream(areas.spliterator(), false)
                .map(JsonUtils::givenAreaPositionsJson)
                .collect(Collectors.joining(",", "[", "]"));
    }


    public static String givenAreaPositionsJson(Iterable<? extends Iterable<Coordinates>> lines) {

        return StreamSupport.stream(lines.spliterator(), false)
                .map(JsonUtils::givenLinearPositionsJson)
                .collect(Collectors.joining(",", "[", "]"));
    }

    public static String givenLinearPositionsJson(Iterable<Coordinates> coordinates) {

        return StreamSupport.stream(coordinates.spliterator(), false)
                .map(c -> givenSinglePositionJson(c.lon(), c.lat()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    public static String givenSinglePositionJson(double lon, double lat) {

        return "[" + lon + "," + lat + "]";
    }

    private JsonUtils() {

    }
}
