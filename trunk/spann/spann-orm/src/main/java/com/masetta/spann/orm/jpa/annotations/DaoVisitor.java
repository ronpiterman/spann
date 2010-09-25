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

package com.masetta.spann.orm.jpa.annotations;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class DaoVisitor extends AnnotationPathMetadataVisitor<ClassMetadata> {

	private static final String DAO = Dao.class.getCanonicalName();

	public DaoVisitor() {
		super(ClassMetadata.class, DAO, false);
	}

	@Override
	protected void visit( ClassMetadata metadata, ScanContext context, AnnotationPath path) {
		BeanDefinitionHolder dao = context.getAttachedBean( metadata, Artifact.CLASS, "main");
		
		// resolve entity
		ClassMetadata entity = ClassMetadataSupport.findTypeParameterCapture( metadata,
				BaseDao.class.getCanonicalName(), 0 );
		if ( entity == null ) {
			throw new IllegalConfigurationException( DAO , metadata , 
					"No generic argument specified for BaseDao<E,PK> - is the class / interface extending BaseDao?" );
		}
		DefSupport.setProperty( dao, "entityType" , entity.getName() );
				

	}

}
