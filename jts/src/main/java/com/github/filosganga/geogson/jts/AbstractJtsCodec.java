package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.codec.Codec;
import com.github.filosganga.geogson.model.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import static com.github.filosganga.geogson.util.Preconditions.checkNotNull;

/**
 * Abstract JTS implementation of Codec.
 * <p>
 * It provides support for JTS conversions.
 */
public abstract class AbstractJtsCodec<S, T extends Geometry<?>> implements Codec<S, T> {

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
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public AbstractJtsCodec(GeometryFactory geometryFactory) {
        this.geometryFactory = checkNotNull(geometryFactory, "The geometryFactory cannot be null");
    }

    // GeometryCollection ---

    protected org.locationtech.jts.geom.Geometry toJtsGeometryCollection(Geometry<?> src) {

        org.locationtech.jts.geom.Geometry returnGeometry;
        if (src instanceof Point) {
            returnGeometry = toJtsPoint((Point) src);
        } else if (src instanceof LineString) {
            returnGeometry = toJtsLineString((LineString) src);
        } else if (src instanceof Polygon) {
            returnGeometry = toJtsPolygon((Polygon) src);
        } else if (src instanceof MultiPoint) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiPoint, MultiPoint> codec = new MultiPointCodec(this.geometryFactory);
            returnGeometry = codec.fromGeometry((MultiPoint) src);
        } else if (src instanceof MultiLineString) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiLineString, MultiLineString> codec = new MultiLineStringCodec(this.geometryFactory);
            returnGeometry = codec.fromGeometry((MultiLineString) src);
        } else if (src instanceof MultiPolygon) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiPolygon, MultiPolygon> codec = new MultiPolygonCodec(this.geometryFactory);
            returnGeometry = codec.fromGeometry((MultiPolygon) src);
        } else if (src instanceof GeometryCollection) {
            GeometryCollection geometryCollection = (GeometryCollection) src;
            returnGeometry = this.geometryFactory.createGeometryCollection(StreamSupport.stream(geometryCollection.getGeometries().spliterator(), false)
                    .map(this::toJtsGeometryCollection)
                    .toArray(org.locationtech.jts.geom.Geometry[]::new));
        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + src.type());
        }
        return returnGeometry;
    }

    protected Geometry<?> fromJtsGeometryCollection(org.locationtech.jts.geom.Geometry src) {
        Geometry<?> returnGeometry;
        if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.GEOMETRY_COLLECTION.getValue())) {
            ArrayList<Geometry<?>> geometries = new ArrayList<Geometry<?>>();
            for (int i = 0; i < src.getNumGeometries(); i++) {
                Geometry<?> geometry = null;
                org.locationtech.jts.geom.Geometry jtsGeometry = src.getGeometryN(i);
                geometry = fromJtsGeometryCollection(jtsGeometry); // recursion!
                geometries.add(geometry);
            }
            returnGeometry = GeometryCollection.of(geometries);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.POINT.getValue())) {
            returnGeometry = fromJtsPoint((org.locationtech.jts.geom.Point) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.LINE_STRING.getValue())) {
            returnGeometry = fromJtsLineString((org.locationtech.jts.geom.LineString) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.POLYGON.getValue())) {
            returnGeometry = fromJtsPolygon((org.locationtech.jts.geom.Polygon) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_POINT.getValue())) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiPoint, MultiPoint> codec = new MultiPointCodec(this.geometryFactory);
            returnGeometry = codec.toGeometry((org.locationtech.jts.geom.MultiPoint) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_LINE_STRING.getValue())) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiLineString, MultiLineString> codec = new MultiLineStringCodec(this.geometryFactory);
            returnGeometry = codec.toGeometry((org.locationtech.jts.geom.MultiLineString) src);
        } else if (src.getGeometryType().equalsIgnoreCase(Geometry.Type.MULTI_POLYGON.getValue())) {
            AbstractJtsCodec<org.locationtech.jts.geom.MultiPolygon, MultiPolygon> codec = new MultiPolygonCodec(this.geometryFactory);
            returnGeometry = codec.toGeometry((org.locationtech.jts.geom.MultiPolygon) src);
        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + src.getGeometryType());
        }

        return returnGeometry;
    }

    // Polygon ---


    protected org.locationtech.jts.geom.Polygon toJtsPolygon(Polygon src) {

        return this.geometryFactory.createPolygon(
                toJtsLinearRing(src.perimeter()),
                StreamSupport
                        .stream(src.holes().spliterator(), false)
                        .map(this::toJtsLinearRing)
                        .toArray(org.locationtech.jts.geom.LinearRing[]::new)
        );
    }


    protected static Polygon fromJtsPolygon(org.locationtech.jts.geom.Polygon src) {

        return Polygon.of(
                fromJtsLineString(src.getExteriorRing()).toLinearRing(),
                StreamSupport
                        .stream(JtsLineStringIterable.forHolesOf(src).spliterator(), false)
                        .map(AbstractJtsCodec::fromJtsLineString)
                        .map(LinearGeometry::toLinearRing)::iterator
        );
    }

    // LinearRing ---

    protected org.locationtech.jts.geom.LinearRing toJtsLinearRing(LinearRing src) {

        return this.geometryFactory.createLinearRing(
                StreamSupport.stream(src.positions().children().spliterator(), false)
                        .map(sp -> new Coordinate(sp.lon(), sp.lat(), sp.alt()))
                        .toArray(Coordinate[]::new)

        );
    }


    protected static LinearRing fromJtsLinearRing(org.locationtech.jts.geom.LinearRing src) {
        return LinearRing.of(StreamSupport
                .stream(JtsPointIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPoint)::iterator
        );
    }

    // LineString ---


    protected org.locationtech.jts.geom.LineString toJtsLineString(LineString src) {

        return this.geometryFactory.createLineString(
                StreamSupport.stream(src.positions().children().spliterator(), false)
                        .map(sp -> new Coordinate(sp.lon(), sp.lat(), sp.alt()))
                        .toArray(Coordinate[]::new)

        );
    }

    protected static LineString fromJtsLineString(org.locationtech.jts.geom.LineString src) {
        return LineString.of(StreamSupport.stream(JtsPointIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPoint)
        );
    }

    // Point

    protected static org.locationtech.jts.geom.Point toJtsPoint(Point src) {
        return new GeometryFactory().createPoint(new Coordinate(src.lon(), src.lat()));
    }


    protected static Point fromJtsPoint(org.locationtech.jts.geom.Point src) {
        Coordinate coordinate = src.getCoordinate();
        return Point.from(coordinate.x, coordinate.y, coordinate.z);
    }

}
