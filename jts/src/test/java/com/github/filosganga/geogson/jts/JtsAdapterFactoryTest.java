/*
 * Copyright 2013 Filippo De Luca - me@filippodeluca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.filosganga.geogson.jts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Filippo De Luca - fdeluca@expedia.com
 */
public class JtsAdapterFactoryTest {

    private static final GeometryFactory gf = new GeometryFactory();

    private Gson toTest;

    @Before
    public void initToTest() {
        toTest = new GsonBuilder()
                .registerTypeAdapterFactory(new GeometryAdapterFactory())
                .registerTypeAdapterFactory(new JtsAdapterFactory())
                .create();
    }

    @Test
    public void shouldHandlePoint() {

        Point source = gf.createPoint(new Coordinate(56.7, 83.6));

        Point parsed = toTest.fromJson(toTest.toJson(source), Point.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleMultiPoint() {

        MultiPoint source = gf.createMultiPoint(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8)});

        MultiPoint parsed = toTest.fromJson(toTest.toJson(source), MultiPoint.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleLineString() {

        LineString source = gf.createLineString(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8)});

        LineString parsed = toTest.fromJson(toTest.toJson(source), LineString.class);

        assertThat(parsed, equalTo(source));
    }

    @Test
    public void shouldHandleLinearRing() {

        LinearRing source = gf.createLinearRing(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8), new Coordinate(43.9, 10), new Coordinate(56.7, 83.6)});

        LinearRing parsed = toTest.fromJson(toTest.toJson(source), LinearRing.class);

        assertThat(parsed, equalTo(source));
    }


    @Test
    public void shouldHandlePolygon() {

        Polygon source = gf.createPolygon(
                gf.createLinearRing(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8), new Coordinate(43.9, 10), new Coordinate(56.7, 83.6)}),
                new LinearRing[]{
                    gf.createLinearRing(new Coordinate[]{new Coordinate(46.7, 73.6), new Coordinate(33.9, 5.8), new Coordinate(33.9, 9), new Coordinate(46.7, 73.6)})
                }
        );

        Polygon parsed = toTest.fromJson(toTest.toJson(source), Polygon.class);

        assertThat(parsed, equalTo(source));
    }


    @Test
    public void shouldHandleMultiPolygon() {

        MultiPolygon source = gf.createMultiPolygon(new Polygon[]{
                gf.createPolygon(
                        gf.createLinearRing(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8), new Coordinate(43.9, 10), new Coordinate(56.7, 83.6)}),
                        new LinearRing[]{
                                gf.createLinearRing(new Coordinate[]{new Coordinate(46.7, 73.6), new Coordinate(33.9, 5.8), new Coordinate(33.9, 9), new Coordinate(46.7, 73.6)})
                        }
                ),
                gf.createPolygon(
                        gf.createLinearRing(new Coordinate[]{new Coordinate(56.7, 83.6), new Coordinate(43.9, 5.8), new Coordinate(43.9, 10), new Coordinate(56.7, 83.6)}),
                        new LinearRing[]{
                                gf.createLinearRing(new Coordinate[]{new Coordinate(46.7, 73.6), new Coordinate(33.9, 5.8), new Coordinate(33.9, 9), new Coordinate(46.7, 73.6)})
                        }
                )
        });

        MultiPolygon parsed = toTest.fromJson(toTest.toJson(source), MultiPolygon.class);

        assertThat(parsed, equalTo(source));
    }



}
