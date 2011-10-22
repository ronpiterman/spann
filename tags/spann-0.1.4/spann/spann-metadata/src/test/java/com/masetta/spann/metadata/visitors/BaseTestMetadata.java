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

package com.masetta.spann.metadata.visitors;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.masetta.spann.metadata.MetadataStore;
import com.masetta.spann.metadata.MetadataStoreImpl;
import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.common.ClasspathResource;
import com.masetta.spann.metadata.common.Resource;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.FieldMetadataSupport;
import com.masetta.spann.metadata.reader.ClassReaderAdapter;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactory;
import com.masetta.spann.metadata.rules.MetadataPathRules;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.metadata.rules.MetadataPathRulesImpl;
import com.masetta.spann.metadata.visitors.VisitorControllerImpl;

public abstract class BaseTestMetadata { //implements ITest {
    
    private static String pkg = BaseTestMetadata.class.getPackage().getName() + ".classes";
    
    private LazyLoadingRulesFactory lazyLoadingRulesFactory;
    
    private MetadataPathRules rules;
    
    private String rulesMode = "eager";
    
    private ClassMetadata metadata;
    
    private String simpleClassName;
    
    private Integer fields, methods, interfaces, typeParams;
    
    private ClassReaderAdapter classReaderAdapter = new SpringClassReaderAdapter();
    
    public BaseTestMetadata(String simpleClassName ) {
        super();
        this.simpleClassName = simpleClassName;
    }
    
    protected static String getTestClassName( String simpleName ) {
        return pkg + "." + simpleName;
    }

    protected ClassMetadata readClassMetadata(final String simpleClassName) {
        return getMetadata(simpleClassName, getRules(), lazyLoadingRulesFactory, classReaderAdapter );
    }

    public static ClassMetadata getMetadata(final String simpleClassName,
            MetadataPathRules rules, final LazyLoadingRulesFactory lazyLoadingRulesFactory2,
            ClassReaderAdapter adapter ) {
        final String className = getTestClassName( simpleClassName );
        MetadataStore store = new MetadataStoreImpl();
        VisitorControllerImpl vc = new VisitorControllerImpl( store , rules , lazyLoadingRulesFactory2 ,
        		adapter );
        Resource resource = ClasspathResource.forClassname( className , 
                BaseTestMetadata.class.getClassLoader() );
        try {
        	vc.visit( resource );
        } catch ( RuntimeException ex ) {
        	ex.printStackTrace();
        	throw ex;
        }
        
        ClassMetadata metadata = store.getByClassname( className, 0 );
        return metadata;
    }

    private MetadataPathRules getRules() {
        if ( this.rules != null )
            return this.rules;
        
        MetadataPathRulesBuilder b = new MetadataPathRulesBuilder();
        b.add( Artifact.CLASS, null ).load( ArtifactElement.ANNOTATIONS );
        b.add( Artifact.FIELD , null).load( ArtifactElement.ANNOTATIONS );
        b.add( Artifact.METHOD, null ).load( ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS );
        
        MetadataPathRulesImpl rules = b.build();
        return rules;
    }
    
    @BeforeClass
    public void readMetadata() {
        this.metadata = readClassMetadata( simpleClassName ); 
    }
    
    @BeforeMethod(alwaysRun=true)
    public void methodStart( Method ctx ) {
        System.out.println( "====================================================================");
        System.out.println( ctx.getName() );
        System.out.println( "====================================================================");
    }
    
    @BeforeMethod(alwaysRun=true)
    public void checkStats() {
        if ( fields == null ) {
            fields = getSize( getMetadata().getFields() );
            methods = getSize( getMetadata().getMethods() );
            interfaces = getSize( getMetadata().getInterfaces(true) );
            typeParams = getSize( getMetadata().getTypeParameters() );
        }
        Assert.assertEquals( getSize( getMetadata().getFields() ), fields.intValue() , "#fields");
        Assert.assertEquals( getSize( getMetadata().getMethods() ), methods.intValue(), "#methods");
        Assert.assertEquals( getSize( getMetadata().getInterfaces(true) ), interfaces.intValue(), "#interfaces");
        Assert.assertEquals( getSize( getMetadata().getTypeParameters() ), typeParams.intValue(), "#type-params");
    }
    
    private int getSize( Collection<?> c) {
        if ( c == null )
            return 0;
        return c.size();
    }

    protected <T> T firstOrNull(Collection<T> c) {
        if ( c == null )
            return null;
        Iterator<T> i = c.iterator();
        if ( i.hasNext() )
            return i.next();
        return null;
    }
    
    public void setRules(MetadataPathRules rules, String name ) {
        this.rules = rules;
        this.rulesMode = name;
    }

    public String getTestName() {
        return getClass().getCanonicalName() + ":" + rulesMode;
    }

    protected ClassMetadata getMetadata() {
        if ( this.metadata == null )
            readMetadata();
        return metadata;
    }

    public void setLazyLoadingRulesFactory(
            LazyLoadingRulesFactory lazyLoadingRulesFactory) {
        this.lazyLoadingRulesFactory = lazyLoadingRulesFactory;
    }
    
    protected GenericType getFieldType( String name ) {
        FieldMetadata fm = FieldMetadataSupport.findField( getMetadata(), name );
        Assert.assertNotNull( fm );
        GenericType t = fm.getFieldType();
        Assert.assertNotNull( t );
        return t;
    }
    
    protected MethodMetadata getMethodMetadata(String methodName ) {
        for ( MethodMetadata mm : getMetadata().getMethods() ) {
            if ( mm.getName().equals( methodName ) )
                return mm;
        }
        throw new IllegalArgumentException("Method " + methodName + " not found.");
    }

	public void setClassReaderAdapter(ClassReaderAdapter classReaderAdapter) {
		this.classReaderAdapter = classReaderAdapter;
	}
	
	@AfterClass
	public void cleanup() {
		this.metadata = null;
	}

}
