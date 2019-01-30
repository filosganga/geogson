package com.github.filosganga.geogson.jts;

import com.github.filosganga.geogson.codec.CodecRegistry;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class CodecRegistryTest {

    @Test
    public void testAddCodec() throws Exception {
        CodecRegistry<org.locationtech.jts.geom.Geometry, Geometry<?>> codecs = new CodecRegistry<org.locationtech.jts.geom.Geometry, Geometry<?>>();
        codecs.addCodec(new PointCodec(new GeometryFactory()));

        org.locationtech.jts.geom.Geometry value = codecs.fromGeometry(Point.from(12.6, 43.6));

        assertThat(value, instanceOf(org.locationtech.jts.geom.Point.class));
    }
}
