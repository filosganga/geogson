package org.filippodeluca.geogson.util;

import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.transform;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;


/**
 * @author Filippo De Luca - me@filippodeluca.com
 */
public final class Optionals {

    private Optionals() {
    }

    public static <T> Optional<T> flatten(Optional<Optional<T>> mayMayT) {
        return mayMayT.or(Optional.<T>absent());
    }

    public static <S, T> Optional<T> flatTransform(Optional<S> src, Function<S, Optional<T>> f) {
        return flatten(src.transform(f));
    }

    public static <S, T> Iterable<T> flatTransform(Iterable<S> xs, Function<S, Optional<T>> f) {
        return Optional.presentInstances(transform(xs, f));
    }

    public static Predicate<Optional<?>> isPresentPr() {
        return new Predicate<Optional<?>>() {
            @Override
            public boolean apply(Optional<?> input) {
                return input.isPresent();
            }
        };
    }

}
