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

import static org.filippodeluca.geogson.model.Matchers.positionWithLonLat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class PositionTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooBigLonShouldRaiseException() {

        Position.of(181, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooSmallLonShouldRaiseException() {

        Position.of(-181, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooBigLatShouldRaiseException() {

        Position.of(0, 91);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithTooSmallLatShouldRaiseException() {

        Position.of(0, -91);
    }

    @Test
    public void constructorWithBigLimitLonLatShouldReturnCoordinate() {

        assertThat(Position.of(180, 90), is(positionWithLonLat(180, 90)));
    }

    @Test
    public void constructorWithSmallLimitLonLatShouldReturnCoordinate() {

        assertThat(Position.of(-180, -90), is(positionWithLonLat(-180, -90)));
    }

    @Test
    public void withLonShouldReturnNewInstance() {

        Position one = Position.of(10, 20);

        Position two = one.withLon(15);

        assertThat(one, is(positionWithLonLat(10, 20)));
        assertThat(two, is(positionWithLonLat(15, 20)));
    }

    @Test
    public void withLatShouldReturnNewInstance() {

        Position one = Position.of(10, 20);

        Position two = one.withLat(25);

        assertThat(one, is(positionWithLonLat(10, 20)));
        assertThat(two, is(positionWithLonLat(10, 25)));
    }

    @Test
    public void equalsAnHashCodeShouldDependOnValue() {

        Position one = Position.of(10, 20);
        Position two = Position.of(10, 20);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

        one = one.withLat(25);

        assertThat(one, not(equalTo(two)));
        assertThat(one.hashCode(), not(equalTo(two.hashCode())));

        two = two.withLat(25);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

        one = one.withLon(15);

        assertThat(one, not(equalTo(two)));
        assertThat(one.hashCode(), not(equalTo(two.hashCode())));

        two = two.withLon(15);

        assertThat(one, equalTo(two));
        assertThat(one.hashCode(), equalTo(two.hashCode()));

    }


}
