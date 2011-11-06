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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.core.visitor.VisitorSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;


public class ReplaceVisitor extends AnnotationPathMetadataVisitor<MethodMetadata> {
	
	private static final String REPLACE = Replace.class.getCanonicalName();
    
    public static final String METHOD_REPLACER_ROLE = "replacer";
    
    private SpannLog log = SpannLogFactory.getLog( ReplaceVisitor.class );
    
    public ReplaceVisitor() {
        super( MethodMetadata.class, REPLACE, true );
    }

	@Override
	protected void visit(MethodMetadata metadata, ScanContext context, AnnotationPath path) {
		BeanDefinition methodOwner = VisitorSupport.findBean(context,  metadata.getParent() );
        if ( methodOwner == null ) {
        	log.info("Skipping replace " + metadata + ".Class " + metadata.getParent().getName() + " is not a bean.");
        	return;
        }
        
        // create the replacer bean or reuse a previously created one
        // for the given scope.
        ClassMetadata replacer = path.getAttribute( 0  , ClassMetadata.class , Replace.METHOD_REPLACER_ATTRIBUTE , false );
        String replacerBean = path.getAttribute( 0 , String.class , Replace.METHOD_REPLACER_BEAN_ATTRIBUTE , false );
        if ( replacer != null && replacerBean != null ) {
        	throw new IllegalConfigurationException( REPLACE , metadata , 
        			Replace.METHOD_REPLACER_ATTRIBUTE + "() and " + Replace.METHOD_REPLACER_BEAN_ATTRIBUTE + "() " +
        					"attributes are mutual exclusive." );
        }
        
        EnumValue scopeValue = path.getAttribute( 0  , EnumValue.class , Replace.ATTACH_SCOPE_ATTRIBUTE , true );
        Artifact scope = scopeValue.resolve( Artifact.class );
        
        if ( replacerBean == null ) {
        	BeanDefinitionHolder methodReplacer = VisitorSupport.getOrCreateAndAttach( context, metadata, 
            		scope , METHOD_REPLACER_ROLE, this , replacer.getName() );
        	replacerBean = methodReplacer.getBeanName();
        }
        
        DefSupport.replaceMethod( methodOwner, metadata, replacerBean );
	}

}
