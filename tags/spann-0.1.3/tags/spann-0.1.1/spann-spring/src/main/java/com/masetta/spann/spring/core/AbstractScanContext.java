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

package com.masetta.spann.spring.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.BeanDefinitionBuilder;
import com.masetta.spann.spring.ScanContext;

public abstract class AbstractScanContext implements ScanContext {
	
	protected final SpannLog log = SpannLogFactory.getLog( AbstractScanContext.class );
	
	private final BeanDefinitionDefaults beanDefinitionDefaults;

    private final Set<String> beanNames;

    private final Set<BeanDefinitionHolder> beans;
    
    private final Map<Metadata,Map<String,BeanDefinitionHolder>> beanDefinitionsMap
    	= new HashMap<Metadata, Map<String,BeanDefinitionHolder>>();
    
    public AbstractScanContext( Set<BeanDefinitionHolder> beans , BeanDefinitionDefaults beanDefinitionDefaults ) {
        super();
        this.beans = beans;
        this.beanNames = new HashSet<String>();
        this.beanDefinitionDefaults = beanDefinitionDefaults;
        for (BeanDefinitionHolder h : beans) {
            beanNames.add(h.getBeanName());
        }
    }
    
    public BeanDefinitionBuilder builder( Metadata metadata , String classname , Object source ) {
    	BeanDefinition def = createDefaultBeanDefinition( source, classname );
    	return (BeanDefinitionBuilder) SpannLogFactory.createTraceProxy( 
    			new BeanDefinitionBuilderImpl( this, metadata ,def ),
    			log , BeanDefinitionBuilder.class );
    }

    public BeanDefinition createDefaultBeanDefinition(Object source,
            String className) {
        GenericBeanDefinition b = new GenericBeanDefinition();
        b.applyDefaults( beanDefinitionDefaults );
        
        b.setSource( source );
        b.setBeanClassName( className );
        
        return b;
    }


    public void attach(BeanDefinitionHolder holder, Metadata element,
            Artifact scope, String role) {
        // 1. attach to role
        attach(element, role, holder, "role");

        // 2. attach to the scope
        Metadata scopeMetadata = getScopeElement(element, scope);
        attach(scopeMetadata, holder.getBeanDefinition().getBeanClassName(),
                holder, "class name");
        
    }

    private Metadata getScopeElement( Metadata element, Artifact scope) {
        if ( scope == null || Artifact.UNDEFINED.equals( scope ) )
            return null;
        
        if ( Artifact.UNKNOWN.equals( scope ) ) {
        	return element;
        }
        
        Metadata e = element;
        while ( e != null && ! e.getArtifact().equals( scope ) ) {
        	e = e.getParent();
        }
        if ( e == null ) {
        	throw new IllegalArgumentException("Scope " + scope + " not found in path of " + element );
        }
        return e;
    }

    private void attach( Metadata element, String role,
            BeanDefinitionHolder holder, final String roleName) {
    	if ( log.isDebugEnabled() ) {
    		log.debug("Attaching " + holder + " to element " + element  + " with " + 
    				roleName + " " + role );
    	}
        Map<String, BeanDefinitionHolder> allByElement = getAllByElement(element);
        if (allByElement.containsKey(role)) {
            throw new IllegalArgumentException("BeanDefinitionHolder with "
                    + roleName + " " + role + " already attached to "
                    + "element " + element + ". " +
                    		"\nAttached BDHs: " + allByElement.get(role));
        }

        allByElement.put(role, holder);
        add(holder);
    }

    private Map<String, BeanDefinitionHolder> getAllByElement( Metadata element ) {
        Map<String, BeanDefinitionHolder> all = this.beanDefinitionsMap
                .get(element);
        if (all == null) {
            all = new HashMap<String, BeanDefinitionHolder>();
            this.beanDefinitionsMap.put(element, all);
        }
        return all;
    }

    private void add(final BeanDefinitionHolder beanDefHolder) {
        beans.add(beanDefHolder);
        beanNames.add(beanDefHolder.getBeanName());
    }

    public void addFinal(BeanDefinitionHolder beanDefinitionHolder) {
        add(beanDefinitionHolder);
    }

    protected boolean isBeanNameAvailable(String beanName) {
        if (this.beanNames.contains(beanName))
            return false;
        for (BeanDefinitionHolder defHolder : beans) {
            if (defHolder != null && beanName.equals(defHolder.getBeanName()))
                return false;
        }
        return true;
    }

    public BeanDefinitionHolder getAttachedBean( Metadata metadata, Artifact scope, String beanRole) {
        Metadata element = getScopeElement( metadata, scope);
        if (beanRole == null) {
            Collection<BeanDefinitionHolder> all = getAllByElement(element)
                    .values();
            if ( all.size() > 1 )
                throw new IllegalArgumentException(
                        "Attempt to get BeanDefinitionHolder by role null, but more than"
                                + " one BeanDefinitionHolder is attached to "
                                + element);
            Iterator<BeanDefinitionHolder> i = all.iterator();
            if (i.hasNext())
                return i.next();
            return null;
        }

        return getAllByElement(element).get(beanRole);
    }
    
    public String toString() {
    	return "AbstractScanContext";
    }

}
