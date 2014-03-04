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

package com.github.filosganga.geogson.util;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

/**
 * @author Filippo De Luca - fdeluca@expedia.com
 */
public class ChainableOptional<T> {

    private Optional<T> delegate;

    ChainableOptional(Optional<? extends T> src) {
        this.delegate = (Optional<T>) src;
    }

    public static <T> ChainableOptional<T> of(Optional<? extends T> src) {
        return new ChainableOptional<T>(src);
    }

    public static <T> ChainableOptional<T> of(Supplier<Optional<? extends T>> src) {
        return of(src.get());
    }

    public ChainableOptional<T> or(Supplier<Optional<? extends T>> secondChoice) {
        if(delegate.isPresent()) {
            return this;
        } else {
            return ChainableOptional.of(secondChoice);
        }
    }

    public T orFinally(Supplier<T> finalChoice) {
        return delegate.or(finalChoice);
    }

    public T orFinally(T finalChoice) {
        return delegate.or(Suppliers.ofInstance(finalChoice));
    }


}
