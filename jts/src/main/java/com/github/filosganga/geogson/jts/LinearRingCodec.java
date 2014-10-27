package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LinearRing;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearRingCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.LinearRing, LinearRing> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.LinearRing JTS
     * LinearRing} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public LinearRingCodec(GeometryFactory geometryFactory) {
      super(geometryFactory);
    }

    @Override
    public LinearRing toGeometry(com.vividsolutions.jts.geom.LinearRing src) {
        return fromJtsLinearRing(src);
    }

    @Override
    public com.vividsolutions.jts.geom.LinearRing fromGeometry(LinearRing src) {
        return toJtsLinearRing(src);
    }
}
