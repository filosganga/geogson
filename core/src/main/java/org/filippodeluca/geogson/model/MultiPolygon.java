package org.filippodeluca.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.google.common.collect.ImmutableList;
import org.filippodeluca.geogson.model.positions.AreaPositions;
import org.filippodeluca.geogson.model.positions.MultiDimensionalPositions;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPolygon extends AbstractGeometry<MultiDimensionalPositions> {

    public MultiPolygon(MultiDimensionalPositions positions) {
        super(positions);
    }

    public static MultiPolygon of(Polygon... polygons) {

        return of(ImmutableList.copyOf(polygons));
    }

    public static MultiPolygon of(Iterable<Polygon> polygons) {

        return new MultiPolygon(
                new MultiDimensionalPositions(transform(polygons, positionsFn(AreaPositions.class)))
        );
    }

    @Override
    public Type type() {
        return Type.MULTI_POLYGON;
    }

}
