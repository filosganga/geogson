package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LineString;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.LineString, LineString> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.LineString JTS
     * LineString} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public LineStringCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public LineString toGeometry(com.vividsolutions.jts.geom.LineString src) {
        return fromJtsLineString(src);
    }

    @Override
    public com.vividsolutions.jts.geom.LineString fromGeometry(LineString src) {
        return toJtsLineString(src);
    }
}
