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

package com.github.filosganga.geogson.gson;

import static com.github.filosganga.geogson.model.Matchers.singlePositionsWithLonLat;
import static com.github.filosganga.geogson.model.Matchers.singlePositionsWithLonLatAlt;
import static com.google.common.collect.Iterables.toArray;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.positions.AreaPositions;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.MultiDimensionalPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

public class PositionsAdapterTest {

  private Gson toTest;

  @Before
  public void initToTest() {
    toTest = new GsonBuilder().registerTypeAdapter(Positions.class, new PositionsAdapter()).create();
  }

  @Test
  public void readShouldReadSinglePosition() throws Exception {

    Positions positions = toTest.fromJson(givenPositionJson(12.5, 45.8), Positions.class);

    assertThat(positions, is(singlePositionsWithLonLat(12.5, 45.8)));

    assertThat(toTest.toJson(positions, Positions.class), is("[12.5,45.8]"));
  }

  @Test
  public void readShouldReadSinglePositionWithAlt() throws Exception {

    Positions positions = toTest.fromJson("[12.5,45.8,56.8]", Positions.class);

    assertThat(positions, is(singlePositionsWithLonLatAlt(12.5, 45.8, 56.8)));

    assertThat(toTest.toJson(positions, Positions.class), is("[12.5,45.8,56.8]"));
  }

  @Test
  public void readShouldReadLinearPositions() throws Exception {

    Positions positions = toTest.fromJson(givenLinearPositionsJson(Coordinates.of(12.5, 45.8), Coordinates.of(13.5, 33.7)), Positions.class);

    assertThat(positions, instanceOf(LinearPositions.class));
  }

  @Test
  public void readShouldReadAreaPositions() throws Exception {

    Positions positions = toTest.fromJson(
            givenAreaPositionsJson(
                    asList(Coordinates.of(12.5, 45.8), Coordinates.of(13.5, 33.7)),
                    asList(Coordinates.of(22.5, 45.8), Coordinates.of(23.5, 33.7)),
                    asList(Coordinates.of(32.5, 45.8), Coordinates.of(33.5, 33.7))
            ),
            Positions.class
    );

    assertThat(positions, instanceOf(AreaPositions.class));
  }

  @Test
  public void readShouldReadMultiDimensionalPositions() throws Exception {

    Positions positions = toTest.fromJson(
            givenMultiDimensionalPositionsJson(
                    asList(
                            asList(Coordinates.of(12.5, 45.8), Coordinates.of(13.5, 33.7)),
                            asList(Coordinates.of(22.5, 45.8), Coordinates.of(23.5, 33.7)),
                            asList(Coordinates.of(32.5, 45.8), Coordinates.of(33.5, 33.7))
                    ),
                    asList(
                            asList(Coordinates.of(13.5, 45.8), Coordinates.of(13.5, 53.7)),
                            asList(Coordinates.of(24.5, 45.8), Coordinates.of(23.5, 53.7)),
                            asList(Coordinates.of(35.5, 45.8), Coordinates.of(33.5, 53.7))
                    )
            ),
            Positions.class
    );

    assertThat(positions, instanceOf(MultiDimensionalPositions.class));
  }

  @Test
  public void readWithEmptyJsonShouldReturnNull() throws Exception {

    assertThat(toTest.fromJson("", Coordinates.class), is(nullValue()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void readWithInvalidJsonShouldRaiseException() throws Exception {

    toTest.fromJson("123", Positions.class);
  }

  protected String givenMultiDimensionalPositionsJson(Iterable<? extends Iterable<Coordinates>>... areas) {

    return "[" + Joiner.on(',').join(FluentIterable.from(asList(areas)).transform(new Function<Iterable<? extends Iterable<Coordinates>>, String>() {
      @Override
      public String apply(Iterable<? extends Iterable<Coordinates>> input) {
        return givenAreaPositionsJson(toArray(input, Iterable.class));
      }
    })) + "]";
  }

  protected String givenAreaPositionsJson(Iterable<Coordinates>... lines) {

    return "[" + Joiner.on(',').join(FluentIterable.from(asList(lines)).transform(new Function<Iterable<Coordinates>, String>() {
      @Override
      public String apply(Iterable<Coordinates> input) {
        return givenLinearPositionsJson(toArray(input, Coordinates.class));
      }
    })) + "]";
  }

  protected String givenLinearPositionsJson(Coordinates... coordinates) {

    return "[" + Joiner.on(',').join(FluentIterable.from(asList(coordinates)).transform(new Function<Coordinates, String>() {
      @Override
      public String apply(Coordinates input) {
        return givenPositionJson(input.getLon(), input.getLat());
      }
    })) + "]";
  }

  protected String givenPositionJson(double lon, double lat) {

    return "[" + lon + "," + lat + "]";
  }

}
