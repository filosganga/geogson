package org.filippodeluca.geogson.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class MultiDimensionalPositions implements Positions {

    ImmutableList<ImmutableList<ImmutableList<Position>>> positions;

    public MultiDimensionalPositions(ImmutableList<ImmutableList<ImmutableList<Position>>> positions) {
        this.positions = positions;
    }

    public ImmutableList<ImmutableList<ImmutableList<Position>>> getPositions() {
        return positions;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and multidimensionl positions");
        } else if(other instanceof LinearPositions) {

            throw new IllegalArgumentException("Cannot merge linear position and multidimensionl positions");
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.<ImmutableList<ImmutableList<Position>>>builder().addAll(positions).add(that.getPositions()).build());
        } else {

            throw new RuntimeException("Cannot merge wtih: " + other);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MultiDimensionalPositions other = (MultiDimensionalPositions) obj;
        return Objects.equal(this.positions, other.positions);
    }
}
