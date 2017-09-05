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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;
import java.util.Optional;

public final class Matchers {

    private Matchers() {
    }

    public static Matcher<Positions> singlePositionsWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<Positions>() {
            @Override
            protected boolean matchesSafely(Positions item) {
                return positionsWithLonLat(lon, lat).matches((item));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Single positions with position: ").appendDescriptionOf(positionsWithLonLat(lon, lat));

            }
        };
    }

    public static Matcher<Positions> singlePositionsWithLonLatAlt(final double lon, final double lat, final  double alt) {
        return new TypeSafeMatcher<Positions>() {
            @Override
            protected boolean matchesSafely(Positions item) {
                return positionWithLonLatAlt(lon, lat, alt).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Single positions with position: ").appendDescriptionOf(positionWithLonLatAlt(lon, lat, alt));

            }
        };
    }



    public static Matcher<SinglePosition> positionsWithLonLat(final double lon, final double lat) {
        return new TypeSafeMatcher<SinglePosition>() {
            @Override
            protected boolean matchesSafely(SinglePosition item) {
                return item.lon() == lon && item.lat() == lat;
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

    public static Matcher<SinglePosition> positionWithLonLatAlt(final double lon, final double lat, final double alt) {
        return new TypeSafeMatcher<SinglePosition>() {
            @Override
            protected boolean matchesSafely(SinglePosition item) {
                return item.lon() == lon && item.lat() == lat && item.alt() == alt;
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
        return pointThatHave(positionsWithLonLat(lon, lat));
    }

    public static Matcher<Point> pointWithLonLatAlt(final double lon, final double lat, final double alt) {
        return pointThatHave(positionWithLonLatAlt(lon, lat, alt));
    }

    public static Matcher<Point> pointThatHave(final Matcher<SinglePosition> positionMatcher) {
        return new TypeSafeMatcher<Point>() {
            @Override
            protected boolean matchesSafely(Point item) {
                return positionMatcher.matches(item.positions());
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

    public static <K,V> Matcher<Map<K, V>> emptyMap() {
        return new TypeSafeMatcher<Map<K, V>>() {
            @Override
            protected boolean matchesSafely(Map<K, V> kvMap) {
                return kvMap.isEmpty();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Empty Map");

            }
        };
    }
}
