package org.filippodeluca.geogson.model;

import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class Polygon extends Geometry {

    public static class Builder {

        private ImmutableList.Builder<ImmutableList<Position>> coordinates;

        public Builder(Iterable<Position> perimeter) {
            this.coordinates = ImmutableList.<ImmutableList<Position>>builder().add(ImmutableList.copyOf(perimeter));
        }

        public Builder withHole(Iterable<Position> hole) {
            coordinates.add(ImmutableList.copyOf(hole));
            return this;
        }

        public Polygon build() {
            return new Polygon(this);
        }
    }

    private final AreaPositions coordinates;

    public Polygon(Builder builder) {
        this(new AreaPositions(
                builder.coordinates.build()
        ));
    }

    public Polygon(AreaPositions coordinates) {
        this.coordinates = coordinates;
    }

    public static Polygon of(Position...perimeter) {
        return of(asList(perimeter));
    }

    public static Polygon of(Iterable<Position> perimeter) {
        return new Builder(perimeter).build();
    }

    public static Polygon of(Iterable<Position> perimeter, Iterable<Position>...holes) {
        Builder builder = new Builder(perimeter);
        for(Iterable<Position> hole : holes) {
            builder.withHole(hole);
        }
        return builder.build();
    }

    @Override
    public Type getType() {
        return Type.POLYGON;
    }

    @Override
    public AreaPositions getPositions() {
        return coordinates;
    }
}
