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

    public static Matcher<Positions> singlePositionsWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Positions>() {
            @Override
            protected boolean matchesSafely(Positions item) {
                return positionWithLonLat(lon, lat).matches(((SinglePosition)item).getPosition());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Single positions with position: ").appendDescriptionOf(positionWithLonLat(lon, lat));

            }
        };
    }


    public static Matcher<Position> positionWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Position>() {
            @Override
            protected boolean matchesSafely(Position item) {
                return item.getLon() == lon && item.getLat() == lat;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Position with lon: ")
                        .appendValue(lon)
                        .appendText(" and lat: ")
                        .appendValue(lat);
            }
        };
    }

    public static Matcher<Point> pointWithLonLat(final double lon, final double lat) {
        return pointThatHave(positionWithLonLat(lon, lat));
    }

    public static Matcher<Point> pointThatHave(final Matcher<Position> positionMatcher) {
        return new TypeSafeMatcher<Point>() {
            @Override
            protected boolean matchesSafely(Point item) {
                return positionMatcher.matches(item.getPosition());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Point with that have ").appendDescriptionOf(positionMatcher);
            }
        };
    }
}
