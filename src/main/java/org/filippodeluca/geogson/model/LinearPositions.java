package org.filippodeluca.geogson.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearPositions implements Positions {

    private ImmutableList<Position> positions;

    public LinearPositions(ImmutableList<Position> positions) {
        this.positions = positions;
    }

    public ImmutableList<Position> getPositions() {
        return positions;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {
            SinglePosition that = (SinglePosition) other;
            return new LinearPositions(ImmutableList.<Position>builder().addAll(positions).add(that.getPosition()).build());
        } else if(other instanceof LinearPositions) {
            LinearPositions that = (LinearPositions) other;

            return new AreaPositions(ImmutableList.of(positions, that.positions));
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
        final LinearPositions other = (LinearPositions) obj;
        return Objects.equal(this.positions, other.positions);
    }
}
