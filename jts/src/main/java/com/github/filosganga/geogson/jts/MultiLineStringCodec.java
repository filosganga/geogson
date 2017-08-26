package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.MultiLineString;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link com.vividsolutions.jts.geom.MultiLineString} and
 * {@link MultiLineString}.
 */
public class MultiLineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiLineString, MultiLineString> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.MultiLineString JTS
     * MultiLineString} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiLineStringCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiLineString toGeometry(com.vividsolutions.jts.geom.MultiLineString src) {
        return MultiLineString.of(StreamSupport.stream(JtsLineStringIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsLineString));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiLineString fromGeometry(MultiLineString src) {
        return this.geometryFactory.createMultiLineString(
                StreamSupport.stream(src.lineStrings().spliterator(), false)
                        .map(this::toJtsLineString)
                        .toArray(com.vividsolutions.jts.geom.LineString[]::new)
        );
    }
}
