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

package com.masetta.spann.metadata.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.util.Unmodifiable;

class TypeMetadataImpl extends ClassMetadataDelegate implements TypeMetadata, ClassMetadata {
    
    private ClassMetadata outerType;

    private Unmodifiable<List<TypeArgument>> typeArguments;

    // ---------------------------------------------------------------------------------------------
    // ctor
    // ---------------------------------------------------------------------------------------------

    TypeMetadataImpl() {
    }

    TypeMetadataImpl(ClassMetadata clazz) {
        this.classMetadata = clazz;
    }

    // ---------------------------------------------------------------------------------------------
    // impl
    // ---------------------------------------------------------------------------------------------

    /**
     * <p>Getter for the field <code>typeArguments</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public List<TypeArgument> getTypeArguments() {
        if (this.typeArguments == null)
            return Collections.EMPTY_LIST;
        else
            return this.typeArguments.getUnmodifable();
    }

    void setClass(ClassMetadata cls) {
        if (this.classMetadata != null) {
            throw new IllegalStateException("Class already set.");
        }
        this.classMetadata = cls;
    }

    void addTypeArgument(TypeArgument tp) {
        if (this.typeArguments == null) {
            this.typeArguments = Unmodifiable
                    .list(new ArrayList<TypeArgument>());
        }
        this.typeArguments.getUnderlying().add(tp);
    }

    /**
     * <p>getClassMetadata</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    /**
     * <p>Getter for the field <code>outerType</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getOuterType() {
        return this.outerType;
    }
    
    void setOuterType( ClassMetadata outerType ) {
        this.outerType = outerType;
    }
    
}
