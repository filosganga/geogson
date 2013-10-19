package com.github.filosganga.geogson.jts;

import com.google.common.base.Optional;
import org.filippodeluca.geogson.model.Geometry;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public interface JtsCodec {

    Optional<Geometry> toGeometry(com.vividsolutions.jts.geom.Geometry src);

    Optional<com.vividsolutions.jts.geom.Geometry> fromGeometry(Geometry src);
}
