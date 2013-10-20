package com.github.filosganga.geogson.spatial4j;

import static com.github.filosganga.geogson.util.Classes.cast;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import com.google.common.base.Optional;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.impl.PointImpl;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PointCodec implements ShapeCodec<Point> {

    private final SpatialContext sc = SpatialContext.GEO;

    @Override
    public Optional<Point> toGeometry(Shape src) {

        Optional<Point> toReturn = Optional.absent();

        if(src instanceof com.spatial4j.core.shape.Point) {
            com.spatial4j.core.shape.Point p = (com.spatial4j.core.shape.Point)src;

            toReturn = Optional.of(Point.from(p.getX(), p.getY()));
        }

        return toReturn;
    }

    @Override
    public Optional<Shape> fromGeometry(Point src) {

        Optional<Shape> toReturn = Optional.absent();

        if(src.type() == Geometry.Type.POINT) {
            Point p = cast(src);

            toReturn = Optional.<Shape>of(new PointImpl(p.lon(), p.lat(), sc));

        }

        return toReturn;
    }
}
