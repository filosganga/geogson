package com.github.filosganga.geogson.util;

import java.util.Iterator;

public class Iterators {

    public static int size(Iterator<?> iterator) {
        int count;
        for(count = 0; iterator.hasNext(); ++count) {
            iterator.next();
        }

        return count;
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return iterator instanceof UnmodifiableIterator ? (UnmodifiableIterator)iterator : new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            public T next() {
                return iterator.next();
            }
        };
    }

    private static abstract class UnmodifiableIterator<E> implements Iterator<E> {
        protected UnmodifiableIterator() {
        }

        /** @deprecated */
        @Deprecated
        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
