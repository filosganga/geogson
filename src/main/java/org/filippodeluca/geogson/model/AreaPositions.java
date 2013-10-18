package org.filippodeluca.geogson.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class AreaPositions implements Positions {

    private final ImmutableList<ImmutableList<Position>> positions;

    public AreaPositions(ImmutableList<ImmutableList<Position>> positions) {
        this.positions = positions;
    }

    public ImmutableList<ImmutableList<Position>> getPositions() {
        return positions;
    }

    @Override
    public Positions merge(Positions other) {

        if(other instanceof SinglePosition) {

            throw new IllegalArgumentException("Cannot merge single position and area positions");
        } else if(other instanceof LinearPositions) {

            LinearPositions that = (LinearPositions) other;
            return new AreaPositions(ImmutableList.<ImmutableList<Position>>builder().addAll(positions).add(that.getPositions()).build());
        } else if (other instanceof AreaPositions) {

            AreaPositions that = (AreaPositions) other;
            return new MultiDimensionalPositions(ImmutableList.of(positions, that.getPositions()));
        } else {

            return other.merge(this);
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
        final AreaPositions other = (AreaPositions) obj;
        return Objects.equal(this.positions, other.positions);
    }
}
