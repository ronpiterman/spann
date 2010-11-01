
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
 *
 * @author rpt
 * @version $Id: $
 */

package com.masetta.spann.metadata.core.support;

import java.lang.reflect.Array;
import java.util.List;

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.util.Predicate;
public abstract class AnnotationMetadataSupport {
	
	private AnnotationMetadataSupport() {}
	
	public static enum ResolutionPolicy {
		FULL, STRING
	}

	/**
	 * If the given annotatedElement has the given annotation, at any meta-annotation level.
	 * <p>
	 *
	 * @param metadata a {@link com.masetta.spann.metadata.core.AnnotatedElementMetadata} object.
	 * @param annotationClassName a {@link java.lang.String} object.
	 * @return true if the given element has the given (meta-)annotation. Otherwise false.
	 */
	public static boolean isMetaAnnotationPresent(AnnotatedElementMetadata metadata,
			String annotationClassName) {
		return ! metadata.findAnnotationPaths( annotationClassName ).isEmpty();
	}
	
	/**
	 * Find an attribute value in a list of attribute paths.
	 * <p>
	 * Looks up a list of attribute paths for an annotation attribute
	 * by name, and either return the first adequate value found or check that
	 * in all paths either predicated or consistent (same) values are present.
	 * <p>
	 * The order of search is reverse to the path: closer values to the
	 * annotated metadata are searched first.
	 *
	 * @param <T>
	 *            the attribute value type
	 * @param paths
	 *            the annotation paths to lookup the attribute value.
	 * @param type
	 *            the attribute value type
	 * @param attribute
	 *            the name of the annotation attribute to search for.
	 * @param ignore
	 *            a predicate which returns 'true' if a given value should be
	 *            ignored.
	 * @param checkConsistency
	 *            if to check that all values of the given attribute in all
	 *            given paths are consistent, thus either predicated or same.
	 * @param fallbackToDefault
	 *            if to use the default value if the attribute is not found.
	 *            When this is true, the given attribute name must be an
	 *            attribute of the target annotation of the first annotation
	 *            path in the paths list.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T findAttribute(List<AnnotationPath> paths, Class<T> type, String attribute,
			Predicate<T> ignore, boolean checkConsistency, boolean fallbackToDefault) {
	
		T value = null;
		for (AnnotationPath path : paths) {
	
			Object raw = getAttribute(path, attribute);
			if ( raw == null || !type.isAssignableFrom(raw.getClass()) ) {
				continue;
			}
	
			if ( ignore != null && ignore.evaluate((T) raw) )
				continue;
	
			if ( checkConsistency ) {
				if ( value != null && !value.equals(raw) ) {
					throw new IllegalArgumentException( String.format(
						"Inconsistent attribute value : '%s' vs. '%s' in annotation path: '%s'",
						value, raw, path));
				}
			} else {
				return (T) raw;
			}
			value = (T) raw;
		}
		if ( fallbackToDefault && value == null && paths.size() > 0 ) {
			value = paths.get(0).getAttribute(0, type, attribute, true);
		}
		return value;
	}
	
	private static Object getAttribute(AnnotationPath path, String attribute) {
		for (int i = path.getPath().length - 1; i >= 0; i--) {
			Object value = path.getAttribute(i, attribute, false);
			if ( value != null ) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Resolve the given attribute value, as returned by the {@link AnnotationMetadata}
	 * to a java object.
	 * <p>
	 * Does not support attribute values of type {@link AnnotationMetadata} and
	 * {@link AnnotationMetadata}[].
	 * <p>
	 * If the given value is an array, the following rules regarding the array type apply:
	 * <p>
	 * If the value was retrieved using a typed getter ({@link AnnotationMetadata#getAttribute(Class, String, boolean)})
	 * the returned array will be correctly typed.
	 * <p>
	 * For EnumValue arrays (which are used by AnnotationMetadata to represent Enum values),
	 * only non empty arrays values guarantee a fully compatible array type.
	 * For empty EnumValue arrays values this method will return an empty Enum array (<code>new Enum[0]</code>).
	 *
	 * @param value the attribute value.
	 * @param classResolutionPolicy how to resolve {@link ClassMetadata} attribute values.
	 * 		If FULL, class attribute value will be resolved to the corresponding
	 * 		java.lang.Class object. If STRING the full qulified name will be returned.
	 * @param enumResolutionPolicy how to resolve {@link EnumValue}s: If FULL, the return
	 * 		value will be the Enum Constant itself. If STRING, {@link EnumValue}s will be
	 * 		resolved to the corresponding enum's constant name.
	 * @throws java.lang.IllegalArgumentException if the given value is of type {@link AnnotationMetadata} or
	 * 	{@link AnnotationMetadata}[].
	 * @return a {@link java.lang.Object} object.
	 */
	@SuppressWarnings("unchecked")
	public static Object resolveAttributeValue( Object value , ResolutionPolicy classResolutionPolicy,
			ResolutionPolicy enumResolutionPolicy ) {
		
		Class<? extends Object> valueType = value.getClass();
		if ( valueType.isArray() ) {
			Class<?> targetComponentType = valueType.getComponentType();
			int length = Array.getLength( value );
			
			if ( AnnotationMetadata.class.isAssignableFrom( targetComponentType ) ) {
				throw new IllegalArgumentException("Can not resolve attribute values of type Annotation[]");
			}
			else if ( ClassMetadata.class.isAssignableFrom( targetComponentType ) ) {
				targetComponentType = ResolutionPolicy.FULL.equals( classResolutionPolicy ) ?
						 Class.class : String.class;
			}
			else if ( EnumValue.class.isAssignableFrom( targetComponentType ) ) {
				if ( ResolutionPolicy.FULL.equals( enumResolutionPolicy ) ) {
					if ( length == 0 ) {
						targetComponentType = Enum.class;
					}
					else {
						EnumValue v = (EnumValue) ((Object[])value)[0];
						targetComponentType = ClassMetadataSupport.resolve( v.getEnumType() );
					}
				}
				else {
					targetComponentType= String.class;
				}
			}
			Object array = Array.newInstance( targetComponentType , length );
			for ( int i = 0; i < length; i++ ) {
				Array.set( array, i, resolveAttributeValue( Array.get( value, i ) ,
						classResolutionPolicy , enumResolutionPolicy ) );
			}
			return array;
		}
		else {
			if ( value instanceof AnnotationMetadata ) {
				throw new IllegalArgumentException("Can not resolve attribute values of type Annotation.");
			}
			else if ( value instanceof ClassMetadata ) {
				switch ( classResolutionPolicy ) {
					case FULL: return ClassMetadataSupport.resolve( (ClassMetadata) value );
					case STRING: return ((ClassMetadata)value).getName();
					default:
						throw new IllegalArgumentException("Unknown classResolutionPolicy:" + classResolutionPolicy );
				}
			}
			else if ( value instanceof EnumValue ) {
				switch ( enumResolutionPolicy ) {
					case FULL: 
						@SuppressWarnings("rawtypes")
						Class cls = ClassMetadataSupport.resolve( ((EnumValue)value).getEnumType() ); 
						return ((EnumValue)value).resolve( cls );
					case STRING:
						return ((EnumValue)value).getValue();
					default:
						throw new IllegalArgumentException("Unknown enumResolutionPolicy:" + enumResolutionPolicy );
				}
			}
			else return value;
		}
	}

}
