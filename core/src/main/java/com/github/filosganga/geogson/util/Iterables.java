package com.github.filosganga.geogson.util;

import java.util.Collection;
import java.util.Iterator;

import static com.github.filosganga.geogson.util.Preconditions.checkNotNull;

public class Iterables {

    public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
        checkNotNull(iterable);
        return (Iterable)(!(iterable instanceof UnmodifiableIterable) ? new UnmodifiableIterable(iterable) : iterable);
    }

    public static int size(Iterable<?> iterable) {
        return iterable instanceof Collection ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
    }

    private static final class UnmodifiableIterable<T> implements Iterable<T> {
        private final Iterable<T> iterable;

        private UnmodifiableIterable(Iterable<T> iterable) {
            this.iterable = iterable;
        }

        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        public String toString() {
            return this.iterable.toString();
        }
    }

}
