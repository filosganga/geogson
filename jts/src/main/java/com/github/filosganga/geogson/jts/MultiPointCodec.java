package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.MultiPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link com.vividsolutions.jts.geom.MultiPoint} and
 * {@link MultiPoint}.
 */
public class MultiPointCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPoint, MultiPoint> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.MultiPoint JTS
     * MultiPoint} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiPointCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiPoint toGeometry(com.vividsolutions.jts.geom.MultiPoint src) {
        return MultiPoint.of(StreamSupport.stream(JtsPointIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPoint));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiPoint fromGeometry(MultiPoint src) {
        return this.geometryFactory.createMultiPoint(
                src.points().stream()
                        .map(p -> new Coordinate(p.lon(), p.lat(), p.alt()))
                        .toArray(Coordinate[]::new)
        );
    }
}
