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

package com.masetta.spann.spring.core.annotations;

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.spring.core.visitor.VisitCallback;
import com.masetta.spann.spring.util.Handler;

public class VisitMethodsCallback implements VisitCallback<ClassMetadata> {

	public void perform(Handler<AnnotatedElementMetadata> visitor, ClassMetadata metadata) {
		for ( MethodMetadata methodMetadata : metadata.getMethods() ) {
			visitor.handle( methodMetadata );
    	}
	}

}
