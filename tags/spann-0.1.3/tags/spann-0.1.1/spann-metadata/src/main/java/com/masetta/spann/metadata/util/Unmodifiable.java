/**
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masetta.spann.metadata.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Unmodifiable<T> {
    
    private final T unmodifable;
    
    private final T underlying;

    public Unmodifiable(T unmodifable, T underlying) {
        super();
        this.unmodifable = unmodifable;
        this.underlying = underlying;
    }

    public T getUnmodifable() {
        return unmodifable;
    }

    public T getUnderlying() {
        return underlying;
    }
    
    public static <T> Unmodifiable<List<T>> list(List<T> list) {
         return new Unmodifiable<List<T>>( Collections.unmodifiableList( list ) , list );
    }
    
    public static <T> Unmodifiable<Set<T>> set(Set<T> set ) {
        return new Unmodifiable<Set<T>>( Collections.unmodifiableSet( set ) , set );
    }

}
