package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Optional;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public interface JtsCodec {

    Optional<Geometry> toGeometry(com.vividsolutions.jts.geom.Geometry src);

    Optional<com.vividsolutions.jts.geom.Geometry> fromGeometry(Geometry src);
}
