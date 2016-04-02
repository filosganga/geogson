package com.github.filosganga.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * A {@link Geometry} composed by a collection of {@link Polygon}.
 *
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

        return of(ImmutableList.copyOf(polygons));
    }

    /**
     * Creates a MultiPolygon from the given {@link Polygon} sequence.
     *
     * @param polygons The {@link Polygon} Iterable.
     * @return MultiPolygon
     */
    public static MultiPolygon of(Iterable<Polygon> polygons) {

        return new MultiPolygon(
                new MultiDimensionalPositions(transform(polygons, positionsFn(AreaPositions.class)))
        );
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
        return FluentIterable.from(positions().children())
                .transform(new Function<AreaPositions, Polygon>() {
                    @Override
                    public Polygon apply(AreaPositions input) {
                        return new Polygon(input);
                    }
                });
    }

}
