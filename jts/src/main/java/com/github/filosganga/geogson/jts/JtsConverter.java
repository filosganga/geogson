package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.MultiLineString;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
class JtsConverter {

    public static Geometry fromJtsGeometry(com.vividsolutions.jts.geom.Geometry src) {

        Geometry toReturn = null;

        if (src instanceof com.vividsolutions.jts.geom.MultiPolygon) {
            toReturn = fromJtsMultiPolygon((com.vividsolutions.jts.geom.MultiPolygon) src);
        } else if (src instanceof com.vividsolutions.jts.geom.Polygon) {
            toReturn = fromJtsPolygon((com.vividsolutions.jts.geom.Polygon) src);
        } else if (src instanceof com.vividsolutions.jts.geom.MultiLineString) {
            toReturn = fromJtsMultiLineString((com.vividsolutions.jts.geom.MultiLineString) src);
        } else if (src instanceof com.vividsolutions.jts.geom.LinearRing) {
            toReturn = fromJtsLinearRing((com.vividsolutions.jts.geom.LinearRing) src);
        } else if (src instanceof com.vividsolutions.jts.geom.LineString) {
            toReturn = fromJtsLineString((com.vividsolutions.jts.geom.LineString) src);
        } else if (src instanceof com.vividsolutions.jts.geom.MultiPoint) {
            toReturn = fromJtsMultiPoint((com.vividsolutions.jts.geom.MultiPoint) src);
        } else if (src instanceof com.vividsolutions.jts.geom.Point) {
            toReturn = fromJtsPoint((com.vividsolutions.jts.geom.Point) src);
        } else {
            throw new IllegalArgumentException("The JTS Geometry of type: " + src.getGeometryType() + " is not supported, sorry.");
        }

        return toReturn;
    }

    public static MultiPolygon fromJtsMultiPolygon(com.vividsolutions.jts.geom.MultiPolygon src) {

        return MultiPolygon.of(FluentIterable.from(JtsPolygonIterable.of(src))
                .transform(fromJtsPolygonFn())
        );
    }

    public static Function<com.vividsolutions.jts.geom.Polygon, Polygon> fromJtsPolygonFn() {
        return new Function<com.vividsolutions.jts.geom.Polygon, Polygon>() {
            @Override
            public Polygon apply(com.vividsolutions.jts.geom.Polygon input) {
                return fromJtsPolygon(input);
            }
        };
    }

    public static Polygon fromJtsPolygon(com.vividsolutions.jts.geom.Polygon src) {

        return Polygon.of(
                LinearRing.of(FluentIterable.from(JtsPointIterable.of(src.getExteriorRing()))
                        .transform(fromJtsPointFn())),
                FluentIterable.from(JtsLineStringIterable.forHolesOf(src))
                        .transform(fromJtsLineStringFn())
                        .transform(lineString2LinearRingFn())
        );
    }

    public static MultiLineString fromJtsMultiLineString(com.vividsolutions.jts.geom.MultiLineString src) {

        return MultiLineString.of(transform(JtsLineStringIterable.of(src), fromJtsLineStringFn()));
    }

    public static LinearRing fromJtsLinearRing(com.vividsolutions.jts.geom.LinearRing src) {

        return LinearRing.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    public static LineString fromJtsLineString(com.vividsolutions.jts.geom.LineString src) {

        return LineString.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    public static Function<com.vividsolutions.jts.geom.LineString, LineString> fromJtsLineStringFn() {
        return new Function<com.vividsolutions.jts.geom.LineString, LineString>() {
            @Override
            public LineString apply(com.vividsolutions.jts.geom.LineString input) {
                return fromJtsLineString(input);
            }
        };
    }

    public static MultiPoint fromJtsMultiPoint(com.vividsolutions.jts.geom.MultiPoint src) {

        return MultiPoint.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    public static Point fromJtsPoint(com.vividsolutions.jts.geom.Point src) {
        return Point.from(fromJtsCoordinate(src.getCoordinate()));
    }

    public static com.vividsolutions.jts.geom.Point toJtsPoint(Point src) {

        return new GeometryFactory().createPoint(new Coordinate(src.lon(), src.lat()));
    }

    public static Function<com.vividsolutions.jts.geom.Point, Point> fromJtsPointFn() {
        return new Function<com.vividsolutions.jts.geom.Point, Point>() {
            @Override
            public Point apply(com.vividsolutions.jts.geom.Point input) {
                return fromJtsPoint(input);
            }
        };
    }

    public static Coordinates fromJtsCoordinate(com.vividsolutions.jts.geom.Coordinate src) {
        return Coordinates.of(src.x, src.y);
    }

    public static Function<LineString, LinearRing> lineString2LinearRingFn() {
        return new Function<LineString, LinearRing>() {
            @Override
            public LinearRing apply(LineString input) {
                return input.toLinearRing();
            }
        };
    }

}
