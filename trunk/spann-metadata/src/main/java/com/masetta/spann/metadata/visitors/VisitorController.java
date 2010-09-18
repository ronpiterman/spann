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
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.util.Provider;

interface VisitorController extends LazyMetadataLoader {
    
    /**
     * Return the metadata of the current visitor
     * @param <T>
     * @param type
     * @return
     */
    <T> T getCurrentMetadata( Class<T> type );
    
    <T> VisitorAdapter<T> visit( Class<T> visitorType, Metadata metadata, ArtifactElement element );
    
    /**
     * Convenience method for obtaining a ClassMetadata for the given className and dimensions,
     * using the class loader of the current visited class.
     * @see #getClassMetadata(String, int, Provider)
     */
    ClassMetadata getClassMetadata( String className , int dimensions );
    
    /**
     * Return the metadata of the given className. If the metadata is not available
     * a new Metadata is created. Will only return null if currently no ClassLoader
     * is available; practically this means only calls from within the PackageVisitorImpl
     * will return null.
     * 
     * @param className
     * @param dimensions the array dimensions of the class
     * @param classLoaderProvider provider for the class loader attached to the metadata.
     * 
     * @return metadata for the given class name or null if no class loader is available
     *         in the current visit path.
     */
    ClassMetadata getClassMetadata( String className , int dimensions , Provider<ClassLoader> classLoaderProvider );

    void visitEnd();

    /** 
     * If the given element should be loaded for the current metadata.
     * @param e
     * @return
     */
    boolean isVisit(ArtifactElement e);

    VisitorDelegate getDelegate();

	VisitorAdapter<?> getEmptyVisitorAdapter(Artifact annotation);
    
}
