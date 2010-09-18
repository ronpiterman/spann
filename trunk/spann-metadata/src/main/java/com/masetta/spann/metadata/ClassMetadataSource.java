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

package com.masetta.spann.metadata;

import com.masetta.spann.metadata.common.Resource;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.reader.ClassReaderAdapter;
import com.masetta.spann.metadata.reader.asm3_2.AsmClassReaderAdapter;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactory;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactoryImpl;
import com.masetta.spann.metadata.rules.MetadataPathRules;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.metadata.rules.Rules;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.metadata.visitors.VisitorControllerImpl;

/**
 * Entry point to the metadata API.
 * <p>
 * Instances should share the store of ClassMetadata as much as possible.
 * <p>
 * <h3>About lazy loading</h3>
 * Lazy loading is optional but highly recommended.
 * <p>
 * Passing a null lazyLoadRules in the constructor will turn lazy loading off.
 * <p>
 * If lazy loading is turned off, default values of annotations attributes may
 * not be read correctly (unless explicitly loading the annotation interface class with
 * rules to load annotation methods default values).
 * <p>
 * If lazy loading is turned on (by passing any rules to the constructor)
 * all metadata which is not loaded explicitly will load on demand. 
 * This is transparent to the user and
 * does not involve the use of any additional API.
 * <p>
 * The metadata implementation classes are <b>not thread safe</b> and 
 * should only be used from a single thread.
 * 
 * @see MetadataPathRulesBuilder
 * @see LazyLoadingRulesFactoryImpl
 * @see Rules
 * @see AsmClassReaderAdapter
 * @see SpringClassReaderAdapter
 * 
 * @author Ron Piterman
 */
public class ClassMetadataSource {
    
    private final MetadataStore store;
    
    private final VisitorControllerImpl visitorController;
    
    /**
     * Create a new ClassMetadataSource.
     * 
     * @param loadRules rules of what to load when a class is first visited.
     * @param lazyLoadRules rules of what to load when an element is lazy loaded.
     * @param classReaderAdapter adapter to implementation of ASM
     * @param store map to store {@link ClassMetadata} objects. 
     */
    public ClassMetadataSource( MetadataPathRules loadRules,
            LazyLoadingRulesFactory lazyLoadRules , ClassReaderAdapter classReaderAdapter ,
            MetadataStore store ) {
        this.store = (MetadataStore) SpannLogFactory.createTraceProxy( store, null, MetadataStore.class );
        this.visitorController = new  VisitorControllerImpl( this.store, 
                loadRules , lazyLoadRules , classReaderAdapter );
    }

    /**
     * Create a new ClassMetadataSource with an internal store.
     * 
     * @param loadRules rules of what to load when a class is first visited.
     * @param lazyLoadRules rules of what to load when an element is lazy loaded.
     * @param classReaderAdapter 
     */
    public ClassMetadataSource( MetadataPathRules loadRules , 
            LazyLoadingRulesFactory lazyLoadRules , ClassReaderAdapter classReaderAdapter ) {
        this( loadRules , lazyLoadRules , classReaderAdapter , new MetadataStoreImpl() );
    }
    
    public ClassMetadata getClassMetadata( Resource resource ) {
        ClassMetadata cm = store.getByResource( resource );
        if ( cm == null ) {
            visitorController.visit( resource );
            cm = store.getByResource( resource );
        }
        return cm;
    }

}
