package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LineString;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.LineString} and
 * {@link LineString}.
 */
public class LineStringCodec extends AbstractJtsCodec<org.locationtech.jts.geom.LineString, LineString> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.LineString JTS
     * LineString} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public LineStringCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public LineString toGeometry(org.locationtech.jts.geom.LineString src) {
        return fromJtsLineString(src);
    }

    @Override
    public org.locationtech.jts.geom.LineString fromGeometry(LineString src) {
        return toJtsLineString(src);
    }
}
