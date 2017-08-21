package com.github.filosganga.geogson.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import com.github.filosganga.geogson.model.positions.Positions;

import static com.github.filosganga.geogson.util.Preconditions.checkArgument;


/**
 * Abstract implementation of {@link Geometry} providing generic methods.
 */
public abstract class AbstractGeometry<P extends Positions> implements Geometry<P>, Serializable {

    private static final long serialVersionUID = 1L;

    private final P positions;

    private Optional<Integer> cachedHashCode = Optional.empty();

    protected AbstractGeometry(P positions) {
        this.positions =  checkArgument(positions, Objects::nonNull, "Postitions is mandatory");
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
        if(!cachedHashCode.isPresent()) {
            cachedHashCode = Optional.of(Objects.hash(getClass(), positions));
        }

        return cachedHashCode.get();
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
        return getClass().getSimpleName() + "{type: " + type() + ", positions: " + Objects.toString(positions) + "}";
    }

}
