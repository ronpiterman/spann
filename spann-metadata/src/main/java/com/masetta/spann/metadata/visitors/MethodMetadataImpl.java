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
import com.masetta.spann.metadata.util.EqualsBuilder;
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
    

    public MethodModifier getModifier() {
        return modifier;
    }

    public ClassMetadata getReturnClass() {
        return returnClass;
    }
    
    public GenericType getReturnType() {
        load( ArtifactElement.SIGNATURE );
        return this.returnType;
    }

    public List<ParameterMetadata> getParameters() {
        return parameters;
    }

    @Override
    protected boolean equals(AbstractMetadataImpl obj, EqualsBuilder b) {
        return super.equals(obj, b)
            && b.eq( parameters , ((MethodMetadata)obj).getParameters() );
    }

    public List<ClassMetadata> getExceptions() {
        return exceptions;
    }

    public String getSignature() {
        return signature;
    }

    void setReturnType( GenericType type ) {
        this.returnType = type;
    }
    
    public void addTypeParameter(TypeParameter pc) {
        if ( this.typeParameter == null )
            this.typeParameter = Unmodifiable.list( new ArrayList<TypeParameter>() );
        this.typeParameter.getUnderlying().add( pc );
    }
    
    public List<TypeParameter> getTypeParameters() {
        load( ArtifactElement.SIGNATURE );
        if ( this.typeParameter == null )
            return null;
        else
            return this.typeParameter.getUnmodifable();
    }
    
    public String toString() {
        return "method metdata [" + returnType + " " + getName() + "" 
            + Arrays.toString( getParameters().toArray() ) + "]";
    }

    public Object getDefault() {
        load( ArtifactElement.DEFAULT_VALUE );
        return this.defaultValue;
    }
    
    void setDefault( Object def ) {
        this.defaultValue = def;
    }
    
    public ClassMetadata getParent() {
    	return (ClassMetadata)super.getParent();
    }

}
