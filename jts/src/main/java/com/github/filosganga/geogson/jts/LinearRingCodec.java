package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LinearRing;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.LinearRing} and
 * {@link LinearRing}.
 */
public class LinearRingCodec extends AbstractJtsCodec<org.locationtech.jts.geom.LinearRing, LinearRing> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.LinearRing JTS
     * LinearRing} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public LinearRingCodec(GeometryFactory geometryFactory) {
      super(geometryFactory);
    }

    @Override
    public LinearRing toGeometry(org.locationtech.jts.geom.LinearRing src) {
        return fromJtsLinearRing(src);
    }

    @Override
    public org.locationtech.jts.geom.LinearRing fromGeometry(LinearRing src) {
        return toJtsLinearRing(src);
    }
}
