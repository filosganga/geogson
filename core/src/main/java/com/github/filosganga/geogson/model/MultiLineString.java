package com.github.filosganga.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiLineString extends AbstractGeometry<AreaPositions> {

    public MultiLineString(AreaPositions positions) {
        super(positions);
    }

    public static MultiLineString of(LineString... lineStrings) {
        return MultiLineString.of(ImmutableList.copyOf(lineStrings));
    }

    public static MultiLineString of(Iterable<LineString> lineStrings) {

        return new MultiLineString(new AreaPositions(transform(lineStrings, positionsFn(LinearPositions.class))));
    }

    @Override
    public Type type() {
        return Type.MULTI_LINE_STRING;
    }

    public Polygon toPolygon() {
        return new Polygon(positions());
    }

    public MultiLineString toMultiLineString() {
        return new MultiLineString(positions());
    }

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
