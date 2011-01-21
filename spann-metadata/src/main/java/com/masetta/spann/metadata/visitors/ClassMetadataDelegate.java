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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.TypeParameter;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.Modifier;

abstract class ClassMetadataDelegate {
    
    protected ClassMetadata classMetadata;
    
    protected ClassMetadataDelegate() {
    }
    
    protected ClassMetadataDelegate(ClassMetadata classMetadata) {
        this.classMetadata = classMetadata;
    }

    public Artifact getArtifact() {
        return classMetadata.getArtifact();
    }

    public ClassLoader getClassLoader() {
        return classMetadata.getClassLoader();
    }

    public int getDimensions() {
        return classMetadata.getDimensions();
    }

    public Set<FieldMetadata> getFields() {
        return classMetadata.getFields();
    }

    public Set<ClassMetadata> getInterfaces(boolean loadSignatureIfNeeded) {
        return classMetadata.getInterfaces(true);
    }

    public Collection<MethodMetadata> getMethods() {
        return classMetadata.getMethods();
    }

    public ClassModifier getModifier() {
        return classMetadata.getModifier();
    }

    public String getName() {
        return classMetadata.getName();
    }

    public ClassMetadata getOuterClass() {
        return classMetadata.getOuterClass();
    }

    public ArtifactPath getPath() {
        return classMetadata.getPath();
    }

    public ClassMetadata getSuperClass( boolean b ) {
        return classMetadata.getSuperClass( b );
    }

    public List<TypeParameter> getTypeParameters() {
        return classMetadata.getTypeParameters();
    }

    public Modifier getOuterModifier() {
        return classMetadata.getOuterModifier();
    }

    public Object getAnnotationAttributeDefault(String attribute) {
        return classMetadata.getAnnotationAttributeDefault(attribute);
    }

    public AnnotationMetadata getAnnotation(String cannonicalClassName) {
        return classMetadata.getAnnotation(cannonicalClassName);
    }

	public List<AnnotationPath> findAnnotationPaths(String annotationCannonicalClassName ) {
		return classMetadata.findAnnotationPaths(annotationCannonicalClassName);
	}

	public Metadata getParent() {
		return classMetadata.getParent();
	}

	public String getSimpleName() {
		return classMetadata.getSimpleName();
	}
}
