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

package com.masetta.spann.metadata.core;

import java.util.List;



/**
 * A specification of a generic class.
 * <p>
 * TypeMetadata is extending {@link ClassMetadata} and contains specification of the underlying class'
 * {@link #getTypeParameters() typeParameters}.
 * <p>
 * For example, having
 * <code> public class Foo extends ArrayList&lt;String> {...} </code>,
 * Foo will be represented by ClassMetadata foo.
 * foo's {@link ClassMetadata#getSuperClass() superClass } will return a {@link TypeMetadata}, 
 * with a single {@link TypeArgument} of {@link TypeParameter#getTypes() type} java.lang.String.
 *
 * @author Ron Piterman    
 */
public interface TypeMetadata extends ClassMetadata {
    
    /**
     * The signature of the outerclass, if this metadata is an inner non static class.
     * @return
     */
    ClassMetadata getOuterType();
    
    List<TypeArgument> getTypeArguments();
    
}
