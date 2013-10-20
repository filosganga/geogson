package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.MultiPolygon;
import com.google.common.collect.FluentIterable;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPolygonCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPolygon, MultiPolygon> {

    @Override
    public MultiPolygon toGeometry(com.vividsolutions.jts.geom.MultiPolygon src) {
        return MultiPolygon.of(transform(JtsPolygonIterable.of(src), fromJtsPolygonFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiPolygon fromGeometry(MultiPolygon src) {
        return geometryFactory.createMultiPolygon(
                FluentIterable.from(src.polygons())
                        .transform(toJtsPolygonFn())
                        .toArray(com.vividsolutions.jts.geom.Polygon.class)
        );
    }
}
