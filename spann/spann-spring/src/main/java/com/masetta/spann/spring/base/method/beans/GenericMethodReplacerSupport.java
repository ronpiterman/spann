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

package com.masetta.spann.spring.base.method.beans;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.ManagedList;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.ReplaceVisitor;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.core.visitor.VisitorSupport;
import com.masetta.spann.spring.util.Handler;

public final class GenericMethodReplacerSupport {
	
	private GenericMethodReplacerSupport() {}
	
	public static final String CONTEXT_VISITORS_FACTORY_BEAN_ROLE = "contextVisitors";
	
	/**
	 * Replace the given method in the given bean definition.
	 * <p>
	 * This method creates two beans:
	 * <OL>
	 * 	<LI>A GenericMethodReplacer, attached to the method as {@link ReplaceVisitor#METHOD_REPLACER_ROLE}.
	 *  <LI>A CallContextHandlerChainFactoryBean, attached to the method as {@link #CONTEXT_VISITORS_FACTORY_BEAN_ROLE}.
	 * </OL>
	 * 
	 * @param methodOwnerBean the bean definition whos method will be replaced.
	 * @param metadata the metadata of the method which should be replaced.
	 * @param context the current scan context
	 * @param path the current annotation path, will be <code>source</code> of the
	 * 		created beans.
	 * @return
	 */
	public static BeanDefinitionHolder replaceMethod( BeanDefinition methodOwnerBean,
			MethodMetadata metadata, ScanContext context , AnnotationPath path ) {
		
		BeanDefinitionHolder callbacks = context.builder( metadata , 
				CallContextChainFactoryBean.class.getCanonicalName() , path )
				.addConstructorArgument( metadata.getParameters().size() )
				.set( CallContextChainFactoryBean.CALLBACKS_PROPERTY, new ManagedList() )
				.setBeanName( beanName(metadata, "#chain"))
				.attach( Artifact.METHOD , CONTEXT_VISITORS_FACTORY_BEAN_ROLE );
		
		BeanDefinitionHolder replacer = context.builder( metadata, 
				GenericMethodReplacer.class.getCanonicalName(), path )
				.setReference( GenericMethodReplacer.CONTEXT_HANDLER_CHAIN_PROPERTY, callbacks.getBeanName() )
				.set( GenericMethodReplacer.DESCRIPTION_PROPERTY , metadata.toString() + ":" + path.toString() )
				.setBeanName( beanName(metadata , "" ) )
				.attach( Artifact.METHOD , ReplaceVisitor.METHOD_REPLACER_ROLE );
		
		DefSupport.replaceMethod( methodOwnerBean, metadata, replacer.getBeanName() );
		return replacer;
		
	}

	private static String beanName(MethodMetadata metadata, String suffix) {
		return metadata.getParent().getSimpleName()+"#"+metadata.getName() + suffix;
	}
	
	
	/**
	 * Set the contextFactory property of the given GenericMethodReplacer bean definition.
	 * @param methodReplacer BeanDefinitionHolder of a GenericMethodReplacer.
	 * @param callContextFactory
	 */
	public static void setCallContextFactory(BeanDefinitionHolder methodReplacer, Object callContextFactory) {
		setMethodReplacerProperty( methodReplacer , GenericMethodReplacer.CONTEXT_FACTORY_PROPERTY , 
				callContextFactory );
	}

	/**
	 * Add a CallContxtVisitosBuilderCallback to the given bean CallContextVisitorsFactory
	 * bean definition.
	 * 
	 * @param callContextVisitorsFactoryBean BDH of CallContextHandlerChainFactoryBean
	 * @param index index to add the callback to. Negative values are used ruby/groovy style (relative
	 * 	to <code>list.size() + 1</code>)
	 * @param callback a bean definition, reference or an implementation of 
	 * 		CallContextHandlerChainBuilderCallback
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void addCallContextChainFactoryCallback( BeanDefinitionHolder callContextChainFactoryBean,
			int index , Object callback ) {
		@SuppressWarnings("rawtypes")
		List list = getCallbacks(callContextChainFactoryBean);
		list.add( relIndex( index , list , 0 ) , callback );
	}

	private static int relIndex( int index, List<?> list, int negativeOffs) {
		return index < 0 ? list.size() + 1 + index + negativeOffs : index;
	}


	@SuppressWarnings("unchecked")
	private static List<Handler<CallContextChainBuilder<?>>> getCallbacks(
			BeanDefinitionHolder callContextVisitorsFactoryBean) {
		List<Handler<CallContextChainBuilder<?>>> list =
			DefSupport.getProperty( callContextVisitorsFactoryBean , 
					CallContextChainFactoryBean.CALLBACKS_PROPERTY , List.class );
		return list;
	}
	
	public static void setCallContextVisitorsBuilderCallback( BeanDefinitionHolder callContextVisitorsFactoryBean,
			int index , Handler<CallContextChainBuilder<?>> callback ) {
		 List<Handler<CallContextChainBuilder<?>>> list = getCallbacks(callContextVisitorsFactoryBean);
		list.set( relIndex(index, list, -1), callback );
	}

	/**
	 * Set the given object as a result strategy for the given method replacer.
	 * 
	 * @param resultFactory
	 */
	public static void setResultFactory(BeanDefinitionHolder methodReplacer, Object resultStrategy ) {
		setMethodReplacerProperty( methodReplacer , GenericMethodReplacer.RESULT_STRATEGY_PROPERTY, 
				resultStrategy  );
	}
	
	/**
	 * Set a transformer, to transform the result produced by the
	 * result factory.
	 * @param resultTransformer
	 */
	public static void setResultTransformer(BeanDefinitionHolder methodReplacer, Object resultTransformer ) {
		setMethodReplacerProperty( methodReplacer , GenericMethodReplacer.RESULT_TRANSFORMER_PROPERTY, 
				resultTransformer );
	}

	private static void setMethodReplacerProperty(BeanDefinitionHolder methodReplacer,
			String property, Object value ) {
		DefSupport.setProperty( methodReplacer.getBeanDefinition(), 
				GenericMethodReplacer.class.getCanonicalName(), property, value );
	}


	public static BeanDefinitionHolder findCallContextChainFactoryBean(MethodMetadata metadata,
			ScanContext context, AnnotationPath path) {
		BeanDefinitionHolder contextVisitorsFactoryBean = 
			VisitorSupport.getOrCreateAndAttach( context, metadata, Artifact.METHOD, 
					CONTEXT_VISITORS_FACTORY_BEAN_ROLE, path, 
					CallContextChainFactoryBean.class.getCanonicalName() );
		return contextVisitorsFactoryBean;
	}

}
