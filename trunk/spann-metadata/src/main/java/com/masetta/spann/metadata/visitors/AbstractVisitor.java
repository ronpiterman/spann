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

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EmptyMetadataArrays;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.reader.VisitorAdapter;

public class AbstractVisitor<T extends AbstractMetadataImpl> implements VisitEndSupport {
    
    private final Class<T> metadataType;
    
    private final VisitorController controller;
    
    private final VisitorDelegate visitorDelegate;
    
    private final VisitorAdapter<AnnotationVisitorImpl> emptyAnnotationVisitor;
    
    @SuppressWarnings("unchecked")
	public AbstractVisitor(Class<T> metadataType, VisitorController controller ) {
        super();
        this.metadataType = metadataType;
        this.controller = controller;
        this.visitorDelegate = controller.getDelegate();
        this.emptyAnnotationVisitor = (VisitorAdapter<AnnotationVisitorImpl>) 
        	controller.getEmptyVisitorAdapter( Artifact.ANNOTATION );
    }

    public <V> VisitorAdapter<V> visit( Class<V> visitorType, Metadata metadata, ArtifactElement element ) {
        return controller.visit( visitorType , metadata , element );
    }
    
    protected T getMetadata() {
        return controller.getCurrentMetadata( metadataType );
    }
    
    protected ClassMetadata getClassMetadata( String classname , int dimensions ) {
        return controller.getClassMetadata( classname , dimensions );
    }
    
    protected ClassMetadata[] getClassMetadata( String[] classNames ) {
        if ( classNames == null || classNames.length == 0 )
            return EmptyMetadataArrays.CLASS_METADATA;
        ClassMetadata[] result = new ClassMetadata[classNames.length];
        for ( int i = 0; i < classNames.length ; i++ )
            result[i] = getClassMetadata( classNames[i] , 0 );
        return result;
    }
    
    public void visitEnd() {
        controller.visitEnd();
    }
    
    public VisitorAdapter<AnnotationVisitorImpl> visitAnnotation(String desc, boolean visible) {
        if ( ! visible )
            return emptyAnnotationVisitor;
        if ( ! isVisit( ArtifactElement.ANNOTATIONS ) )
            return emptyAnnotationVisitor;
        AnnotationMetadata metadata = createAnnotationMetadata(desc);
        return visit( AnnotationVisitorImpl.class , metadata , ArtifactElement.ANNOTATIONS );
    }

    protected AnnotationMetadata createAnnotationMetadata(String desc) {
        ClassMetadata annotationCls = getClassMetadataFromType( desc, true );
        AnnotatedElementMetadataImpl parent = (AnnotatedElementMetadataImpl) getMetadata();
        return parent.getOrCreateAnnotation( parent, annotationCls );
    }
    
    protected ClassMetadata getClassMetadataFromType(String desc, boolean ignoreArray) {
        return this.visitorDelegate.resolveDescriptor(desc, ignoreArray);
    }

    protected boolean isVisit(ArtifactElement e ) {
        return this.controller.isVisit( e );
    }
    
    protected VisitorDelegate getVisitorDelegate() {
        return this.visitorDelegate;
    }

	public VisitorAdapter<AnnotationVisitorImpl> getEmptyAnnotationVisitor() {
		return emptyAnnotationVisitor;
	}

}
