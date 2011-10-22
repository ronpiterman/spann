
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
 *
 * @author rpt
 * @version $Id: $
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

/**
 * ClassMetadata implementation for java primitive types.
 * @author Ron Piterman
 *
 */
public final class PrimitiveClassMetadata implements ClassMetadata , ClassModifier {
	
	private final String name;
	
	private final ArtifactPath path;

	private final ClassLoader classloader;
	
	/**
	 * <p>Constructor for PrimitiveClassMetadata.</p>
	 *
	 * @param cls a {@link java.lang.Class} object.
	 */
	public PrimitiveClassMetadata( Class<?> cls ) {
		this.classloader = cls.getClassLoader();
		this.name = cls.getCanonicalName();
		this.path = ArtifactPath.EMPTY_PATH.append( this );
	}

	/** {@inheritDoc} */
	public Object getAnnotationAttributeDefault(String attribute) {
		return null;
	}

	/**
	 * <p>getClassLoader</p>
	 *
	 * @return a {@link java.lang.ClassLoader} object.
	 */
	public ClassLoader getClassLoader() {
		return classloader;
	}

	/**
	 * <p>getDimensions</p>
	 *
	 * @return a int.
	 */
	public int getDimensions() {
		return 0;
	}

	/**
	 * <p>getFields</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<FieldMetadata> getFields() {
		return Collections.emptySet();
	}

	/** {@inheritDoc} */
	public Set<ClassMetadata> getInterfaces(boolean loadSignatureIfNeeded) {
		return Collections.emptySet();
	}

	/**
	 * <p>getMethods</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<MethodMetadata> getMethods() {
		return Collections.emptySet();
	}

	/**
	 * <p>getModifier</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.modifier.ClassModifier} object.
	 */
	public ClassModifier getModifier() {
		return this;
	}

	/**
	 * <p>getOuterClass</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
	 */
	public ClassMetadata getOuterClass() {
		return null;
	}

	/**
	 * <p>getOuterModifier</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.modifier.Modifier} object.
	 */
	public Modifier getOuterModifier() {
		return null;
	}

	/** {@inheritDoc} */
	public ClassMetadata getSuperClass(boolean loadSignatureIfNeeded) {
		return null;
	}

	/**
	 * <p>getTypeParameters</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<TypeParameter> getTypeParameters() {
		return Collections.emptyList();
	}

	/**
	 * <p>getArtifact</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.common.Artifact} object.
	 */
	public Artifact getArtifact() {
		return Artifact.CLASS;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>getParent</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.Metadata} object.
	 */
	public Metadata getParent() {
		return null;
	}

	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.ArtifactPath} object.
	 */
	public ArtifactPath getPath() {
		return path;
	}

	/** {@inheritDoc} */
	public List<AnnotationPath> findAnnotationPaths(String annotationCannonicalClassName) {
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	public AnnotationMetadata getAnnotation(String cannonicalClassName) {
		return null;
	}

	/**
	 * <p>getClassType</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.modifier.ClassType} object.
	 */
	public ClassType getClassType() {
		return ClassType.CLASS;
	}

	/**
	 * <p>isAbstract</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAbstract() {
		return false;
	}

	/**
	 * <p>getAccess</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.core.modifier.Access} object.
	 */
	public Access getAccess() {
		return Access.PUBLIC;
	}

	/**
	 * <p>isFinal</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFinal() {
		return true;
	}

	/**
	 * <p>isStatic</p>
	 *
	 * @return a boolean.
	 */
	public boolean isStatic() {
		return false;
	}

	/**
	 * <p>isSynthetic</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSynthetic() {
		return false;
	}

	/**
	 * <p>getSimpleName</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSimpleName() {
		return getName();
	}

}
