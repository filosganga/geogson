package com.github.filosganga.geogson.model.positions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.collect.ImmutableList;

/**
 * Abstract implementation of {@link Positions}. Provides some basic methods.
 */
public abstract class AbstractPositions<T extends Positions> implements Positions {

    private static final long serialVersionUID = 1L;

    protected final ImmutableList<T> children;

    protected AbstractPositions(ImmutableList<T> children) {
        this.children = checkNotNull(children, "The children cannot be null");
    }

    @Override
    public Iterable<T> children() {
        return children;
    }

    @Override
    public int size() {
        return children.size();
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
