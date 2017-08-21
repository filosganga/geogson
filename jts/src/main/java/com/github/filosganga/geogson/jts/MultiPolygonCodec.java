package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.MultiPolygon;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link com.vividsolutions.jts.geom.MultiPolygon} and
 * {@link MultiPolygon}.
 */
public class MultiPolygonCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPolygon, MultiPolygon> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.MultiPolygon JTS
     * MultiPolygon} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiPolygonCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiPolygon toGeometry(com.vividsolutions.jts.geom.MultiPolygon src) {
        return MultiPolygon.of(StreamSupport.stream(JtsPolygonIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPolygon));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiPolygon fromGeometry(MultiPolygon src) {
        return this.geometryFactory.createMultiPolygon(
                StreamSupport.stream(src.polygons().spliterator(), false)
                        .map(this::toJtsPolygon)
                        .toArray(com.vividsolutions.jts.geom.Polygon[]::new));
    }
}
