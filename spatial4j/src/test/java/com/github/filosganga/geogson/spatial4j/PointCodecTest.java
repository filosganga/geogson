package com.github.filosganga.geogson.spatial4j;

import static com.github.filosganga.geogson.model.Matchers.optionalThatContain;
import static com.github.filosganga.geogson.model.Matchers.pointWithLonLat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Matchers;
import com.github.filosganga.geogson.model.Point;
import com.google.common.base.Optional;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.impl.PointImpl;
import com.spatial4j.core.shape.jts.JtsPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PointCodecTest {

    private PointCodec toTest;

    @Before
    public void initToTest() {
        toTest = new PointCodec();
    }

    @Test
    public void toGeometryShouldConvertPoint() {

        PointImpl src = new PointImpl(12.5, 43.5, SpatialContext.GEO);

        Optional<Point> value = toTest.toGeometry(src);

        assertThat(value, is(optionalThatContain(pointWithLonLat(12.5, 43.5))));
    }

    @Test
    public void toGeometryShouldConvertJtsPoint() {

        com.vividsolutions.jts.geom.Point point = new GeometryFactory().createPoint(new Coordinate(12.5, -34.8));
        JtsPoint src = new JtsPoint(point, SpatialContext.GEO);

        Optional<Point> value = toTest.toGeometry(src);

        assertThat(value, is(optionalThatContain(pointWithLonLat(12.5, -34.8))));
    }
}
