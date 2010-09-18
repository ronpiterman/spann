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

import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.reader.VisitorAdapter;

public class MethodVisitorImpl extends AbstractVisitor<MethodMetadataImpl> {
    
    private final VisitorAdapter<AnnotationVisitorImpl> annotationVisitor;

    public MethodVisitorImpl( VisitorController controller , VisitorAdapter<AnnotationVisitorImpl> annotationVisitor ) {
        super( MethodMetadataImpl.class, controller);
        this.annotationVisitor = annotationVisitor;
    }

    public VisitorAdapter<AnnotationVisitorImpl> visitAnnotationDefault() {
        visit( Object.class , getMetadata() , ArtifactElement.DEFAULT_VALUE );
        return annotationVisitor;
    }
    
    public VisitorAdapter<AnnotationVisitorImpl> visitParameterAnnotation(int parameter,
            String desc, boolean visible) { 
        if ( ! visible )
            return getEmptyAnnotationVisitor();
        if ( ! isVisit( ArtifactElement.PARAMETER_ANNOTATIONS ) )
            return getEmptyAnnotationVisitor();        
        ClassMetadata annotationCls = getClassMetadataFromType( desc, true );
        final ParameterMetadataImpl param = (ParameterMetadataImpl) getMetadata().getParameters().get( parameter );
        AnnotationMetadata m = ((AnnotatedElementMetadataImpl)param).getOrCreateAnnotation( param , annotationCls );
        return visit( AnnotationVisitorImpl.class , m, ArtifactElement.PARAMETER_ANNOTATIONS );
    }

}
