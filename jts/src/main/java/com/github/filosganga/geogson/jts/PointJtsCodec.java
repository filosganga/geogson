package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Optional;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PointJtsCodec implements JtsCodec {

    @Override
    public Optional<Geometry> toGeometry(com.vividsolutions.jts.geom.Geometry src) {
        if (src instanceof com.vividsolutions.jts.geom.Point) {
            return Optional.<Geometry>of(JtsConverter.fromJtsPoint((com.vividsolutions.jts.geom.Point) src));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<com.vividsolutions.jts.geom.Geometry> fromGeometry(Geometry src) {
        return Optional.absent();
    }
}
