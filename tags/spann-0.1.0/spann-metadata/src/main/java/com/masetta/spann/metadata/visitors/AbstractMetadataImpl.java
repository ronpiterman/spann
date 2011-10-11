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
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.util.EqualsBuilder;

abstract class AbstractMetadataImpl implements Metadata {
    
    private final ArtifactPath path;
    
    private final Artifact artifact;
    
    private final String name;
    
    private boolean lazyloading;
    
    private int loaded;
    
    AbstractMetadataImpl( ArtifactPath parent , Artifact type , String name ) {
        this.artifact = type;
        this.name = name;
        this.path = parent.append( this );
    }
    
    public ArtifactPath getPath() {
        return path;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public String getName() {
        return name;
    }
    
    protected boolean load( ArtifactElement e ) {
        if ( ! lazyloading && ! isLoaded(e) ) {
            lazyloading = true;
            ArtifactPath pathToCls = path;
            
            while ( ! (pathToCls.getMetadata() instanceof ClassMetadataImpl) )
                pathToCls = pathToCls.getParent();
            ((ClassMetadataImpl)pathToCls.getMetadata() ).load( path , e );
            
            loaded(e);
            lazyloading = false;
        }
        return true;
    }
    
    protected void ignore( Object o ) {}

    boolean isLoaded(ArtifactElement e) {
        return (loaded & e.getBit()) != 0;
    }

    protected void loaded(ArtifactElement e) {
        loaded = loaded | e.getBit();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((artifact == null) ? 0 : artifact.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EqualsBuilder b = new EqualsBuilder();
        return equals( (AbstractMetadataImpl) obj , b );
    }

    protected boolean equals(AbstractMetadataImpl obj, EqualsBuilder b) {
        return b.eq( path ,obj.getPath() );
    }    
    
    public String toString() {
        return artifact.name() + " Metadata " + "[name=" + name +"]";
    }
    
    public Metadata getParent() {
    	return this.path.getParent().getMetadata();
    }
}
