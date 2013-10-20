package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Optional;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public interface JtsCodec<S extends Object, T extends Geometry<?>> {

    T toGeometry(S src);

    S fromGeometry(T src);
}
