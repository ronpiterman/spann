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

package com.masetta.spann.spring.base;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;
import com.masetta.spann.spring.util.Factory;
import com.masetta.spann.spring.util.spring.LazyClassBeanDefinitionSupport;
import com.masetta.spann.spring.util.type.ClassMetadataClassFactory;

public class ExtendsVisitor extends AnnotationPathMetadataVisitor<ClassMetadata> {

    private static final String EXTENDS = Extends.class.getCanonicalName();

	public ExtendsVisitor() {
        super(ClassMetadata.class, EXTENDS , true );
    }

	@Override
	protected void visit(ClassMetadata metadata, ScanContext context, AnnotationPath path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void visit(ClassMetadata metadata, ScanContext context, List<AnnotationPath> paths) {
		ClassMetadata superclass = AnnotationMetadataSupport.findAttribute( paths , ClassMetadata.class , 
				Extends.SUPERCLASS_ATTRIBUTE , null , false , false );
		
		if ( ! metadata.getModifier().getClassType().isInterface() ) {
			if ( ! ClassMetadataSupport.instanceOf( metadata, superclass.getName() , 0 ) ) {
				throw new IllegalConfigurationException( EXTENDS , metadata ,  "Should either be an interface" +
						" or extend " +
						superclass.getName() );
			}
			return;
        }
		
        String role = AnnotationMetadataSupport.findAttribute( paths , String.class , Extends.ROLE_ATTRIBUTE , null , false , true );
        BeanDefinitionHolder bdh = context.getAttachedBean( metadata , metadata.getArtifact() , role );
        
        Factory<Class<?>> superclsFactory = new ClassMetadataClassFactory( superclass );
        LazyClassBeanDefinitionSupport.getInterfaceImplementor( bdh ).setSuperclass( superclsFactory );
	}

}
