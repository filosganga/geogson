package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link org.locationtech.jts.geom.Point} and
 * {@link Point}.
 */
public class PointCodec extends AbstractJtsCodec<org.locationtech.jts.geom.Point, Point> {

    /**
     * Create a codec for a {@link org.locationtech.jts.geom.Point JTS
     * Point} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public PointCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public Point toGeometry(org.locationtech.jts.geom.Point src) {
        return fromJtsPoint(src);
    }

    @Override
    public org.locationtech.jts.geom.Point fromGeometry(Point src) {
        return this.geometryFactory.createPoint(new Coordinate(src.lon(), src.lat()));
    }

}
