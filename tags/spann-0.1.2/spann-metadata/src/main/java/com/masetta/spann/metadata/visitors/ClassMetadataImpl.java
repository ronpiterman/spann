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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.TypeParameter;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.Modifier;
import com.masetta.spann.metadata.util.Unmodifiable;

class ClassMetadataImpl extends AnnotatedElementMetadataImpl 
    implements ClassMetadata , SignatureOwner , TypeParameterSupport {
	
	private final static Set<ClassMetadata> EMPTY_CLASS_METADATA_SET =
		Collections.unmodifiableSet( new HashSet<ClassMetadata>() );
    
    private String signature;

    private final ClassLoader classLoader;
    
    private final LazyMetadataLoader lazyLoader;
    
    private ClassModifier modifier;
    
    private ClassMetadata outerClass;
    
    private ClassMetadata superClass;
    
    private Unmodifiable<List<TypeParameter>> typeParameter;
    
    private Unmodifiable<Set<ClassMetadata>> interfaces;
    
    private Set<ClassMetadata> innerClasses;
    
    private Unmodifiable<Set<FieldMetadata>> fields;
    
    private Map<String,MethodMetadata> methods;
    
    private Modifier outerModifier;
    
    // used by annotation classes only for fast access
    // to annotation attribute default value.
    private Map<String,MethodMetadata> methodsByName;
    
    ClassMetadataImpl( LazyMetadataLoader loader, String name , ClassLoader classLoader ) {
        super( ArtifactPath.EMPTY_PATH , Artifact.CLASS , name );
        this.classLoader = classLoader;
        this.lazyLoader = loader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /** 
     * Only called on inner classes after creation.
     * 
     * @param modifier
     * @param outer
     */
    void setInnerOf( Modifier modifier , ClassMetadata outer ) {
        if ( modifier != null )
            this.outerModifier = modifier;
        this.outerClass = outer;
    }
    
    void init( ClassModifier modifer , String signature, ClassMetadata superclass,
            ClassMetadata[] interfaces) {
    	if ( isLoaded( ArtifactElement.SELF ) )
    		return;
        this.modifier = modifer;
        this.superClass = superclass;
        this.interfaces = Unmodifiable.set( initIfcs(interfaces) );
        this.signature = signature;
        loaded( ArtifactElement.SELF );
    }

    private Set<ClassMetadata> initIfcs(ClassMetadata[] interfaces) {
        if ( interfaces == null || interfaces.length == 0 )
            return EMPTY_CLASS_METADATA_SET;
        else
            return new LinkedHashSet<ClassMetadata>( Arrays.asList( interfaces ) );
    }
    
    public ClassMetadata getSuperClass( boolean loadSignatureIfNeeded ) {
    	ignore( load( ArtifactElement.SELF ) && loadSignatureIfNeeded 
        		&& load( ArtifactElement.SIGNATURE ));
        return superClass;
    }

    public Set<ClassMetadata> getInterfaces(boolean loadSignatureIfNeeded) {
        ignore( load( ArtifactElement.SELF ) && loadSignatureIfNeeded 
        		&& load( ArtifactElement.SIGNATURE ));
        return interfaces == null ? EMPTY_CLASS_METADATA_SET : interfaces.getUnmodifable();
    }

    public ClassMetadata getOuterClass() {
        return outerClass;
    }

    void addField(FieldMetadata field) {
        if ( this.fields == null ) {
        	Set<FieldMetadata> set = new LinkedHashSet<FieldMetadata>();
            this.fields = new Unmodifiable<Set<FieldMetadata>>( Collections.unmodifiableSet( set ) , set );
        }
        this.fields.getUnderlying().add( field );
    }

    public ClassModifier getModifier() {
        load( ArtifactElement.SELF );
        return modifier;
    }

    void addInnerClass(ClassMetadata cls) {
        if ( this.innerClasses == null )
            this.innerClasses = new LinkedHashSet<ClassMetadata>();
        this.innerClasses.add( cls );
    }

    void addMethod(String descriptor, MethodMetadata mm) {
        if ( this.methods == null )
            this.methods = new LinkedHashMap<String,MethodMetadata>();
        this.methods.put( descriptor, mm );
    }

    public Set<ClassMetadata> getInnerClasses() {
        load( ArtifactElement.CLASSES );
        return innerClasses;
    }

	public Set<FieldMetadata> getFields() {
        load( ArtifactElement.FIELDS );
        if ( fields == null ) 
        	return Collections.emptySet();
        return fields.getUnmodifable();
    }

    public Collection<MethodMetadata> getMethods() {
        load( ArtifactElement.METHODS );
        if ( methods == null )
            return Collections.emptySet();
        return methods.values();
    }
    
    public List<TypeParameter> getTypeParameters() {
        load( ArtifactElement.SELF);
        load( ArtifactElement.SIGNATURE );
        if ( this.typeParameter == null )
            return Collections.emptyList();
        else
            return this.typeParameter.getUnmodifable();
    }

    public void addTypeParameter(TypeParameter pc) {
        if ( this.typeParameter == null )
            this.typeParameter = Unmodifiable.list( new ArrayList<TypeParameter>() );
        this.typeParameter.getUnderlying().add( pc );
    }

    void setSuper( TypeMetadataImpl type ) {
        if ( ! type.getName().equals( this.superClass.getName() ) ) {
            throw new IllegalArgumentException("Type " + type + 
                    " can not replce superclass: class name does not match current super class.");
        }
        this.superClass = type;
        return;
    }
    
    void setInterface( TypeMetadataImpl type ) {
        for ( ClassMetadata ifc : getInterfaces(true) ) {
            if ( ifc.getName().equals( type.getName() ) ) {
                this.interfaces.getUnderlying().remove( ifc );
                this.interfaces.getUnderlying().add( type );
                return;
            }
        }
        
        throw new IllegalArgumentException( "Type " + type + " can not be set: not " +
                "found in previously set interface list." );
    }

    public String getSignature() {
        return signature;
    }

    public int getDimensions() {
        return 0;
    }
    
    public String toString() {
            return getClass().getSimpleName() + " [name=" + getName() + "]";
    }
    
    void load( ArtifactPath path , ArtifactElement element ) {
        this.lazyLoader.lazyload( classLoader , getName(), path, element );
    }

    MethodMetadata findMethod(String desc) {
        if ( this.methods == null ) {
            return null;
        }
        return this.methods.get( desc );
    }
    
    public Modifier getOuterModifier() {
        return outerModifier;
    }

    public Object getAnnotationAttributeDefault(String attribute) {
    	getMethods();
        ClassModifier mod = getModifier();
        if ( mod == null )
            return null;
        if ( ! mod.getClassType().isAnnotation() )
            throw new UnsupportedOperationException("Class " + getName() + " is not an annotation interface.");
        if ( this.methodsByName == null ) {
            this.methodsByName = new HashMap<String, MethodMetadata>();
            for ( MethodMetadata mm : getMethods() ) {
                this.methodsByName.put( mm.getName(), mm );
            }
        }
        MethodMetadata mm = this.methodsByName.get( attribute );
        if ( mm == null )
            throw new IllegalArgumentException("No such annotation attribute: " + attribute );
        return mm.getDefault();
    }

	public String getSimpleName() {
		String[] tokens = getName().split("\\.");
		return tokens[tokens.length - 1];
	}

}
