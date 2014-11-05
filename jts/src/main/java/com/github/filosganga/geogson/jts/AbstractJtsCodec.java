package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import java.util.ArrayList;

import com.github.filosganga.geogson.codec.Codec;
import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.GeometryCollection;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.MultiLineString;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
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

    /**
     * {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    protected final GeometryFactory geometryFactory;

    /**
     * Get the {@link GeometryFactory} of this {@link AbstractJtsCodec} (gotten
     * via constructor)
     *
     * @return the {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public GeometryFactory getGeometryFactory() {
        return this.geometryFactory;
    }

    /**
     * Create a codec with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public AbstractJtsCodec(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    // GeometryCollection ---

    protected Function<Geometry<?>, com.vividsolutions.jts.geom.Geometry> toJtsGeometryCollectionFn() {
      return new Function<Geometry<?>, com.vividsolutions.jts.geom.Geometry>() {
          @Override
          public com.vividsolutions.jts.geom.Geometry apply(Geometry<?> input) {
              return toJtsGeometryCollection(input);
          }
      };
    }

    protected com.vividsolutions.jts.geom.Geometry toJtsGeometryCollection(Geometry<?> src) {

        com.vividsolutions.jts.geom.Geometry returnGeometry;
        if (src instanceof Point) {
            returnGeometry = toJtsPoint((Point) src);
        } else if (src instanceof LineString) {
            returnGeometry = toJtsLineString((LineString) src);
        } else if (src instanceof Polygon) {
            returnGeometry = toJtsPolygon((Polygon) src);
        } else if (src instanceof MultiPoint) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPoint, MultiPoint> codec = new MultiPointCodec(this.geometryFactory);
            com.vividsolutions.jts.geom.MultiPoint multiPoint = codec.fromGeometry((MultiPoint) src);
            returnGeometry = multiPoint;
        } else if (src instanceof MultiLineString) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiLineString, MultiLineString> codec = new MultiLineStringCodec(this.geometryFactory);
            com.vividsolutions.jts.geom.MultiLineString multiLineString = codec.fromGeometry((MultiLineString) src);
            returnGeometry = multiLineString;
        } else if (src instanceof MultiPolygon) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPolygon, MultiPolygon> codec = new MultiPolygonCodec(this.geometryFactory);
            com.vividsolutions.jts.geom.MultiPolygon multiPolygon = codec.fromGeometry((MultiPolygon) src);
            returnGeometry = multiPolygon;
        } else if (src instanceof GeometryCollection) {
            ArrayList<com.vividsolutions.jts.geom.Geometry> geometries = new ArrayList<com.vividsolutions.jts.geom.Geometry>();
            GeometryCollection geometryCollection = (GeometryCollection) src;
            for (Geometry<?> geometry : geometryCollection.getGeometries()) {
                com.vividsolutions.jts.geom.Geometry jtsGeometry = toJtsGeometryCollection(geometry); // recursion!
                geometries.add(jtsGeometry);
            }
            returnGeometry = this.geometryFactory.createGeometryCollection(geometries.toArray(new com.vividsolutions.jts.geom.Geometry[geometryCollection.size()]));
        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + src.type()); //$NON-NLS-1$
        }
        return returnGeometry;
    }

    public Function<com.vividsolutions.jts.geom.Geometry, Geometry<?>> fromJtsGeometryCollectionFn() {
        return new Function<com.vividsolutions.jts.geom.Geometry, Geometry<?>>() {
            @Override
            public Geometry<?> apply(com.vividsolutions.jts.geom.Geometry input) {
                return fromJtsGeometryCollection(input);
            }
        };
    }

    protected Geometry<?> fromJtsGeometryCollection(com.vividsolutions.jts.geom.Geometry src) {
        Geometry<?> returnGeometry;
        if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.GEOMETRY_COLLECTION.getValue())) {
            ArrayList<Geometry<?>> geometries = new ArrayList<Geometry<?>>();
            for (int i = 0; i < src.getNumGeometries(); i++) {
                Geometry<?> geometry = null;
                com.vividsolutions.jts.geom.Geometry jtsGeometry = src.getGeometryN(i);
                geometry = fromJtsGeometryCollection(jtsGeometry); // recursion!
                geometries.add(geometry);
            }
            returnGeometry = GeometryCollection.of(geometries);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.POINT.getValue())) {
            returnGeometry = fromJtsPoint((com.vividsolutions.jts.geom.Point) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.LINE_STRING.getValue())) {
            returnGeometry = fromJtsLineString((com.vividsolutions.jts.geom.LineString) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.POLYGON.getValue())) {
            returnGeometry = fromJtsPolygon((com.vividsolutions.jts.geom.Polygon) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_POINT.getValue())) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPoint, MultiPoint> codec = new MultiPointCodec(this.geometryFactory);
            MultiPoint multiPoint = codec.toGeometry((com.vividsolutions.jts.geom.MultiPoint) src);
            returnGeometry = multiPoint;
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_LINE_STRING.getValue())) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiLineString, MultiLineString> codec = new MultiLineStringCodec(this.geometryFactory);
            MultiLineString multiLineString = codec.toGeometry((com.vividsolutions.jts.geom.MultiLineString) src);
            returnGeometry = multiLineString;
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_POLYGON.getValue())) {
            AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPolygon, MultiPolygon> codec = new MultiPolygonCodec(this.geometryFactory);
            MultiPolygon multiPolygon = codec.toGeometry((com.vividsolutions.jts.geom.MultiPolygon) src);
            returnGeometry = multiPolygon;
        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + src.getGeometryType()); //$NON-NLS-1$
        }

        return returnGeometry;
    }

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

        return this.geometryFactory.createPolygon(
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

        return this.geometryFactory.createLinearRing(
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

        return this.geometryFactory.createLineString(
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
