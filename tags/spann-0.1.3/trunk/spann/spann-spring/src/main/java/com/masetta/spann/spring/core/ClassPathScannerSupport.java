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

package com.masetta.spann.spring.core;

import java.util.LinkedList;
import java.util.List;


import com.masetta.spann.metadata.ClassMetadataSource;
import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactory;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactoryImpl;
import com.masetta.spann.metadata.rules.MetadataPathRules;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.core.visitor.DefaultVisitor;

/**
 * Static factory for the default {@link ClassMetadataSource}
 * @author Ron Piterman
 *
 */
public final class ClassPathScannerSupport {
	
	private ClassPathScannerSupport() {}
	
	/**
	 * Create a default {@link ClassMetadataSource} for use with spann class path scan.
	 * <p>
	 * The {@link ClassMetadataSource} will use its own store and spring's ASM implementation.
	 * <p>
	 * The loading rules used by this {@link ClassMetadataSource} are to load class and class
	 * annotations metadata.
	 * <p>
	 * Thee lazy loading rules used by the returned {@link ClassMetadataSource} indicate:
	 * <ul>
	 *   <li>When loading method load its annotations.
	 *   <li>Load also all other methods' annoations ( on same class ).
	 *   <li>When loading paramter annotations load all other methods' parameter annoations.
	 * <ul>  
	 * @return
	 */
	public static ClassMetadataSource createDefaultCms() {
		MetadataPathRules rules = createDefaultRules();
		LazyLoadingRulesFactory lazyRules = createDefaultLazyRules();
		return new ClassMetadataSource( rules , lazyRules ,
				new SpringClassReaderAdapter() );
	}

	private static LazyLoadingRulesFactory createDefaultLazyRules() {
		LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
		impl.lazyLoadChildren( Artifact.METHOD, ArtifactElement.ANNOTATIONS );
		// when loading method annotations, all annotations of other methods in the class should load.
		impl.setLazySiblingsLoadRule( Artifact.METHOD, ArtifactElement.ANNOTATIONS, false );
		// when loading parameter annotations, all parameter annotations of other methods in the class should load.
		impl.setLazySiblingsLoadRule( Artifact.METHOD, ArtifactElement.PARAMETER_ANNOTATIONS, false );
		return impl;
	}

	private static MetadataPathRules createDefaultRules() {
		MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
		// all classes load annotations, and nothing else
		builder.add( Artifact.CLASS , null ).load( ArtifactElement.ANNOTATIONS );
		return builder.build();
	}

	public static List<MetadataVisitor<ClassMetadata>> createDefaultVisitors() {
		List<MetadataVisitor<ClassMetadata>> list = new LinkedList<MetadataVisitor<ClassMetadata>>();
		list.add( new DefaultVisitor() );
		return list;
	}

}
