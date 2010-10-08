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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.orm.jpa.beans.callbacks.ConsumeHandler;
import com.masetta.spann.orm.jpa.beans.factories.JpqlDynamicQueryFactory;
import com.masetta.spann.orm.jpa.beans.factories.JpqlSimpleQueryFactory;
import com.masetta.spann.orm.jpa.beans.factories.NamedQueryFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;

public class JpqlVisitor extends AbstractGenericReplacerAnnotationVisitor {

	private static final String JPQL_QUERY = Jpql.class.getCanonicalName();

	private static final Pattern USE_MESSAGE = Pattern.compile("\\{\\d+\\}");

	private static final Integer[] ZERO_TO_29 = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 };

	public JpqlVisitor() {
		super(JPQL_QUERY, true);
	}

	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		String query = path.getAttribute( 0, String.class, "value", false );
		MessageFormat format = createMessageFormat( query ) ;
		
		BeanDefinitionHolder dao = DaoVisitor.getDao( context , metadata );
		
		BeanDefinitionHolder h = null;
		if ( format != null ) {
			int[] argsToConsume = getMessageArguments( format );
			h = context.builder( metadata, 
					JpqlDynamicQueryFactory.class.getCanonicalName() , path )
				.set( JpqlDynamicQueryFactory.QUERY_PROPERTY, query )
				.set( NamedQueryFactory.ENTITY_MANAGER_SUPPORT_PROPERTY , dao )
				.addFinal(); 
			
			GenericMethodReplacerSupport.addCallContextVisitorsBuilderCallback( 
					callContextVisitorsFactoryBean, 0, new ConsumeHandler( argsToConsume ) );
		} else {
			h = context.builder( metadata, 
					JpqlSimpleQueryFactory.class.getCanonicalName() , path )
					.set( NamedQueryFactory.QUERY_PROPERTY, query )
					.set( NamedQueryFactory.ENTITY_MANAGER_SUPPORT_PROPERTY , dao )
					.addFinal();
		}
		
		GenericMethodReplacerSupport.setCallContextFactory( methodReplacer, h );
	}

	private MessageFormat createMessageFormat(String xql) {
		if ( !USE_MESSAGE.matcher(xql).find() )
			return null;

		return new MessageFormat(xql);
	}

	/**
	 * Detect which arguments are used to consume them
	 * 
	 * @param messageFormat
	 * @return
	 */
	private int[] getMessageArguments(MessageFormat messageFormat) {
		String formatted = messageFormat.format(ZERO_TO_29);
		try {
			Object[] parsed = messageFormat.parse(formatted);
			int length = countNotNull(parsed);

			// actually should never be
			if ( length == 0 )
				return null;

			int[] consumeMethodArguments = new int[length];
			int i = 0;
			for (Object string : parsed) {
				if ( string != null ) {
					consumeMethodArguments[i++] = Integer.parseInt((String) string);
				}
			}

			return consumeMethodArguments;

		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private int countNotNull(Object[] parsed) {
		int count = 0;
		for (Object o : parsed) {
			if ( o != null )
				count++;
		}
		return count;
	}

}
