/*
 * Copyright 2013 Filippo De Luca - me@filippodeluca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.filippodeluca.geogson.model.positions;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public class LinearPositions implements Positions {

    private ImmutableList<SinglePosition> children;

    public LinearPositions(ImmutableList<SinglePosition> children) {
        this.children = children;
    }

    public LinearPositions(Iterable<SinglePosition> children) {
        this(ImmutableList.copyOf(children));
    }

    @Override
    public Positions merge(Positions other) {
        if(other instanceof SinglePosition) {

            SinglePosition that = (SinglePosition) other;
            return new LinearPositions(ImmutableList.<SinglePosition>builder().addAll(children).add(that).build());
        } else if(other instanceof LinearPositions) {
            LinearPositions that = (LinearPositions) other;

            return new AreaPositions(ImmutableList.<LinearPositions>builder().add(this).add(that).build());
        } else {
            return other.merge(this);
        }
    }

    @Override
    public Iterable<SinglePosition> children() {
        return children;
    }

    public int getSize() {
        return children.size();
    }

    public boolean isClosed() {
        return getSize() >= 4 && getLast(children).equals(getFirst(children, null));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(LinearPositions.class, children);
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
        return Objects.equal(this.children, other.children);
    }
}
