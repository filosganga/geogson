package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LinearRing;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearRingCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.LinearRing, LinearRing> {

    @Override
    public LinearRing toGeometry(com.vividsolutions.jts.geom.LinearRing src) {
        return fromJtsLinearRing(src);
    }

    @Override
    public com.vividsolutions.jts.geom.LinearRing fromGeometry(LinearRing src) {
        return toJtsLinearRing(src);
    }
}
