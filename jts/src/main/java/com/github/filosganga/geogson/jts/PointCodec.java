package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Point;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PointCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.Point, Point> {

    @Override
    public Point toGeometry(com.vividsolutions.jts.geom.Point src) {
        return fromJtsPoint(src);
    }

    @Override
    public com.vividsolutions.jts.geom.Point fromGeometry(Point src) {
        return geometryFactory.createPoint(new Coordinate(src.lon(), src.lat()));
    }

}
