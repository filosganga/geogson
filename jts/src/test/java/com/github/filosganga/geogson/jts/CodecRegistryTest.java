package com.github.filosganga.geogson.jts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import com.github.filosganga.geogson.codec.CodecRegistry;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class CodecRegistryTest {

    @Test
    public void testAddCodec() throws Exception {
        CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>> codecs = new CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>>();
        codecs.addCodec(new PointCodec(new GeometryFactory()));

        com.vividsolutions.jts.geom.Geometry value = codecs.fromGeometry(Point.from(12.6, 43.6));

        assertThat(value, instanceOf(com.vividsolutions.jts.geom.Point.class));
    }
}
