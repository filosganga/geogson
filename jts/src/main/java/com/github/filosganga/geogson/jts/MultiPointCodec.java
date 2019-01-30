package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.MultiPoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.MultiPoint} and
 * {@link MultiPoint}.
 */
public class MultiPointCodec extends AbstractJtsCodec<org.locationtech.jts.geom.MultiPoint, MultiPoint> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.MultiPoint JTS
     * MultiPoint} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiPointCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiPoint toGeometry(org.locationtech.jts.geom.MultiPoint src) {
        return MultiPoint.of(StreamSupport.stream(JtsPointIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPoint));
    }

    @Override
    public org.locationtech.jts.geom.MultiPoint fromGeometry(MultiPoint src) {
        return this.geometryFactory.createMultiPoint(
                src.points().stream()
                        .map(p -> new Coordinate(p.lon(), p.lat(), p.alt()))
                        .toArray(Coordinate[]::new)
        );
    }
}
