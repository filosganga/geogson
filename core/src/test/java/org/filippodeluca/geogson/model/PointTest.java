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

package org.filippodeluca.geogson.model;

import static org.filippodeluca.geogson.model.Matchers.pointThatHave;
import static org.filippodeluca.geogson.model.Matchers.pointWithLonLat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PointTest {

    @Test
    public void ofLonLatShouldReturnRightValues() {

        assertThat(Point.of(10, 20), is(pointWithLonLat(10, 20)));
    }

    @Test
    public void ofCoordinateShouldReturnRightValues() {

        Position position = Position.of(-10, 60);

        assertThat(Point.of(position), is(pointThatHave(is(position))));
    }

    @Test
    public void withLonShouldReturnRightValues() {

        assertThat(Point.of(10, 20).withLon(15), is(pointWithLonLat(15, 20)));
    }

    @Test
    public void withLatShouldReturnRightValues() {

        assertThat(Point.of(10, 20).withLat(25), is(pointWithLonLat(10, 25)));
    }

    @Test
    public void equalsHashCodeShouldDependByCoordinate() {

        Point one = Point.of(10, 20);
        Point two = Point.of(10, 20);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

        one = one.withLon(15);

        assertThat(one, not(equalTo(two)));
        assertThat(one.hashCode(), not(equalTo(two.hashCode())));

        two = two.withLon(15);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

        one = one.withLat(25);

        assertThat(one, not(equalTo(two)));
        assertThat(one.hashCode(), not(equalTo(two.hashCode())));

        two = two.withLat(25);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

    }

}
