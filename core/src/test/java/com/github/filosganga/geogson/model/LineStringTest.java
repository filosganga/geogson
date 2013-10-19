package com.github.filosganga.geogson.model;

import static java.util.Arrays.asList;

import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
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
