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

package com.masetta.spann.spring;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.Metadata;

/**
 * Visitor's access to spring and to the beans created by other visitors. Enables adding or
 * changing bean definitions.
 * 
 * @author Ron Piterman    
 */
public interface ScanContext {
    
    /**
     * Attach the given BeanDefinitionHolder (1) to the given Metadata with the given role and
     * (2) to the given attach-scope with its class name.
     * <p>
     * Using <code>null</code> as scope will attach to the <i>global</i> scope.
     *  
     * <p>
     * For example: attaching a BDH with class name "example.Foo" in attach-scope <code>null</code> 
     * and role 'foo' to a MethodMetadata will:
     * <ol>
     * <li>Attached the given BDH to the method with the role 'foo'.</li>
     * <li>Attached the given BDH to the global scope using its the bean's class-name as key.</li>
     * </ol>
     * 
     * <p>
     * This visitor or other visitors can later access the same BDH, use it as bean-property or mutate it.
     *
     * <p>
     * If a bean with the given class is alreay attached to the given scope, 
     * an IllegalConfigurationException is thrown.
     * 
     * <p>
     * If a visitor needs to add a bean definition which should not be attached
     * to the current element or should not be subject to future use and changes ( and is therefore
     * considered immutable) it should use the {@link #addFinal(BeanDefinitionHolder)} method.
   	 *
     * <p>
     * <b>For example:</b>
     * for method metadata of <code>void foo()</code> in class <code>example.Example</code> 
     * <br>
     * and BeanDefintionHolder of bean type <code>example.Bar</code>
     * <br>
     * <pre>
     * attach( bdh , methodMetadata , Artifact.CLASS , "bar" );
     * </pre>
     * will attach bdh to :
     * <pre>
     * void Foo() => 'bar'
     * class example.Example => 'example.Bar'
     * </pre>
     * 
     * Other visitor can later retrieve the BDH either by metadata and role, 
     * or by scope and class name.
     * 
     * @param bdh the {@link BeanDefinitionHolder} to attach
     * @param metadata the current metadata
     * @param scope the scope to attach to, or null for global scope.
     * @param role the role to attach to.
     * 
     */
    void attach( BeanDefinitionHolder bdh, Metadata metadata , Artifact attachScope, String role );
    
    /**
     * Adds a final {@link BeanDefinitionHolder} to the context. The given
     * {@link BeanDefinitionHolder} is added to the context, and can not be
     * accessed by any other visitor.
     * <p>
     * If a Visitor needs to allow other visitors to access or mutate the
     * {@link BeanDefinitionHolder} it should use the
     * {@link #attach(BeanDefinitionHolder, AttachScope, String) attach} method instead.
     * 
     * @param bdh
     *            a final, immutable {@link BeanDefinitionHolder}.
     */
    void addFinal(BeanDefinitionHolder bdh);
    
    /**
     * Get the bean definition attached to the given scope and the given role.
     * 
     * @param metadata a metadata to which a bean definition holder is attached.
     * @param scope Either (1) the scope to which the bean was attached, or (2) the Artifact corresponding
     * 		to the Metadata which was used to attach the BDH
     * @param beanRole if scope is (1), beanrole should be the role to which the BDH was attached.
     * 		   if scope is (2), beanRole should be the full qualified class name of the bean class.
     * 		beanRole may also be null, in which case only one BDH may be attached to the given metadata, which
     * 		will be retrieved. 
     * 		
     * @return a bean definition holder previously attached to the given element in the given scope by the given role.
     * 
     * @throws IllegalArgumentException if the given beanRole is null and more than one
     *         bean definition holder is attached to the given element.
     */
    BeanDefinitionHolder getAttachedBean( Metadata metadata, Artifact scope , String beanRole );
    
    /**
	 * Return the BeanDefinition for the given bean name. Can not be used to access
	 * beans registered by this context using {@link #attach(BeanDefinitionHolder, Metadata, Artifact, String) attach}
	 * or {@link #addFinal(BeanDefinitionHolder) addFinal}, and should only be used to obtain reference
	 * to beans defined by spring XML or standard spring annotations.
	 * 
	 * @param beanName name of a spring bean to find a definition for. 
	 * @return the BeanDefinition for the given name (never <code>null</code>)
	 * 
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 */
	BeanDefinition getSpringBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Return the names of all beans defined in spring bean factory. This does not
	 * include any beans registered by this context and should be used to obtain reference
	 * to beans defined by spring XML or standard spring annotations.
	 * 
	 * @return the names of all beans defined in the spring bean factory,
	 * or an empty array if none defined. The array does not include any beans defined by this 
	 * 	context.
	 */
	String[] getSpringBeanDefinitionNames();
	
    /**
     * Create a BeanDefinition and set its defaults.
     * 
     * The created BeanDefinition is <b>not</b> added to the context - this must
     * be explicitly done via {@link #set(BeanDefinitionHolder)} or
     * {@link #addFinal(BeanDefinitionHolder)}.
     * 
     * Autowire candidate is set according to patterns defined by the context's
     * creator.
     * 
     * Visitors may create their own BeanDefinitions and are not
     * restricted to using this method.
     * 
     * @param 
     * 
     * @param className
     * 
     * @return A BeanDefinition, pre configured with various defaults.
     */
    BeanDefinition createDefaultBeanDefinition(Object source, String className);
    
    /**
     * Create a BeanDefinitionBuilder for the given metadtaa, classname and source.
     * <p>
     * The builder is a conviniece method for creating a bean definitions.
     * 
     * @param metadata the current meatdata.
     * @param classname the bean's classname (full qualified)
     * @param source any object that should be used as bean source.
     * 
     * @return a new builder for a bean with the given classname.
     */
    BeanDefinitionBuilder builder( Metadata metadata , String classname , Object source );
    
    /** Use Spring 'logic' to generate a name for the given beanDefinition */
    String generateBeanName( BeanDefinition beanDefinition );
    
    /** create a unique name using the given base bean name */
    String generateBeanName( String basename );

}
