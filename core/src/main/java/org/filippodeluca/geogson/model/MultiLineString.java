package org.filippodeluca.geogson.model;

import static com.google.common.collect.Iterables.transform;

import java.io.Serializable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.filippodeluca.geogson.model.positions.AreaPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiLineString implements Geometry, Serializable {

    private final AreaPositions positions;

    public MultiLineString(AreaPositions positions) {
        this.positions = positions;
    }

    public static MultiLineString of(LineString... lineStrings) {
        return of(ImmutableList.copyOf(lineStrings));
    }

    public static MultiLineString of(Iterable<LineString> lineStrings) {
        return new MultiLineString(new AreaPositions(transform(lineStrings, LineString.positionsFn())));
    }

    public static Function<MultiLineString, AreaPositions> positionsFn() {
        return PositionsFn.INSTANCE;
    }

    @Override
    public Type type() {
        return Type.MULTI_LINE_STRING;
    }

    @Override
    public AreaPositions positions() {
        return positions;
    }

    public int size() {
        return Iterables.size(positions.children());
    }

    public Polygon toPolygon() {
        return new Polygon(positions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MultiLineString other = (MultiLineString) obj;
        return Objects.equal(this.positions, other.positions);
    }

    private static enum PositionsFn implements Function<MultiLineString, AreaPositions> {
        INSTANCE;

        @Override
        public AreaPositions apply(MultiLineString input) {
            return input.positions();
        }
    }

}
