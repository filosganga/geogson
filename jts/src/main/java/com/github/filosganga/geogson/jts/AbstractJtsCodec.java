package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.codec.Codec;
import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class AbstractJtsCodec<S extends Object, T extends Geometry<?>> implements Codec<S, T> {

    protected final GeometryFactory geometryFactory = new GeometryFactory();

    // Polygon ---

    protected Function<Polygon, com.vividsolutions.jts.geom.Polygon> toJtsPolygonFn() {
        return new Function<Polygon, com.vividsolutions.jts.geom.Polygon>() {
            @Override
            public com.vividsolutions.jts.geom.Polygon apply(Polygon input) {
                return toJtsPolygon(input);
            }
        };
    }

    protected com.vividsolutions.jts.geom.Polygon toJtsPolygon(Polygon src) {

        return geometryFactory.createPolygon(
                toJtsLinearRing(src.perimeter()),
                FluentIterable.from(src.holes())
                        .transform(toJtsLinearRingFn())
                        .toArray(com.vividsolutions.jts.geom.LinearRing.class)
        );
    }

    public Function<com.vividsolutions.jts.geom.Polygon, Polygon> fromJtsPolygonFn() {
        return new Function<com.vividsolutions.jts.geom.Polygon, Polygon>() {
            @Override
            public Polygon apply(com.vividsolutions.jts.geom.Polygon input) {
                return fromJtsPolygon(input);
            }
        };
    }

    protected Polygon fromJtsPolygon(com.vividsolutions.jts.geom.Polygon src) {

        return Polygon.of(fromJtsLineString(src.getExteriorRing()).toLinearRing(),
                FluentIterable.from(JtsLineStringIterable.forHolesOf(src))
                        .transform(fromJtsLineStringFn())
                .transform(LinearRing.toLinearRingFn())
        );
    }

    // LinearRing ---

    protected Function<LinearRing, com.vividsolutions.jts.geom.LinearRing> toJtsLinearRingFn() {
        return new Function<LinearRing, com.vividsolutions.jts.geom.LinearRing>() {
            @Override
            public com.vividsolutions.jts.geom.LinearRing apply(LinearRing input) {
                return toJtsLinearRing(input);
            }
        };
    }

    protected com.vividsolutions.jts.geom.LinearRing toJtsLinearRing(LinearRing src) {

        return  geometryFactory.createLinearRing(
                FluentIterable.from(src.positions().children())
                        .transform(SinglePosition.coordinatesFn())
                        .transform(toJtsCoordinateFn())
                        .toArray(Coordinate.class)

        );
    }

    protected Function<com.vividsolutions.jts.geom.LinearRing, LinearRing> fromJtsLinearRingFn() {
        return new Function<com.vividsolutions.jts.geom.LinearRing, LinearRing>() {
            @Override
            public LinearRing apply(com.vividsolutions.jts.geom.LinearRing input) {
                return fromJtsLinearRing(input);
            }
        };
    }

    protected LinearRing fromJtsLinearRing(com.vividsolutions.jts.geom.LinearRing src) {
        return  LinearRing.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    // LineString ---

    protected Function<LineString, com.vividsolutions.jts.geom.LineString> toJtsLineStringFn() {
        return new Function<LineString, com.vividsolutions.jts.geom.LineString>() {
            @Override
            public com.vividsolutions.jts.geom.LineString apply(LineString input) {
                return toJtsLineString(input);
            }
        };
    }

    protected com.vividsolutions.jts.geom.LineString toJtsLineString(LineString src) {

        return  geometryFactory.createLineString(
                FluentIterable.from(src.positions().children())
                        .transform(SinglePosition.coordinatesFn())
                        .transform(toJtsCoordinateFn())
                        .toArray(Coordinate.class)

        );
    }

    protected Function<com.vividsolutions.jts.geom.LineString, LineString> fromJtsLineStringFn() {
        return new Function<com.vividsolutions.jts.geom.LineString, LineString>() {
            @Override
            public LineString apply(com.vividsolutions.jts.geom.LineString input) {
                return fromJtsLineString(input);
            }
        };
    }

    protected LineString fromJtsLineString(com.vividsolutions.jts.geom.LineString src) {

        return LineString.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    // Point


    protected static com.vividsolutions.jts.geom.Point toJtsPoint(Point src) {
        return new GeometryFactory().createPoint(new Coordinate(src.lon(), src.lat()));
    }


    protected static Function<com.vividsolutions.jts.geom.Point, Point> fromJtsPointFn() {
        return new Function<com.vividsolutions.jts.geom.Point, Point>() {
            @Override
            public Point apply(com.vividsolutions.jts.geom.Point input) {
                return fromJtsPoint(input);
            }
        };
    }

    protected static Point fromJtsPoint(com.vividsolutions.jts.geom.Point src) {
        return Point.from(fromJtsCoordinate(src.getCoordinate()));
    }

    protected static Function<Coordinates, Coordinate> toJtsCoordinateFn() {
        return ToJtsCoordinate.INSTANCE;
    }

    protected static Coordinates fromJtsCoordinate(com.vividsolutions.jts.geom.Coordinate src) {
        return Coordinates.of(src.x, src.y);
    }


    private enum  ToJtsCoordinate implements Function<Coordinates, Coordinate> {
        INSTANCE;

        @Override
        public Coordinate apply(com.github.filosganga.geogson.model.Coordinates input) {
            return new Coordinate(input.getLon(), input.getLat());
        }


    }

}
