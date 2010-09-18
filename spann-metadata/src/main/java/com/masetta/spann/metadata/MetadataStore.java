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

/**
 * Stores class metadata. One instance of Metadata store should be shared between
 * instances of ClassMetadataSource.
 * <p>
 * MetadataStore may be created by clients but its methods should not be used directly by clients.
 * It is used internally by the {@link ClassMetadataSource}
 * class.
 * <p>
 * The store is not expected to be thread safe.
 * <p>
 * @author Ron Piterman
 */
public interface MetadataStore {
	
	void store( Resource resource , ClassMetadata clsMetadata );
	
	ClassMetadata getByClassname( String classname, int dimensions );
	
	ClassMetadata getByResource( Resource r );

}
