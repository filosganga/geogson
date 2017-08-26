package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.stream.StreamSupport;

/**
 * Codec for a {@link com.vividsolutions.jts.geom.GeometryCollection JTS
 * GeometryCollection}
 */
public class GeometryCollectionCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.GeometryCollection, GeometryCollection> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.GeometryCollection JTS
     * GeometryCollection} with a given {@link GeometryFactory}
     *
     * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public GeometryCollectionCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public GeometryCollection toGeometry(com.vividsolutions.jts.geom.GeometryCollection src) {
        return GeometryCollection.of(StreamSupport.stream(JtsGeometryCollectionIterable.of(src).spliterator(), false)
                .map(this::fromJtsGeometryCollection)
                .toArray(Geometry<?>[]::new)
        );
    }

    @Override
    public com.vividsolutions.jts.geom.GeometryCollection fromGeometry(GeometryCollection src) {
        return this.geometryFactory.createGeometryCollection(
                StreamSupport.stream(src.getGeometries().spliterator(), false)
                        .map(this::toJtsGeometryCollection)
                        .toArray(com.vividsolutions.jts.geom.Geometry[]::new)
        );
    }
}
