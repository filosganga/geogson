package org.filippodeluca.geogson.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class SinglePosition implements Positions {

    private final Position position;

    public SinglePosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {
            Position that = ((SinglePosition)other).getPosition();
            return new LinearPositions(ImmutableList.of(position, that));
        } else {
            return other.merge(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SinglePosition other = (SinglePosition) obj;
        return Objects.equal(this.position, other.position);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("position", position)
                .toString();
    }
}
