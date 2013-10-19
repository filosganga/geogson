package com.github.filosganga.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.github.filosganga.geogson.model.positions.Positions;
import com.google.common.base.Function;
import com.google.common.base.Objects;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class AbstractGeometry<P extends Positions> implements Geometry<P>, Serializable {

    private final P positions;

    protected AbstractGeometry(P positions) {

        checkArgument(positions != null);

        this.positions = positions;
    }

    public static <P extends Positions> Function<AbstractGeometry<P>, P> positionsFn(Class<P> positionsClass) {
        return new Function<AbstractGeometry<P>, P>() {
            @Override
            public P apply(AbstractGeometry<P> input) {
                return input.positions();
            }
        };
    }

    @Override
    public P positions() {
        return positions;
    }

    @Override
    public int size() {
        return positions.size();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractGeometry other = (AbstractGeometry) obj;
        return Objects.equal(this.positions, other.positions);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("positions", positions)
                .toString();
    }

}
