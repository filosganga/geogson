package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.AreaPositions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Geometry composed by a collection of {@link LineString}.
 * <p>
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#multilinestring.
 * <p>
 * eg: {@code
 * MultiLineString mls = MultiLineString.of(
 * LineString.of(Point.from(1,2), Point(2,2)),
 * LineString.of(Point.from(2,3), Point(3,3))
 * )
 * }
 * <p>
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
        return of(Arrays.asList(lineStrings));
    }

    /**
     * Creates a MultiLineString from the given LineStrings.
     *
     * @param lineStrings The Iterable of {@link LineString}.
     * @return MultiLineString.
     */
    public static MultiLineString of(Iterable<LineString> lineStrings) {
        AreaPositions.Builder positionsBuilder = AreaPositions.builder();
        for(LineString lineString : lineStrings) {
            positionsBuilder.addLinearPosition(lineString.positions());
        }
        return new MultiLineString(positionsBuilder.build());
    }

    /**
     * Creates a MultiLineString from the given LineStrings.
     *
     * @param lineStrings The Iterable of {@link LineString}.
     * @return MultiLineString.
     */
    public static MultiLineString of(Stream<LineString> lineStrings) {
        return of(lineStrings.collect(Collectors.toList()));
    }

    @Override
    public Type type() {
        return Type.MULTI_LINE_STRING;
    }

    /**
     * Converts to a {@link Polygon}.
     *
     * @return Polygon
     * @throws IllegalArgumentException if this MultiLineString contains an open {@link LineString} or it is empty.
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
     * @return Guava lazy {@code Iterable<LineString>}.
     */
    public List<LineString> lineStrings() {
        return positions().children().stream()
                .map(LineString::new)
                .collect(Collectors.toList());
    }

}
