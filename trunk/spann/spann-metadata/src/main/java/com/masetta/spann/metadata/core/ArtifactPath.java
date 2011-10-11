
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

package com.masetta.spann.metadata.core;

import java.util.Iterator;

/**
 * Immutable path of artifacts, representing a parent-child path. For example Class-Method-Parameter.
 * 
 * ArtifactPath can not be created directly. Instead, use the EMPTY_PATH constant
 * and {@link ArtifactPath#append(Metadata) append} method. 
 * 
 * @author Ron Piterman
 */
public final class ArtifactPath implements Iterable<Metadata> {
    
    /** Constant <code>EMPTY_PATH</code> */
    public static final ArtifactPath EMPTY_PATH = new ArtifactPath();
    
    private final Metadata metadata;
    
    private final ArtifactPath parent;
    
    private ArtifactPath() {
        this.metadata = null;
        this.parent = null;
    }
    
    private ArtifactPath( ArtifactPath parent, Metadata metadata ) {
        this.metadata = metadata;
        this.parent = parent;
    }
    
    /**
     * Create a new path, appending the given metadata object to the end of this path.
     *
     * @param metadata a {@link Metadata} to append to this path.
     * @return a new ArtifactPath, which is the result of appending the given metadata to this path.
     */
    public ArtifactPath append( Metadata metadata ) {
        return new ArtifactPath( this , metadata );
    }
    
    /**
     * Retrieve the size of this path.
     * 
     * @return the size of this path.
     */
    public int size() {
        if ( parent == null )
            return 0;
        return 1 + parent.size();
    }
    
    /**
     * Create an iterator for this path.
     * 
     * @return a new iterator for this path.
     */
    public Iterator<Metadata> iterator() {
        return new PathIterator( this );
    }
    
    /**
     * Retrieve the last element of the path.
     *
     * @return The last metadata element in the path.
     */
    public Metadata getMetadata() {
        return this.metadata;
    }

    /**
     * Retrieve the parent path of this path. The parent path represents
     * all elements of this path excluding the last element.
     *
     * @return the parent path, representing all elements of this path excluding the last one, or null
     * 	if this path is empty.
     */
    public ArtifactPath getParent() {
        return parent;
    }
    
    private static class PathIterator implements Iterator<Metadata> {
        
        private ArtifactPath path;
        
        public PathIterator(ArtifactPath path) {
            super();
            this.path = path;
        }

        public boolean hasNext() {
            return path.size() > 0;
        }

        public Metadata next() {
            ArtifactPath next = this.path;
            this.path = this.path.getParent();
            return next.metadata;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "ArtifactPath [" + toStringInternal( "" ) + "]";
    }
    
    private String toStringInternal( String prefix ) {
        return metadata != null ? prefix + metadata + parent.toStringInternal(",") : ""; 
    }

}
