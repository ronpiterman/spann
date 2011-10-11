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

package com.masetta.spann.metadata.rules;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;

public class MetadataStab implements Metadata {
    
    private Artifact artifact;
    
    private String name;

    public Artifact getArtifact() {
        return artifact;
    }

    public String getName() {
        return name;
    }

    public MetadataStab(Artifact artifact, String name) {
        super();
        this.artifact = artifact;
        this.name = name;
    }

    public ArtifactPath getPath() {
        return null;
    }

	public Metadata getParent() {
		return null;
	}

}
