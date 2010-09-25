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

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.util.EqualsBuilder;

class FieldMetadataImpl extends AnnotatedElementMetadataImpl implements FieldMetadata , SignatureOwner {
    
    private final String signature;
    
    private ClassMetadata fieldClass;
    
    private GenericType fieldType;

    private final FieldModifier modifier;

    FieldMetadataImpl( ClassMetadata parent, FieldModifier mod , String name , ClassMetadata fieldType , String signature ) {
        super(parent.getPath(), Artifact.FIELD, name );
        this.modifier = mod;
        this.fieldClass = fieldType;
        this.signature = signature;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean equals(AbstractMetadataImpl obj, EqualsBuilder b) {
        return super.equals(obj, b)
            && b.eq( modifier.isStatic() , ((FieldMetadata)obj).getModifier().isStatic() );
    }

    /**
     * <p>Getter for the field <code>fieldClass</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getFieldClass() {
        return fieldClass;
    }

    /**
     * <p>Getter for the field <code>modifier</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.modifier.FieldModifier} object.
     */
    public FieldModifier getModifier() {
        return modifier;
    }

    /**
     * <p>Getter for the field <code>signature</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * <p>Getter for the field <code>fieldType</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.GenericType} object.
     */
    public GenericType getFieldType() {
        load( ArtifactElement.SIGNATURE );
        return fieldType;
    }

    void setFieldType(GenericType fieldType) {
        this.fieldType = fieldType;
    }
}
