package com.github.filosganga.geogson.model;

import org.junit.Test;

import static java.util.Arrays.asList;

public class LineStringTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithLessThen2PointsShouldRaiseException() {

        LineString.of(asList(Point.from(1, 2)));
    }

    @Test
    public void constructorWith2PointsShouldBuild() {

        LineString.of(Point.from(1, 2), Point.from(5, 7));
    }
}
