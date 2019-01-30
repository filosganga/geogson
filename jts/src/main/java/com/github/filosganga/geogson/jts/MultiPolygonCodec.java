package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.MultiPolygon;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.MultiPolygon} and
 * {@link MultiPolygon}.
 */
public class MultiPolygonCodec extends AbstractJtsCodec<org.locationtech.jts.geom.MultiPolygon, MultiPolygon> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.MultiPolygon JTS
     * MultiPolygon} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiPolygonCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiPolygon toGeometry(org.locationtech.jts.geom.MultiPolygon src) {
        return MultiPolygon.of(StreamSupport.stream(JtsPolygonIterable.of(src).spliterator(), false)
                .map(AbstractJtsCodec::fromJtsPolygon));
    }

    @Override
    public org.locationtech.jts.geom.MultiPolygon fromGeometry(MultiPolygon src) {
        return this.geometryFactory.createMultiPolygon(
                StreamSupport.stream(src.polygons().spliterator(), false)
                        .map(this::toJtsPolygon)
                        .toArray(org.locationtech.jts.geom.Polygon[]::new));
    }
}
