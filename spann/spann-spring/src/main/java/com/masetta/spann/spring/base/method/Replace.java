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

package com.masetta.spann.spring.base.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.MethodReplacer;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Replace the call from the annotated (abstract) method
 * to a method replacer.
 * 
 * @author Ron Piterman    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Visitor(order=Order.AFTER_CREATE , value=ReplaceVisitor.class)
public @interface Replace {
    
    String ATTACH_SCOPE_ATTRIBUTE = "attachScope";
    
    String METHOD_REPLACER_ATTRIBUTE = "methodReplacer";
    
    String METHOD_REPLACER_BEAN_ATTRIBUTE = "methodReplacerBean";
    
    
    /**
     * Either a class implementing {@link MethodReplacer} or {@link FactoryBean}
     * which creates a {@link MethodReplacer}
     * <p>
     * The class should not be configured or annotated as a spring bean.
     * It will be added to the container by the annotation visitor for in the given scope.
     * 
     * @return
     */
    Class<?> methodReplacer() default Void.class;
    
    String methodReplacerBean() default "";
    
    /**
     * If the given type should be instantiated only one and be used for
     * all replaced methods.
     * 
     * If false, for each method replaced by this annotation a bean of the given type
     * will be created.
     */
    Artifact attachScope() default Artifact.METHOD;
    
}
