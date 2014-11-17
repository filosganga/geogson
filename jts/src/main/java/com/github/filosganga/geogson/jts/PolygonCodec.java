package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Polygon;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link com.vividsolutions.jts.geom.Polygon} and
 * {@link Polygon}.
 */
public class PolygonCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.Polygon, Polygon> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.Polygon JTS
     * Polygon} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public PolygonCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public Polygon toGeometry(com.vividsolutions.jts.geom.Polygon src) {
        return fromJtsPolygon(src);
    }

    @Override
    public com.vividsolutions.jts.geom.Polygon fromGeometry(Polygon src) {
        return toJtsPolygon(src);
    }
}
