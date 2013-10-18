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

        Coordinate coordinate = Coordinate.of(-10, 60);

        assertThat(Point.of(coordinate), is(pointThatHave(is(coordinate))));
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
