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

package com.masetta.spann.spring.core.visitor;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.util.Resolver;
import com.masetta.spann.spring.util.spring.ClassResolverImplementorAdapter;
import com.masetta.spann.spring.util.spring.LazyClassBeanDefinition;
import com.masetta.spann.spring.util.type.CglibAbstractClassImplementor;
import com.masetta.spann.spring.util.type.CglibInterfaceImplementor;
import com.masetta.spann.spring.util.type.Implementor;

/**
 * Usefull utility methods for MetadataVisitors.
 * 
 * @author Ron Piterman
 */
public abstract class VisitorSupport {

	private VisitorSupport() {
	}

	/**
	 * Get a BeanDefinitionHolder attached to the given scope by the given
	 * classname, or create a new one and attache it to the given metadata in
	 * the given scope and role.
	 * 
	 * @param context
	 *            the current scan context.
	 * @param metadata
	 *            the context metadata data.
	 * @param scope
	 *            scope to lookup the BeanDefinitionHolder, and attach the
	 *            created BDH, if not found.
	 * @param role
	 *            role to attache the new created BDH, if needed.
	 * @param source
	 *            the bean definition source.
	 * @param classname
	 *            the class name to lookup in the given scope, and to be used
	 *            for creating the new bean definition, if needed.
	 * 
	 * @return a BeanDefinitionHolder of a bean with the given classname
	 *         attached to the given scope (either existing one or a new
	 *         created).
	 */
	public static BeanDefinitionHolder getOrCreateAndAttach(ScanContext context, Metadata metadata,
			Artifact scope, String role, Object source, String classname) {
		BeanDefinitionHolder bdh = context.getAttachedBean(metadata, scope, classname);
		if ( bdh == null ) {
			BeanDefinition bd = context.createDefaultBeanDefinition(source, classname);
			String beanName = context.generateBeanName(bd);
			bdh = new BeanDefinitionHolder(bd, beanName);
			context.attach(bdh, metadata, scope, role);
		}
		return bdh;
	}

	/**
	 * Replace the given method in the bean definition attached to the given
	 * scope and role by the methodReplacer with the given name.
	 * 
	 * @param context
	 *            current scan context.
	 * @param metadata
	 *            metadata of the method to be replaced.
	 * @param scope
	 *            the scope to lookup the bean who's method should be replaced.
	 * @param role
	 *            the role of the bean who's method should be replaced.
	 * @param methodReplacerBeanName
	 *            the MethodReplacer bean name.
	 */
	public static void replaceMethod(ScanContext context, MethodMetadata metadata, Artifact scope,
			String role, String methodReplacerBeanName) {
		BeanDefinitionHolder bdh = context.getAttachedBean(metadata, scope, role);
		if ( bdh == null ) {
			throw new IllegalArgumentException("No bean with role " + role + " attached to "
					+ scope + " of " + metadata);
		}

		DefSupport.replaceMethod(bdh.getBeanDefinition(), metadata, methodReplacerBeanName);
	}
	
	/**
	 * Create a bean definition to implement the given class.
	 * 
	 * @param metadata class metadata of the class to implement.
	 * @param context current scan context.
	 * 
	 * @return a BeanDefinition of a bean which extends the given class or implements
	 * 		the given interface.
	 */
	public static BeanDefinition implement( ClassMetadata metadata , ScanContext context ) {
		BeanDefinition beanDefinition = context.createDefaultBeanDefinition( metadata, metadata.getName() );
		ClassModifier modifier = metadata.getModifier();
		if ( ! canInstanciate( modifier ) ) {
			return null;
		}
        Implementor implementor;
        if ( modifier.getClassType().isInterface() ) {
            implementor = new CglibInterfaceImplementor();
        }
        else {
            implementor = new CglibAbstractClassImplementor();
        }
        
        Resolver<Class<?>,BeanDefinition> resolver = new ClassResolverImplementorAdapter(
                implementor , metadata.getClassLoader() );
        return new LazyClassBeanDefinition( beanDefinition , resolver );
	}

	private static boolean canInstanciate(ClassModifier modifier) {
		if ( modifier.getClassType().isClass() ) {
			return modifier.isAbstract();
		}
		
		return ( modifier.getClassType().isInterface() );
	}

	/**
	 * Find a single spring bean for the given class. Class matching is strict - only a bean which
	 * is defined for the exact class name is returned.<p>
	 * If no bean is defined returns null<p>
	 * If more than one bean is defined, throws an IllegalArgumentException.<p>   
	 * @param context currenct ScannContext
	 * @param parent a class name
	 * 
	 * @return 
	 */
	public static String findSingleSpringBeanForClass(ScanContext context, String className ) {
		String result = null;
		for ( String beanName : context.getSpringBeanDefinitionNames() ) {
			BeanDefinition def = context.getSpringBeanDefinition( beanName );
			if ( def.getBeanClassName().equals( className ) ) {
				if ( result != null ) {
					throw new IllegalArgumentException("More than one spring bean defined for class " + className );
				}
				result = beanName;
			}
		}
		return result;
	}
	
	public static BeanDefinition findBean( ScanContext context , ClassMetadata cls ) {
		String springBean = findSingleSpringBeanForClass(context, cls.getName() );
		if ( springBean != null )
			return context.getSpringBeanDefinition( springBean );
		
		BeanDefinitionHolder h = context.getAttachedBean( cls , Artifact.CLASS , "main" );
		if ( h != null ) {
			return h.getBeanDefinition();
		}
		
		// TODO try some more?
		
		return null;
	}
	
	public static String findBeanName( ScanContext context , ClassMetadata cls ) {
		String springBean = findSingleSpringBeanForClass(context, cls.getName() );
		if ( springBean != null )
			return springBean;
		
		BeanDefinitionHolder h = context.getAttachedBean( cls , Artifact.CLASS , "main" );
		if ( h != null ) {
			return h.getBeanName();
		}
		
		// TODO try some more?
		
		return null;
	}
	
	public static boolean isSpringBean( ScanContext ctx , String beanName ) {
		for ( String name : ctx.getSpringBeanDefinitionNames() ) {
			if ( beanName.equals( name ) )
				return true;
		}
		return false;
	}

}
