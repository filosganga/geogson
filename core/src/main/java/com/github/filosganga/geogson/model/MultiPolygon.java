package com.github.filosganga.geogson.model;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

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
