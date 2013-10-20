package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Polygon;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PolygonCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.Polygon, Polygon> {

    @Override
    public Polygon toGeometry(com.vividsolutions.jts.geom.Polygon src) {
        return fromJtsPolygon(src);
    }

    @Override
    public com.vividsolutions.jts.geom.Polygon fromGeometry(Polygon src) {
        return toJtsPolygon(src);
    }
}
