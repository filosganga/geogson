package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return of(Arrays.asList(polygons));
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} sequence.
     *
     * @param polygons The {@link Polygon} Iterable.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Iterable<Polygon> polygons) {
        MultiDimensionalPositions.Builder positionsBuilder = MultiDimensionalPositions.builder();
        for(Polygon polygon : polygons) {
            positionsBuilder.addAreaPosition(polygon.positions());
        }

        return new MultiPolygon(positionsBuilder.build());
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} stream.
     *
     * @param polygons The {@link Polygon} Stream.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Stream<Polygon> polygons) {
        return of(polygons.collect(Collectors.toList()));
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
    public List<Polygon> polygons() {
        return positions().children().stream()
                .map(Polygon::new)
                .collect(Collectors.toList());

    }

}
