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

package com.masetta.spann.orm.jpa.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.ListableBeanFactory;

import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.spring.util.Resolver;

/**
 * Utility class to help test dao methods.
 * <p>
 * 
 * @author Ron Piterman
 */
public final class DaoTestSupport {
	
	public static final Object SKIP = new Object();
	
	public static final Object NULL = new Object();
	
	private static final Resolver<Object,Object> SAME = new Resolver<Object, Object>() {
		public Object resolve(Object param) {
			return param;
		}
	};
	
	private static final Resolver<Object,Object> LIST = new Resolver<Object, Object>() {
		public Object resolve(Object param) {
			return Collections.singletonList( param );
		}
	};
	
	private static final Resolver<Object,Object> SET = new Resolver<Object, Object>() {
		public Object resolve(Object param) {
			return Collections.singleton( param );
		}
	};

	private DaoTestSupport() {}
	
	/**
	 * Create callbacks for all dao methods of all BaseDao implementations in
	 * the given pacakgaes.
	 * <p>
	 * The given resolver should resolve any class used as argument in any dao's method
	 * to an object of that type, with the following rules:
	 * <ol>
	 * 	<li>Never return null. If null value is needed, use DaoTestSupport.NULL
	 * 	<li>If unable to deliver a value for a given class (say, a persistent entity),
	 *   you can skip all methods using this type of argument by returning DaoTestSupport.SKIP
	 *   for that type.
	 * </ol>
	 * 
	 * @param factory bean factory to get the beans from.
	 * @param parameterResolver resolver to generate method arguments by type.
	 * @param packages
	 * @return
	 */
	public static List<DaoMethodInvoker> createCallbacks( ListableBeanFactory factory , Resolver<Object,Class<?>> parameterResolver , Package ...packages ) {
		Map<String,? extends BaseDao<?,?>> all = factory.getBeansOfType( BaseDao.class );
		List<DaoMethodInvoker> invokers = new ArrayList<DaoMethodInvoker>();
		for ( BaseDao<?,?> dao : all.values() ) {
			process( dao , parameterResolver , packages, invokers );
		}
		return invokers;
	}

	private static void process(BaseDao<?, ?> dao, Resolver<Object, Class<?>> parameterResolver,
			Package[] packages , List<DaoMethodInvoker> invokers ) {
		Class<?> daoClass = findDaoClass( dao.getClass() );
		if ( daoClass == null )
			return;
		
		if ( ! isInPackages( daoClass , packages ) ) {
			return;
		}
		
		for ( Method method : daoClass.getDeclaredMethods() ) {
			if ( isAnnotationPresent( method, DaoMethod.class , new HashSet<AnnotatedElement>()) ) {
				DaoMethodInvoker c = createInvoker( daoClass, method , dao , parameterResolver );
				if  ( c != null )
					invokers.add( c );
			}
 		}
		
	}

	private static boolean isAnnotationPresent(AnnotatedElement e, Class<? extends Annotation> ann , java.util.Set<AnnotatedElement> visited ) {
		if ( ! visited.add( e ) )
			return false;
		
		if ( e.isAnnotationPresent( ann ) )
			return true;
		
		for ( Annotation a : e.getAnnotations() ) {
			if ( isAnnotationPresent( a.annotationType() , ann , visited ) )
				return true;
		}
		return false;
	}

	private static DaoMethodInvoker createInvoker(Class<?> daoClass, Method method, BaseDao<?, ?> dao,
			Resolver<Object, Class<?>> parameterResolver) {
		Object[] arguments = createArguments( method , parameterResolver );
		if ( arguments == null )
			return null;
		return new DaoMethodInvoker( daoClass , method , dao , arguments );
	}

	private static Object[] createArguments(Method method, Resolver<Object, Class<?>> parameterResolver) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		Object[] arguments = new Object[ parameterTypes.length ];
		for ( int i = 0; i < arguments.length; i++ ) {
			Object arg = null;
			Resolver<Object,Object> wrapper = SAME;
			Class<?> type = parameterTypes[i]
			                                     ;
			if ( Collection.class.isAssignableFrom( type ) ) {
				type = getCollectionElementType( method , i );
				if ( type == null ) {
					return null;
				}
				wrapper = getWrapperFor( type );
			}
			
			arg = parameterResolver.resolve( type );
			if ( arg == null ) {
				throw new IllegalArgumentException("Could not resolve method argument for " + type + 
						". Resolver returned null. Use DaoTestSupport.NULL for null argument value.");
			}
				
			if ( arg == SKIP ) {
				return null;
			}
			
			if ( arg == NULL ) {
				arg = null;
			}
			
			arguments[i] = wrapper.resolve( arg );
		}
		return arguments;
	}

	private static Resolver<Object, Object> getWrapperFor(Class<?> type) {
		if ( Set.class.isAssignableFrom( type ) )
			return SET;
		return LIST;
	}

	private static Class<?> getCollectionElementType(Method method, int i) {
		Type type = method.getGenericParameterTypes()[i];
		if ( ! ( type instanceof ParameterizedType ) ) 
			return null;
		Type t1 = ((ParameterizedType)type).getActualTypeArguments()[0];
		return extractElementClass( t1 );
	}

	private static Class<?> extractElementClass(Type t1) {
		if ( t1 instanceof Class )
			return (Class<?>) t1;
		if ( t1 instanceof WildcardType ) {
			Type[] ts = ((WildcardType)t1).getLowerBounds();
			if ( ts.length > 0 ) {
				return extractElementClass( ts[0] );
			}
			ts = ((WildcardType)t1).getUpperBounds();
			if ( ts.length > 0 ) {
				return extractElementClass( ts[0] );
			}
			return null;
		}
		if ( t1 instanceof ParameterizedType ) {
			return extractElementClass(((ParameterizedType)t1).getRawType() );
		}
		return null;
	}

	private static boolean isInPackages(Class<?> dao, Package[] packages) {
		for ( Package p : packages ) {
			if ( p.equals( dao.getPackage() ) )
				return true;
		}
		return false;
	}

	private static Class<?> findDaoClass( Class<?> dao ) {
		if ( dao == null ) {
			return null;
		}
		
		if ( dao.isAnnotationPresent( Dao.class ) ) {
			return dao;
		}
		
		Class<?> spr = findDaoClass( dao.getSuperclass() );
		if ( spr != null  )
			return spr;
		
		for ( Class<?> ifc : dao.getInterfaces() ) {
			spr = findDaoClass( ifc );
			if ( spr != null )
				return spr;
		}
		
		return null;
	}

}
