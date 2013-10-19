package org.filippodeluca.geogson.model;

import static com.google.common.collect.Iterables.transform;

import java.io.Serializable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.filippodeluca.geogson.model.positions.MultiDimensionalPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPolygon implements Geometry, Serializable {

    private final MultiDimensionalPositions positions;

    public MultiPolygon(MultiDimensionalPositions positions) {
        this.positions = positions;
    }

    public static MultiPolygon of(Polygon... polygons) {

        return of(ImmutableList.copyOf(polygons));
    }

    public static MultiPolygon of(Iterable<Polygon> polygons) {

        return new MultiPolygon(
                new MultiDimensionalPositions(transform(polygons, Polygon.positionsFn()))
        );
    }

    public static Function<MultiPolygon, MultiDimensionalPositions> positionsFn() {
        return PositionsFn.INSTANCE;
    }

    @Override
    public Type type() {
        return Type.MULTI_POLYGON;
    }

    @Override
    public MultiDimensionalPositions positions() {
        return positions;
    }

    public int size() {
        return Iterables.size(positions.children());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MultiPolygon.class, positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MultiPolygon other = (MultiPolygon) obj;
        return Objects.equal(this.positions, other.positions);
    }

    private static enum PositionsFn implements Function<MultiPolygon, MultiDimensionalPositions> {
        INSTANCE;

        @Override
        public MultiDimensionalPositions apply(MultiPolygon input) {
            return input.positions();
        }
    }
}
