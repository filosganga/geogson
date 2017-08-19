package com.github.filosganga.geogson.model.positions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Abstract implementation of {@link Positions}. Provides some basic methods.
 */
public abstract class AbstractPositions<T extends Positions> implements Positions {

    private static final long serialVersionUID = 1L;

    protected final Iterable<T> children;

    protected AbstractPositions(Iterable<T> children) {
        this.children = checkNotNull(children, "The children cannot be null");
    }

    @Override
    public Iterable<T> children() {
        return Iterables.unmodifiableIterable(children);
    }

    @Override
    public int size() {
        return Iterables.size(children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), children);
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
