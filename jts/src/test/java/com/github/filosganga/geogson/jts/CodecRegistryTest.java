package com.github.filosganga.geogson.jts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class CodecRegistryTest {

    @Test
    public void testAddCodec() throws Exception {
        CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>> codecs = new CodecRegistry<com.vividsolutions.jts.geom.Geometry, Geometry<?>>();
        codecs.addCodec(new PointJtsCodec());

        com.vividsolutions.jts.geom.Geometry value = codecs.fromGeometry(Point.from(12.6, 43.6));

        assertThat(value, instanceOf(com.vividsolutions.jts.geom.Point.class));
    }
}
