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

package com.masetta.spann.spring.util.type;

import java.lang.reflect.Array;

import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

public abstract class AbstractCglibImplementor implements Implementor {
    
    protected final static Class<?>[] DEFAULT_CALLBACK_TYPES = new Class[] { NoOp.INSTANCE.getClass() };
    
    private Class<?>[] additionalInterfaces = null;
    
    private CallbackFilter callbackFilter = null;
    
    private Class<?>[] callbackTypes = DEFAULT_CALLBACK_TYPES;
    
    protected void setupCallbacks(Enhancer enhancer) {
        if ( callbackFilter != null ) {
            enhancer.setCallbackFilter( callbackFilter );
        }
        enhancer.setCallbackTypes( callbackTypes );
    }
    
    /** 
     * Retrieve the joined array of {@link #additionalInterfaces} and the given ifc. 
     * 
     * @param ifc
     * @return
     */
    protected Class<?>[] getInterfaces(Class<?> ifc ) {
        if ( isEmpty( additionalInterfaces ) ) {
            return new Class[] { ifc };
        }
        if ( contains( additionalInterfaces, ifc ) ) { 
            return getAdditionalInterfaces();
        }
        return push( getAdditionalInterfaces() , ifc );
    }

    @SuppressWarnings("unchecked")
	private <T> T[] push(T[] array, T newMember ) {
        T[] newArray = (T[]) Array.newInstance( array.getClass().getComponentType(), array.length + 1 );
        System.arraycopy( array, 0, newArray, 0, array.length );
        newArray[ array.length ] = newMember;
        return newArray;
    }

    private <T> boolean contains(T[] array, T needle) {
        for ( T t : array ) {
            if ( t.equals( needle ) )
                return true;
        }
        return false;
    }

    /**
     * Additional intrfaces that the generated class should implement.
     * @return
     */
    public Class<?>[] getAdditionalInterfaces() {
        return additionalInterfaces;
    }

    /**
     * Set additional interfaces that the generated class should implement.
     * @param additionalInterfaces
     */
    public void setAdditionalInterfaces(Class<?>[] additionalInterfaces) {
        this.additionalInterfaces = additionalInterfaces;
    }

    /**
     * CallbackFilter to use.
     * @return
     */
    public CallbackFilter getCallbackFilter() {
        return callbackFilter;
    }

    /**
     * Set the callback filter and the callback types to use.
     * If callbackTypes contains a only one element, callbackFilter should be null.
     * If callbackTypes contains more than one element, callbackFilter should not be null.
     * 
     * See cglib documentation for details.
     * 
     * @param callbackFilter
     * @param callbackTypes
     */
    public void setCallbacks(CallbackFilter callbackFilter, Class<?>[] callbackTypes ) {
        if ( isEmpty( callbackTypes ) )
            throw new IllegalArgumentException("callback types must not be null/empty.");
        if ( callbackTypes.length == 1 ) {
            if ( callbackFilter != null ) {
                throw new IllegalArgumentException("callbackFilter should only be specified if" +
                        " using more than one callbackType;" +
                        " but callbackTypes.length < 2 and callbackFilter != null.");
            }
        }
        else if ( callbackFilter == null ) {
            throw new IllegalArgumentException("callbackFilter should not be null if using" +
                    " more than one callbackType;" +
                    " but callbackTypes.length >= 2 and callbackFilter == null.");
        }
        this.callbackFilter = callbackFilter;
        this.callbackTypes = callbackTypes;
    }

    /**
     * Callback types to use.
     * @return
     */
    public Class<?>[] getCallbackTypes() {
        return callbackTypes;
    }
    
    protected final  boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    
    public void addInterface( Class<?> ifc ) {
        if ( isEmpty( additionalInterfaces ) )
            this.additionalInterfaces = new Class[] { ifc };
        if ( contains( additionalInterfaces, ifc ) )
            return;
        this.additionalInterfaces = push( this.additionalInterfaces , ifc );
    }
    
}
