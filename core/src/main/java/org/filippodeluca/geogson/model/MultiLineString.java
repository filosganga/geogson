package org.filippodeluca.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import org.filippodeluca.geogson.model.positions.AreaPositions;
import org.filippodeluca.geogson.model.positions.LinearPositions;

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

}
