package org.filippodeluca.geogson.model.positions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public abstract class AbstractPositions<T extends Positions> implements Positions {

    protected final ImmutableList<T> children;

    public AbstractPositions(ImmutableList<T> children) {
        this.children = children;
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
        return Objects.hashCode(getClass(), children);
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
        return Objects.equal(this.children, other.children);
    }
}
