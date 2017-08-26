package com.github.filosganga.geogson.model.positions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.filosganga.geogson.util.Preconditions.checkArgument;

/**
 * Abstract implementation of {@link Positions}. Provides some basic methods.
 */
public abstract class AbstractPositions<T extends Positions> implements Positions {

    private static final long serialVersionUID = 1L;

    protected final List<T> children;

    private transient Integer cachedSize = null;
    private transient Integer cachedHashCode = null;

    public interface PositionsBuilder {

        static PositionsBuilder builderOf(Positions p) {
            if(p instanceof SinglePosition) {
                return LinearPositions.builder();
            } else if (p instanceof LinearPositions) {
                return AreaPositions.builder();
            } else if (p instanceof AreaPositions) {
                return MultiDimensionalPositions.builder();
            } else {
                throw new IllegalArgumentException("No builder can be supplied for Positions " + p);
            }
        }

        PositionsBuilder addChild(Positions p);

        Positions build();
    }

    AbstractPositions(List<T> children) {
        this.children = checkArgument(children, Objects::nonNull, "The children cannot be null");
    }

    @Override
    public List<T> children() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public int size() {
        if(cachedSize == null) {
            cachedSize = children.size();
        }
        return cachedSize;
    }

    @Override
    public int hashCode() {
        if(cachedHashCode == null) {
            cachedHashCode = Objects.hash(getClass(), children);
        }
        return cachedHashCode;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "children=" + children +
                '}';
    }
}
