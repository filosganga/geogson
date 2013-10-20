package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.model.LineString;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.LineString, LineString> {

    @Override
    public LineString toGeometry(com.vividsolutions.jts.geom.LineString src) {
        return fromJtsLineString(src);
    }

    @Override
    public com.vividsolutions.jts.geom.LineString fromGeometry(LineString src) {
        return toJtsLineString(src);
    }
}
