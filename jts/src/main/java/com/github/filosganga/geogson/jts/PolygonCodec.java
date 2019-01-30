package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Polygon;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.Polygon} and
 * {@link Polygon}.
 */
public class PolygonCodec extends AbstractJtsCodec<org.locationtech.jts.geom.Polygon, Polygon> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.Polygon JTS
     * Polygon} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public PolygonCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public Polygon toGeometry(org.locationtech.jts.geom.Polygon src) {
        return fromJtsPolygon(src);
    }

    @Override
    public org.locationtech.jts.geom.Polygon fromGeometry(Polygon src) {
        return toJtsPolygon(src);
    }
}
