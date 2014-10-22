package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.GeometryCollection;

public class GeometryCollectionCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.GeometryCollection, GeometryCollection> {

	@Override
	public GeometryCollection toGeometry(com.vividsolutions.jts.geom.GeometryCollection src) {
		return GeometryCollection.of(transform(JtsGeometryCollectionIterable.of(src), fromJtsGeometryCollectionFn()));
	}

	@Override
	public com.vividsolutions.jts.geom.GeometryCollection fromGeometry(GeometryCollection src) {
		return null;
	}

}
