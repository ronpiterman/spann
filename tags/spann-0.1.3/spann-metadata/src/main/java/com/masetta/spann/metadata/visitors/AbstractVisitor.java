
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
    
    /**
     * <p>Constructor for AbstractVisitor.</p>
     *
     * @param metadataType a {@link java.lang.Class} object.
     * @param controller a {@link com.masetta.spann.metadata.visitors.VisitorController} object.
     */
    @SuppressWarnings("unchecked")
	public AbstractVisitor(Class<T> metadataType, VisitorController controller ) {
        super();
        this.metadataType = metadataType;
        this.controller = controller;
        this.visitorDelegate = controller.getDelegate();
        this.emptyAnnotationVisitor = (VisitorAdapter<AnnotationVisitorImpl>) 
        	controller.getEmptyVisitorAdapter( Artifact.ANNOTATION );
    }

    /**
     * <p>visit</p>
     *
     * @param visitorType a {@link java.lang.Class} object.
     * @param metadata a {@link com.masetta.spann.metadata.core.Metadata} object.
     * @param element a {@link com.masetta.spann.metadata.common.ArtifactElement} object.
     * @param <V> a V object.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public <V> VisitorAdapter<V> visit( Class<V> visitorType, Metadata metadata, ArtifactElement element ) {
        return controller.visit( visitorType , metadata , element );
    }
    
    /**
     * <p>getMetadata</p>
     *
     * @return a T object.
     */
    protected T getMetadata() {
        return controller.getCurrentMetadata( metadataType );
    }
    
    /**
     * <p>getClassMetadata</p>
     *
     * @param classname a {@link java.lang.String} object.
     * @param dimensions a int.
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    protected ClassMetadata getClassMetadata( String classname , int dimensions ) {
        return controller.getClassMetadata( classname , dimensions );
    }
    
    /**
     * <p>getClassMetadata</p>
     *
     * @param classNames an array of {@link java.lang.String} objects.
     * @return an array of {@link com.masetta.spann.metadata.core.ClassMetadata} objects.
     */
    protected ClassMetadata[] getClassMetadata( String[] classNames ) {
        if ( classNames == null || classNames.length == 0 )
            return EmptyMetadataArrays.CLASS_METADATA;
        ClassMetadata[] result = new ClassMetadata[classNames.length];
        for ( int i = 0; i < classNames.length ; i++ )
            result[i] = getClassMetadata( classNames[i] , 0 );
        return result;
    }
    
    /**
     * <p>visitEnd</p>
     */
    public void visitEnd() {
        controller.visitEnd();
    }
    
    /**
     * <p>visitAnnotation</p>
     *
     * @param desc a {@link java.lang.String} object.
     * @param visible a boolean.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<AnnotationVisitorImpl> visitAnnotation(String desc, boolean visible) {
        if ( ! visible )
            return emptyAnnotationVisitor;
        if ( ! isVisit( ArtifactElement.ANNOTATIONS ) )
            return emptyAnnotationVisitor;
        AnnotationMetadata metadata = createAnnotationMetadata(desc);
        return visit( AnnotationVisitorImpl.class , metadata , ArtifactElement.ANNOTATIONS );
    }

    /**
     * <p>createAnnotationMetadata</p>
     *
     * @param desc a {@link java.lang.String} object.
     * @return a {@link com.masetta.spann.metadata.core.AnnotationMetadata} object.
     */
    protected AnnotationMetadata createAnnotationMetadata(String desc) {
        ClassMetadata annotationCls = getClassMetadataFromType( desc, true );
        AnnotatedElementMetadataImpl parent = (AnnotatedElementMetadataImpl) getMetadata();
        return parent.getOrCreateAnnotation( parent, annotationCls );
    }
    
    /**
     * <p>getClassMetadataFromType</p>
     *
     * @param desc a {@link java.lang.String} object.
     * @param ignoreArray a boolean.
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    protected ClassMetadata getClassMetadataFromType(String desc, boolean ignoreArray) {
        return this.visitorDelegate.resolveDescriptor(desc, ignoreArray);
    }

    /**
     * <p>isVisit</p>
     *
     * @param e a {@link com.masetta.spann.metadata.common.ArtifactElement} object.
     * @return a boolean.
     */
    protected boolean isVisit(ArtifactElement e ) {
        return this.controller.isVisit( e );
    }
    
    /**
     * <p>Getter for the field <code>visitorDelegate</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.visitors.VisitorDelegate} object.
     */
    protected VisitorDelegate getVisitorDelegate() {
        return this.visitorDelegate;
    }

	/**
	 * <p>Getter for the field <code>emptyAnnotationVisitor</code>.</p>
	 *
	 * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
	 */
	public VisitorAdapter<AnnotationVisitorImpl> getEmptyAnnotationVisitor() {
		return emptyAnnotationVisitor;
	}

}
