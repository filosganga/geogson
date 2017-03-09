package com.github.filosganga.geogson.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.Objects;

import com.github.filosganga.geogson.model.positions.Positions;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;

/**
 * Abstract implementation of {@link Geometry} providing generic methods.
 */
public abstract class AbstractGeometry<P extends Positions> implements Geometry<P>, Serializable {

    private static final long serialVersionUID = 1L;

    private final P positions;

    protected AbstractGeometry(P positions) {

        checkArgument(positions != null);

        this.positions = positions;
    }

    /**
     * Guava {@link Function} extracting the Positions instance from an AbstractGeometry.
     *
     * @param <P> The Position type.
     * @param positionsClass The Position class.
     */
    public static <P extends Positions> Function<AbstractGeometry<P>, P> positionsFn(Class<P> positionsClass) {
        return new Function<AbstractGeometry<P>, P>() {
            @Override
            public P apply(AbstractGeometry<P> input) {
                return input.positions();
            }
        };
    }

    /**
     * Returns the underlying {@link Positions} instance.
     *
     * @return P instance.
     */
    @Override
    public P positions() {
        return positions;
    }

    /**
     * Returns the underlying {@link Positions} size.
     *
     * @return int
     */
    @Override
    public int size() {
        return positions.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), positions);
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
        return Objects.equals(this.positions, other.positions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("positions", positions)
                .toString();
    }

}
