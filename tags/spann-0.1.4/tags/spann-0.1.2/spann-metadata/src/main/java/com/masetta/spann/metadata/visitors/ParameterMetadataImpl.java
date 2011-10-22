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
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.ParameterMetadata;

class ParameterMetadataImpl extends AnnotatedElementMetadataImpl implements ParameterMetadata {
    
    private final ClassMetadata parameterClass;
    
    private GenericType parameterType;
    
    private MethodMetadataImpl parent;
    
    ParameterMetadataImpl(MethodMetadataImpl parent, ClassMetadata parameterType ) {
        super(parent.getPath(), Artifact.PARAMETER, "" );
        this.parameterClass = parameterType;
        this.parent = parent;
    }

    public GenericType getParameterType() {
        parent.load( ArtifactElement.SIGNATURE );
        return parameterType;
    }

    void setParameterType(GenericType type) {
        this.parameterType = type;
    }

    public ClassMetadata getParameterClass() {
        return parameterClass;
    }
    
    @Override
    protected void loadAnnotations() {
        parent.load( ArtifactElement.PARAMETER_ANNOTATIONS);
    }
    
    @Override
    public String toString() {
    	return "Parameter [" + getParameterClass().getName() + "]";
    }

}
