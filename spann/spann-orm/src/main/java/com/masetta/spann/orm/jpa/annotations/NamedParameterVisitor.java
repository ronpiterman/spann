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

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.orm.jpa.beans.callbacks.QueryArgumentsHandlers;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.util.Handler;

public class NamedParameterVisitor extends AbstractGenericReplacerAnnotationVisitor {

	private static final String NAMED_PARAMETER = NamedParameter.class.getCanonicalName();

	public NamedParameterVisitor() {
		super(NAMED_PARAMETER, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		String[] names = path.getAttribute( 0, String[].class, "value", false );
		@SuppressWarnings("rawtypes")
		Handler setter = new QueryArgumentsHandlers.Named( names );
		GenericMethodReplacerSupport.setCallContextVisitorsBuilderCallback(
				callContextVisitorsFactoryBean, -1 , setter );
	}

}
