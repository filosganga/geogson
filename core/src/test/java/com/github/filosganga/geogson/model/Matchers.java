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

package com.github.filosganga.geogson.model;

import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.common.base.Optional;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class Matchers {

    private Matchers() {
    }

    public static Matcher<Positions> singlePositionsWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Positions>() {
            @Override
            protected boolean matchesSafely(Positions item) {
                return positionWithLonLat(lon, lat).matches(((SinglePosition) item).coordinates());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Single positions with position: ").appendDescriptionOf(positionWithLonLat(lon, lat));

            }
        };
    }

    public static Matcher<Positions> singlePositionsWithLonLatAlt(final double lon, final double lat, final  double alt) {
        return new TypeSafeMatcher<Positions>() {
            @Override
            protected boolean matchesSafely(Positions item) {
                return positionWithLonLatAlt(lon, lat, alt).matches(((SinglePosition) item).coordinates());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Single positions with position: ").appendDescriptionOf(positionWithLonLatAlt(lon, lat, alt));

            }
        };
    }


    public static Matcher<Coordinates> positionWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Coordinates>() {
            @Override
            protected boolean matchesSafely(Coordinates item) {
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

    public static Matcher<Coordinates> positionWithLonLatAlt(final double lon, final double lat, final double alt) {
        return new TypeSafeMatcher<Coordinates>() {
            @Override
            protected boolean matchesSafely(Coordinates item) {
                return item.getLon() == lon && item.getLat() == lat && item.getAlt() == alt;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Position with lon: ")
                        .appendValue(lon)
                        .appendText(" and lat: ")
                        .appendValue(lat)
                        .appendText(" and alt: ")
                        .appendValue(alt);
            }
        };
    }

    public static Matcher<Point> pointWithLonLat(final double lon, final double lat) {
        return pointThatHave(positionWithLonLat(lon, lat));
    }

    public static Matcher<Point> pointThatHave(final Matcher<Coordinates> positionMatcher) {
        return new TypeSafeMatcher<Point>() {
            @Override
            protected boolean matchesSafely(Point item) {
                return positionMatcher.matches(item.coordinates());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Point with that have ").appendDescriptionOf(positionMatcher);
            }
        };
    }

    public static <T> Matcher<Optional<? extends T>> optionalThatContain(final Matcher<? extends T> matcher) {

        return new TypeSafeMatcher<Optional<? extends T>>() {
            @Override
            protected boolean matchesSafely(Optional<? extends T> item) {
                return item.isPresent() && matcher.matches(item.get());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Optional that contains ").appendDescriptionOf(matcher);
            }
        };

    }
}
