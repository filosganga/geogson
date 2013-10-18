package org.filippodeluca.geogson.model;

import static org.filippodeluca.geogson.model.Matchers.coordinateWithLonLat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void withLonShouldReturnNewInstance() {

        Coordinate one = Coordinate.of(10, 20);

        Coordinate two = one.withLon(15);

        assertThat(one, is(coordinateWithLonLat(10, 20)));
        assertThat(two, is(coordinateWithLonLat(15, 20)));
    }

    @Test
    public void withLatShouldReturnNewInstance() {

        Coordinate one = Coordinate.of(10, 20);

        Coordinate two = one.withLat(25);

        assertThat(one, is(coordinateWithLonLat(10, 20)));
        assertThat(two, is(coordinateWithLonLat(10, 25)));
    }

    @Test
    public void equalsAnHashCodeShouldDependOnValue() {

        Coordinate one = Coordinate.of(10, 20);
        Coordinate two = Coordinate.of(10, 20);

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
