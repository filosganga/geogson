package org.filippodeluca.geogson.model;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public final class Matchers {

    private Matchers() {
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

    public static Matcher<Point> pointWithLonLat(final double lon, final double lat) {
        return pointThatHave(coordinateWithLonLat(lon, lat));
    }

    public static Matcher<Point> pointThatHave(final Matcher<Coordinate> coordinateMatcher) {
        return new TypeSafeMatcher<Point>() {
            @Override
            protected boolean matchesSafely(Point item) {
                return coordinateMatcher.matches(item.getCoordinate());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Point with that have ").appendDescriptionOf(coordinateMatcher);
            }
        };
    }
}
