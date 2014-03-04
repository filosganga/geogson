package com.github.filosganga.geogson.gson;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.MultiPoint;
import com.github.filosganga.geogson.model.MultiPolygon;
import com.github.filosganga.geogson.model.Point;
import com.github.filosganga.geogson.model.Polygon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class GeometryAdapterFactoryTest {

    private Gson toTest;

    @Before
    public void initToTest() {
        toTest = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
    }

    @Test
    public void shouldHandlePoint() {

        Point point = Point.from(56.7, 83.6);

        Point parsed = toTest.fromJson(toTest.toJson(point), Point.class);

        assertThat(parsed, equalTo(point));
    }

    @Test
    public void shouldHandleMultiPoint() {

        MultiPoint source = MultiPoint.of(Point.from(12.3, 45.3), Point.from(43.9, 5.8));

        MultiPoint parsed = toTest.fromJson(toTest.toJson(source), MultiPoint.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleLineString() {

        LineString source = LineString.of(Point.from(12.3, 45.3), Point.from(43.9, 5.8));

        LineString parsed = toTest.fromJson(toTest.toJson(source), LineString.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleLinearRing() {

        LinearRing source = LinearRing.of(Point.from(12.3, 45.3), Point.from(43.9, 5.8), Point.from(43.9, 5.8), Point.from(12.3, 45.3));

        LinearRing parsed = toTest.fromJson(toTest.toJson(source), LinearRing.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandlePolygon() {

        Polygon source = Polygon.of(
                LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3)),
                LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3))
        );

        Polygon parsed = toTest.fromJson(toTest.toJson(source), Polygon.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleMultiPolygon() {

        MultiPolygon source = MultiPolygon.of(
                Polygon.of(
                        LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3)),
                        LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3))
                ),
                Polygon.of(
                        LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3)),
                        LinearRing.of(Point.from(120.3, 45.3), Point.from(100, -50.8), Point.from(100, 5.8), Point.from(120.3, 45.3))
                )
        );

        MultiPolygon parsed = toTest.fromJson(toTest.toJson(source), MultiPolygon.class);

        assertThat(parsed, equalTo(source));
    }

}
