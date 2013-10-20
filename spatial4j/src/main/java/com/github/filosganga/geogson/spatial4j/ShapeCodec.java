package com.github.filosganga.geogson.spatial4j;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Optional;
import com.spatial4j.core.shape.Shape;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public interface ShapeCodec<T extends Geometry<?>> {

    Optional<T> toGeometry(Shape src);

    Optional<Shape> fromGeometry(T src);
}
