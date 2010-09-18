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

package com.masetta.spann.metadata.core.support;

import java.util.Collection;
import java.util.Collections;
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
import com.masetta.spann.metadata.core.modifier.Access;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.ClassType;
import com.masetta.spann.metadata.core.modifier.Modifier;

public final class PrimitiveClassMetadata implements ClassMetadata , ClassModifier {
	
	private final String name;
	
	private final ArtifactPath path;

	private final ClassLoader classloader;
	
	public PrimitiveClassMetadata( Class<?> cls ) {
		this.classloader = cls.getClassLoader();
		this.name = cls.getCanonicalName();
		this.path = ArtifactPath.EMPTY_PATH.append( this );
	}

	public Object getAnnotationAttributeDefault(String attribute) {
		return null;
	}

	public ClassLoader getClassLoader() {
		return classloader;
	}

	public int getDimensions() {
		return 0;
	}

	public Set<FieldMetadata> getFields() {
		return Collections.emptySet();
	}

	public Set<ClassMetadata> getInterfaces(boolean loadSignatureIfNeeded) {
		return Collections.emptySet();
	}

	public Collection<MethodMetadata> getMethods() {
		return Collections.emptySet();
	}

	public ClassModifier getModifier() {
		return this;
	}

	public ClassMetadata getOuterClass() {
		return null;
	}

	public Modifier getOuterModifier() {
		return null;
	}

	public ClassMetadata getSuperClass(boolean loadSignatureIfNeeded) {
		return null;
	}

	public List<TypeParameter> getTypeParameters() {
		return Collections.emptyList();
	}

	public Artifact getArtifact() {
		return Artifact.CLASS;
	}

	public String getName() {
		return name;
	}

	public Metadata getParent() {
		return null;
	}

	public ArtifactPath getPath() {
		return path;
	}

	public List<AnnotationPath> findAnnotationPaths(String annotationCannonicalClassName) {
		return Collections.emptyList();
	}

	public AnnotationMetadata getAnnotation(String cannonicalClassName) {
		return null;
	}

	public ClassType getClassType() {
		return ClassType.CLASS;
	}

	public boolean isAbstract() {
		return false;
	}

	public Access getAccess() {
		return Access.PUBLIC;
	}

	public boolean isFinal() {
		return true;
	}

	public boolean isStatic() {
		return false;
	}

	public boolean isSynthetic() {
		return false;
	}

	public String getSimpleName() {
		return getName();
	}

}
