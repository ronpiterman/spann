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

package com.masetta.spann.metadata.reader;

import com.masetta.spann.metadata.visitors.AnnotationVisitorImpl;
import com.masetta.spann.metadata.visitors.ClassVisitorImpl;
import com.masetta.spann.metadata.visitors.FieldVisitorImpl;
import com.masetta.spann.metadata.visitors.MethodVisitorImpl;

/**
 * Generic adapter interface for class reader visitor interfaces.<p>
 * 
 * @author Ron Piterman
 * 
 * @param <T> The delegate type, one of {@link ClassVisitorImpl}, {@link MethodVisitorImpl}, 
 * 		{@link AnnotationVisitorImpl}, {@link FieldVisitorImpl}.
 * 
 * @version $Id: $
 */
public interface VisitorAdapter<T> {
    
    /**
     * Retrieve the spann delegate which handles the visitr callback methods.
     * <p>
     * 
     * @return one of {@link ClassVisitorImpl}, {@link MethodVisitorImpl}, 
 * 		{@link AnnotationVisitorImpl}, {@link FieldVisitorImpl}
     */
    T getDelegate();

}
