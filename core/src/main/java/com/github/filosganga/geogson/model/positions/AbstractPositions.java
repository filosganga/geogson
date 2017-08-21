package com.github.filosganga.geogson.model.positions;

import com.github.filosganga.geogson.util.Iterables;

import java.util.Objects;
import java.util.Optional;

import static com.github.filosganga.geogson.util.Preconditions.checkArgument;

/**
 * Abstract implementation of {@link Positions}. Provides some basic methods.
 */
public abstract class AbstractPositions<T extends Positions> implements Positions {

    private static final long serialVersionUID = 1L;

    protected final Iterable<T> children;

    private Optional<Integer> cachedSize = Optional.empty();
    private Optional<Integer> cachedHashCode = Optional.empty();

    AbstractPositions(Iterable<T> children) {
        this.children = checkArgument(children, Objects::nonNull, "The children cannot be null");
    }

    @Override
    public Iterable<T> children() {
        return Iterables.unmodifiableIterable(children);
    }

    @Override
    public int size() {
        if(!cachedSize.isPresent()) {
            cachedSize = Optional.of(Iterables.size(children));
        }
        return cachedSize.get();
    }

    @Override
    public int hashCode() {
        if(!cachedHashCode.isPresent()) {
            cachedHashCode = Optional.of(Objects.hash(getClass(), children));
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
        final AbstractPositions other = (AbstractPositions) obj;
        return Objects.equals(this.children, other.children);
    }
}
