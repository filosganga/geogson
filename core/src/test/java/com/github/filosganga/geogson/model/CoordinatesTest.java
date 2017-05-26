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

import static com.github.filosganga.geogson.model.Matchers.positionWithLonLat;
import static com.github.filosganga.geogson.model.Matchers.positionWithLonLatAlt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class CoordinatesTest {

    @Test
    public void constructorWithBigLimitLonLatShouldReturnCoordinate() {

        assertThat(Coordinates.of(180, 90), is(positionWithLonLat(180, 90)));
    }

    @Test
    public void constructorWithSmallLimitLonLatShouldReturnCoordinate() {

        assertThat(Coordinates.of(-180, -90), is(positionWithLonLat(-180, -90)));
    }

    @Test
    public void withLonShouldReturnNewInstance() {

        Coordinates one = Coordinates.of(10, 20);

        Coordinates two = one.withLon(15);

        assertThat(one, is(positionWithLonLat(10, 20)));
        assertThat(two, is(positionWithLonLat(15, 20)));
    }

    @Test
    public void withLatShouldReturnNewInstance() {

        Coordinates one = Coordinates.of(10, 20);

        Coordinates two = one.withLat(25);

        assertThat(one, is(positionWithLonLat(10, 20)));
        assertThat(two, is(positionWithLonLat(10, 25)));
    }

    @Test
    public void withAltShouldReturnNewInstance() {

        Coordinates one = Coordinates.of(10, 20, 30);

        Coordinates two = one.withAlt(25);

        assertThat(one, is(positionWithLonLatAlt(10, 20, 30)));
        assertThat(two, is(positionWithLonLatAlt(10, 20, 25)));
    }

    @Test
    public void equalsAnHashCodeShouldDependOnValue() {

        Coordinates base = Coordinates.of(10, 20);
        Coordinates same = Coordinates.of(10, 20);

        Coordinates different = Coordinates.of(10, 21);


        assertThat(base.equals(null), is(false));
        assertThat(base.equals(base), is(true));

        assertThat(ImmutableSet.of(base, same, different).size(), is(2));

    }


}
