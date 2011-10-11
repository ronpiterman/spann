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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.core.TypeParameter;
import com.masetta.spann.metadata.core.modifier.MethodModifier;
import com.masetta.spann.metadata.util.Equals;
import com.masetta.spann.metadata.util.Unmodifiable;

/**
 * @author Ron Piterman    
 *
 */
class MethodMetadataImpl extends AnnotatedElementMetadataImpl implements MethodMetadata , SignatureOwner ,
    TypeParameterSupport {
    
    private final String signature;
    
    private final MethodModifier modifier;
    
    private final ClassMetadata returnClass;
    
    private GenericType returnType;
    
    private final List<ParameterMetadata> parameters;
    
    private final List<ClassMetadata> exceptions;
    
    private Unmodifiable<List<TypeParameter>> typeParameter;
    
    // Annotation attribute default value.
    private Object defaultValue;
    
    MethodMetadataImpl( ClassMetadata cls , MethodModifier modifier, String name , ClassMetadata returnClass,
            ClassMetadata[] parameter , ClassMetadata[] exceptions , String signature ) {
        super(cls.getPath(), Artifact.METHOD, name );
        this.modifier = modifier;
        this.returnClass = returnClass;
        this.parameters = createArguments( parameter );
        this.exceptions = createExceptions( exceptions );
        this.signature = signature;
    }
    
    @SuppressWarnings("unchecked")
    private List<ParameterMetadata> createArguments( ClassMetadata[] arguments ) {
        if ( arguments == null || arguments.length == 0 )
            return Collections.EMPTY_LIST;
        List<ParameterMetadata> args = new ArrayList<ParameterMetadata>();
        for ( ClassMetadata arg : arguments ) {
            args.add( new ParameterMetadataImpl( this , arg ) );
        }
        return Collections.unmodifiableList( args );
    }
    
    @SuppressWarnings("unchecked")
    private List<ClassMetadata> createExceptions(ClassMetadata[] exs) {
        if ( exs == null || exs.length == 0 )
            return Collections.EMPTY_LIST;
        return Arrays.asList( exs );
    }
    

    /**
     * <p>Getter for the field <code>modifier</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.modifier.MethodModifier} object.
     */
    public MethodModifier getModifier() {
        return modifier;
    }

    /**
     * <p>Getter for the field <code>returnClass</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getReturnClass() {
        return returnClass;
    }
    
    /**
     * <p>Getter for the field <code>returnType</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.GenericType} object.
     */
    public GenericType getReturnType() {
        load( ArtifactElement.SIGNATURE );
        return this.returnType;
    }

    /**
     * <p>Getter for the field <code>parameters</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ParameterMetadata> getParameters() {
        return parameters;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean equalsInternal(AbstractMetadataImpl obj) {
        return super.equalsInternal(obj)
            && Equals.eq( parameters , ((MethodMetadata)obj).getParameters() );
    }

    /**
     * <p>Getter for the field <code>exceptions</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ClassMetadata> getExceptions() {
        return exceptions;
    }

    /**
     * <p>Getter for the field <code>signature</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSignature() {
        return signature;
    }

    void setReturnType( GenericType type ) {
        this.returnType = type;
    }
    
    /** {@inheritDoc} */
    public void addTypeParameter(TypeParameter pc) {
        if ( this.typeParameter == null )
            this.typeParameter = Unmodifiable.list( new ArrayList<TypeParameter>() );
        this.typeParameter.getUnderlying().add( pc );
    }
    
    /**
     * <p>getTypeParameters</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<TypeParameter> getTypeParameters() {
        load( ArtifactElement.SIGNATURE );
        if ( this.typeParameter == null )
            return null;
        else
            return this.typeParameter.getUnmodifable();
    }
    
    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return "method metdata [" + returnType + " " + getName() + "" 
            + Arrays.toString( getParameters().toArray() ) + "]";
    }

    /**
     * <p>getDefault</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getDefault() {
        load( ArtifactElement.DEFAULT_VALUE );
        return this.defaultValue;
    }
    
    void setDefault( Object def ) {
        this.defaultValue = def;
    }
    
    /**
     * <p>getParent</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getParent() {
    	return (ClassMetadata)super.getParent();
    }

}
