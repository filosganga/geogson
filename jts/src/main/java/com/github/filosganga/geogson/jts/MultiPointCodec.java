package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.Point;
import com.google.common.collect.FluentIterable;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPointCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPoint, MultiPoint> {

    @Override
    public MultiPoint toGeometry(com.vividsolutions.jts.geom.MultiPoint src) {
        return MultiPoint.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiPoint fromGeometry(MultiPoint src) {
        return geometryFactory.createMultiPoint(
                FluentIterable.from(src.points())
                        .transform(Point.coordinatesFn())
                        .transform(toJtsCoordinateFn())
                        .toArray(Coordinate.class)
        );
    }
}
