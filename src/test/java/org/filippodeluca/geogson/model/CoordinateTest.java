package org.filippodeluca.geogson.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class CoordinateTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooBigLonShouldRaiseException() {

        Coordinate.of(181, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooSmallLonShouldRaiseException() {

        Coordinate.of(-181, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooBigLatShouldRaiseException() {

        Coordinate.of(0, 91);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooSmallLatShouldRaiseException() {

        Coordinate.of(0, -91);
    }

    @Test
    public void constructorWithBigLimitLonLatShouldReturnCoordinate() {

        assertThat(Coordinate.of(180, 90), is(coordinateWithLonLat(180, 90)));
    }

    @Test
    public void constructorWithSmallLimitLonLatShouldReturnCoordinate() {

        assertThat(Coordinate.of(-180, -90), is(coordinateWithLonLat(-180, -90)));
    }

    public static Matcher<Coordinate> coordinateWithLon(final double lon) {
        return new TypeSafeMatcher<Coordinate>() {
            @Override
            protected boolean matchesSafely(Coordinate item) {
                return item.getLon() == lon;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Coordinate with lon: ").appendValue(lon);
            }
        };
    }

    public static Matcher<Coordinate> coordinateWithLat(final double lat) {
        return new TypeSafeMatcher<Coordinate>() {
            @Override
            protected boolean matchesSafely(Coordinate item) {
                return item.getLat() == lat;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Coordinate with lat: ").appendValue(lat);
            }
        };
    }

    public static Matcher<Coordinate> coordinateWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Coordinate>() {
            @Override
            protected boolean matchesSafely(Coordinate item) {
                return item.getLon() == lon && item.getLat() == lat;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Coordinate with lon: ")
                        .appendValue(lon)
                        .appendText(" and lat: ")
                        .appendValue(lat);
            }
        };
    }
}
