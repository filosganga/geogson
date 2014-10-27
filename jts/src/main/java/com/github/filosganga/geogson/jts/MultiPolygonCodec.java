package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.MultiPolygon;
import com.google.common.collect.FluentIterable;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiPolygonCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPolygon, MultiPolygon> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.MultiPolygon JTS
     * MultiPolygon} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiPolygonCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiPolygon toGeometry(com.vividsolutions.jts.geom.MultiPolygon src) {
        return MultiPolygon.of(transform(JtsPolygonIterable.of(src), fromJtsPolygonFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiPolygon fromGeometry(MultiPolygon src) {
        return this.geometryFactory.createMultiPolygon(
                FluentIterable.from(src.polygons())
                        .transform(toJtsPolygonFn())
                        .toArray(com.vividsolutions.jts.geom.Polygon.class)
        );
    }
}
