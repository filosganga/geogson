package org.filippodeluca.geogson.model;

import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPolygon extends Geometry {

    private final MultiDimensionalPositions coordinates;

    public MultiPolygon(MultiDimensionalPositions coordinates) {
        this.coordinates = coordinates;
    }

    public static MultiPolygon of(Polygon... polygons) {

        return new MultiPolygon(
                new MultiDimensionalPositions(ImmutableList.copyOf(
                        transform(asList(polygons), new Function<Polygon, AreaPositions>() {
                            @Override
                            public AreaPositions apply(Polygon input) {
                                return input.getPositions();
                            }
                        })
                ))
        );

    }

    @Override
    public Type getType() {
        return Type.MULTI_POLYGON;
    }

    @Override
    public Positions getPositions() {
        return coordinates;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coordinates);
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
        return Objects.equal(this.coordinates, other.coordinates);
    }
}
