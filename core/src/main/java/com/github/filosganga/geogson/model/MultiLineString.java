package com.github.filosganga.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * Geometry composed by a collection of {@link LineString}.
 *
 * GeoJson reference: {@see http://geojson.org/geojson-spec.html#multilinestring}
 *
 * eg: {@code
 *     MultiLineString mls = MultiLineString.of(
 *         LineString.of(Point.from(1,2), Point(2,2)),
 *         LineString.of(Point.from(2,3), Point(3,3))
 *     )
 * }
 *
 * TODO Consider adding an abstract parent for this and Polygon.
 */
public class MultiLineString extends AbstractGeometry<AreaPositions> {

    private static final long serialVersionUID = 1L;

    public MultiLineString(AreaPositions positions) {
        super(positions);
    }

    /**
     * Creates a MultiLineString from the given LineStrings.
     *
     * @param lineStrings The {@link LineString} sequence.
     * @return MultiLineString.
     */
    public static MultiLineString of(LineString... lineStrings) {
        return MultiLineString.of(ImmutableList.copyOf(lineStrings));
    }

    /**
     * Creates a MultiLineString from the given LineStrings.
     *
     * @param lineStrings The Iterable of {@link LineString}.
     * @return MultiLineString.
     */
    public static MultiLineString of(Iterable<LineString> lineStrings) {

        return new MultiLineString(new AreaPositions(transform(lineStrings, positionsFn(LinearPositions.class))));
    }

    @Override
    public Type type() {
        return Type.MULTI_LINE_STRING;
    }

    /**
     * Converts to a {@link Polygon}.
     *
     * @throws IllegalArgumentException if this MultiLineString contains an open {@link LineString} or it is empty.
     * @return Polygon
     */
    public Polygon toPolygon() {
        return new Polygon(positions());
    }

    /**
     * Converts to a {@link MultiLineString}.
     *
     * @return this.
     */
    public MultiLineString toMultiLineString() {
        return new MultiLineString(positions());
    }

    /**
     * Returns the {@link LineString} Iterable contained by this MultiLineString.
     *
     * @return Guava lazy Iterable<LineString>.
     */
    public Iterable<LineString> lineStrings() {
        return FluentIterable.from(positions().children())
                .transform(new Function<LinearPositions, LineString>() {
                    @Override
                    public LineString apply(LinearPositions input) {
                        return new LineString(input);
                    }
                });
    }

}
