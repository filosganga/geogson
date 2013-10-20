package com.github.filosganga.geogson.jts;

import static com.google.common.collect.Iterables.transform;

import com.github.filosganga.geogson.model.MultiLineString;
import com.google.common.collect.FluentIterable;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiLineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiLineString, MultiLineString> {

    @Override
    public MultiLineString toGeometry(com.vividsolutions.jts.geom.MultiLineString src) {
        return MultiLineString.of(transform(JtsLineStringIterable.of(src), fromJtsLineStringFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiLineString fromGeometry(MultiLineString src) {
        return geometryFactory.createMultiLineString(
                FluentIterable.from(src.lineStrings())
                        .transform(toJtsLineStringFn())
                        .toArray(com.vividsolutions.jts.geom.LineString.class)
        );
    }
}
