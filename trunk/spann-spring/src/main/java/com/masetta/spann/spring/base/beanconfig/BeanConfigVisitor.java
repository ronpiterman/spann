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

package com.masetta.spann.spring.base.beanconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.modifier.ClassType;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.spring.RuntimeSpannReference;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.base.Attached;
import com.masetta.spann.spring.core.visitor.DefSupport;

public class BeanConfigVisitor extends AnnotationPathMetadataVisitor<AnnotatedElementMetadata> {

	private static final String ATTRIBUTE_HANDLER_DEF = AttributeHandlerDefinition.class.getCanonicalName();
	
	private static final String BEAN_CONFIG = BeanConfig.class.getCanonicalName();
	
	private Map<String,AttributeHandler> handler = new HashMap<String,AttributeHandler>();

	public BeanConfigVisitor() {
		super( AnnotatedElementMetadata.class, BEAN_CONFIG , false );
	}

	@Override
	protected void visit(AnnotatedElementMetadata metadata, ScanContext context, AnnotationPath path) {
		if ( ( metadata instanceof ClassMetadata ) && 
				(((ClassMetadata)metadata).getModifier().getClassType().equals( ClassType.ANNOTATION ) ) ) {
			return;
		}
		
		AnnotationMetadata attached = path.getAttribute( 0, AnnotationMetadata.class, 
				BeanConfig.ATTACHED_ATTRIBUTE , false );
		ClassMetadata create = path.getAttribute( 0 , ClassMetadata.class,
				BeanConfig.CREATE_ATTRIBUTE , false );
		
		BeanDefinition beanDefinition = getOrCreateBeanDefinition( metadata, context, attached , create );
		
		boolean explicit = path.getAttribute( 0, Boolean.class, BeanConfig.EXPLICIT_ATTRIBUTE, true );
		
		AnnotationMetadata annotation = path.getPath()[1];
		
		ClassMetadata defHandlerType = path.getAttribute( 0 , ClassMetadata.class,
				BeanConfig.DEFAULT_ATTRIBUTE_HANDLER_ATTRIBUTE , true );
		
		AttributeHandler defaultHandler = getHandler( defHandlerType );
		
		// TODO apply references
		for ( AnnotationMetadata am : path.getAttribute(0, AnnotationMetadata[].class, BeanConfig.REFERENCES_ATTRIBUTE, true ) ) {
			String property = am.getAttribute( String.class, SpannReference.PROPERTY, false );
			String role = am.getAttribute( String.class, SpannReference.ROLE, false );
			EnumValue scope = am.getAttribute( EnumValue.class, SpannReference.SCOPE, false );
			DefSupport.setProperty( beanDefinition, property, new RuntimeSpannReference( metadata,
					scope.resolve( Artifact.class ) , role ) );
			
		}
		
		AttributeHandler h = null;
		for ( MethodMetadata method : annotation.getType().getMethods() ) {
			Object value = annotation.getAttribute( method.getName(), false );
			if ( explicit && value == null )
				continue;
			
			List<AnnotationPath> paths = method.findAnnotationPaths( ATTRIBUTE_HANDLER_DEF );
			ClassMetadata handlerDef = paths.isEmpty() ? null : paths.get( 0 ).getAttribute( 
					0 , ClassMetadata.class , AttributeHandlerDefinition.VALUE_ATTRIBUTE , false ); 
			
			h = handlerDef == null ? defaultHandler : getHandler( handlerDef );
			
			h.handle( metadata , context , beanDefinition, annotation , method.getName() );
		}
		
	}

	private AttributeHandler getHandler(ClassMetadata cls) {
		AttributeHandler h = this.handler.get( cls.getName() );
		if ( h == null ) {
			h = ClassMetadataSupport.newInstance( AttributeHandler.class, cls );
			this.handler.put( cls.getName(), h );
		}
		return h;
	}

	private BeanDefinition getOrCreateBeanDefinition(AnnotatedElementMetadata metadata, ScanContext context,
			AnnotationMetadata attached, ClassMetadata create) {
		Artifact scope = attached.getAttribute( EnumValue.class, Attached.SCOPE_ATTRIBUTE, 
				true ).resolve( Artifact.class );
		String role = attached.getAttribute( String.class, Attached.ROLE_ATTRIBTUE, true );
		if ( create == null ) {
			return context.getAttachedBean( metadata, scope , role ).getBeanDefinition(); 
		}
		else {
			BeanDefinition beanDefinition = context.createDefaultBeanDefinition( metadata, 
					create.getName() );
			BeanDefinitionHolder bdh = new BeanDefinitionHolder(beanDefinition , 
					context.generateBeanName( beanDefinition ) );
			context.attach( bdh, metadata, scope, role);
			return beanDefinition;
		}
	}
	
	

}
