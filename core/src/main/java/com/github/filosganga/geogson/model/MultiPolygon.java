package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A {@link Geometry} composed by a collection of {@link Polygon}.
 * <p>
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#multipolygon.
 */
public class MultiPolygon extends AbstractGeometry<MultiDimensionalPositions> {

    private static final long serialVersionUID = 1L;

    public MultiPolygon(MultiDimensionalPositions positions) {
        super(positions);
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} sequence.
     *
     * @param polygons The {@link Polygon} sequence.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Polygon... polygons) {
        return of(Arrays.stream(polygons));
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} sequence.
     *
     * @param polygons The {@link Polygon} Iterable.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Iterable<Polygon> polygons) {
        return of(StreamSupport.stream(polygons.spliterator(), false));
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} stream.
     *
     * @param polygons The {@link Polygon} Stream.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Stream<Polygon> polygons) {

        return new MultiPolygon(new MultiDimensionalPositions(
                polygons.map(Polygon::positions)::iterator
        ));
    }

    @Override
    public Type type() {
        return Type.MULTI_POLYGON;
    }

    /**
     * Returns the Iterable of {@link Polygon} composing this MultiPolygon.
     *
     * @return an Iterable of the polygons contained in this MultiPolygon.
     */
    public Iterable<Polygon> polygons() {
        return StreamSupport.stream(positions().children().spliterator(), false)
                .map(Polygon::new)::iterator;

    }

}
