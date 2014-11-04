package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.GeometryCollection;
import com.google.common.collect.FluentIterable;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Codec for a {@link com.vividsolutions.jts.geom.GeometryCollection JTS
 * GeometryCollection}
 *
 * @author lurajon jone@ecc.no
 * @author bernhard.kiselka
 */
public class GeometryCollectionCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.GeometryCollection, GeometryCollection> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.GeometryCollection JTS
     * GeometryCollection} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public GeometryCollectionCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public GeometryCollection toGeometry(com.vividsolutions.jts.geom.GeometryCollection src) {
        return GeometryCollection.of(transform(JtsGeometryCollectionIterable.of(src), fromJtsGeometryCollectionFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.GeometryCollection fromGeometry(GeometryCollection src) {
       return this.geometryFactory.createGeometryCollection(
           FluentIterable.from(src.getGeometries())
                   .transform(toJtsGeometryCollectionFn())
                   .toArray(com.vividsolutions.jts.geom.Geometry.class)
           );
    }
}
