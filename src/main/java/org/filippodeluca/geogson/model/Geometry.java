package org.filippodeluca.geogson.model;

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class Geometry {

    public static enum Type {
        POINT("Point"),
        MULTI_POINT("MultiPoint"),
        LINE_STRING("LineString"),
        MULTI_LINE_STRING("MultiLineString"),
        POLYGON("Polygon"),
        MULTI_POLYGON("MultiPolygon"),
        GEOMETRY_COLLECTION("GeometryCollection");

        private final String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public abstract Type getType();

    public abstract Positions getPositions();
}
