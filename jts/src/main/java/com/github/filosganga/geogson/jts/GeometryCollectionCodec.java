package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * Codec for a {@link org.locationtech.jts.geom.GeometryCollection JTS
 * GeometryCollection}
 */
public class GeometryCollectionCodec extends AbstractJtsCodec<org.locationtech.jts.geom.GeometryCollection, GeometryCollection> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.GeometryCollection JTS
     * GeometryCollection} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public GeometryCollectionCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public GeometryCollection toGeometry(org.locationtech.jts.geom.GeometryCollection src) {
        return GeometryCollection.of(StreamSupport.stream(JtsGeometryCollectionIterable.of(src).spliterator(), false)
                .map(this::fromJtsGeometryCollection)
                .toArray(Geometry<?>[]::new)
        );
    }

    @Override
    public org.locationtech.jts.geom.GeometryCollection fromGeometry(GeometryCollection src) {
        return this.geometryFactory.createGeometryCollection(
                StreamSupport.stream(src.getGeometries().spliterator(), false)
                        .map(this::toJtsGeometryCollection)
                        .toArray(org.locationtech.jts.geom.Geometry[]::new)
        );
    }
}
