package com.github.filosganga.geogson.jts;

import static com.github.filosganga.geogson.util.Optionals.isPresentPr;

import com.github.filosganga.geogson.model.Geometry;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class ChainedJtsCodec implements JtsCodec {

    private final Iterable<JtsCodec> delegates;

    public ChainedJtsCodec(Iterable<JtsCodec> delegates) {
        this.delegates = delegates;
    }

    @Override
    public Optional<Geometry> toGeometry(final com.vividsolutions.jts.geom.Geometry src) {

        return FluentIterable.from(delegates)
                .transform(toMayGeometryFn(src))
                .firstMatch(isPresentPr())
                .or(Optional.<Geometry>absent());

    }

    @Override
    public Optional<com.vividsolutions.jts.geom.Geometry> fromGeometry(Geometry src) {
        return FluentIterable.from(delegates)
                .transform(fromMayGeometryFn(src))
                .firstMatch(isPresentPr())
                .or(Optional.<com.vividsolutions.jts.geom.Geometry>absent());
    }

    public Function<JtsCodec, Optional<Geometry>> toMayGeometryFn(final com.vividsolutions.jts.geom.Geometry src) {
        return new Function<JtsCodec, Optional<Geometry>>() {
            @Override
            public Optional<Geometry> apply(JtsCodec input) {
                return input.toGeometry(src);
            }
        };
    }

    public Function<JtsCodec, Optional<com.vividsolutions.jts.geom.Geometry>> fromMayGeometryFn(final Geometry src) {
        return new Function<JtsCodec, Optional<com.vividsolutions.jts.geom.Geometry>>() {
            @Override
            public Optional<com.vividsolutions.jts.geom.Geometry> apply(JtsCodec input) {
                return input.fromGeometry(src);
            }
        };
    }
}
