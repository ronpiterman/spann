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

package com.masetta.spann.spring.integration.synadapter;

import org.springframework.stereotype.Component;

import com.masetta.spann.spring.base.method.ClassTypeProperty;
import com.masetta.spann.spring.base.method.SyntheticAdapter;
import com.masetta.spann.spring.core.annotations.VisitMethods;

@Component("ab")
@VisitMethods
public class AdapterBean {
	
	@SyntheticAdapter(implement=Extractor.class)
	public String extract( Float f ) {
		return f.toString();
	}
	
	@SyntheticAdapter(implement=ExtractorImpl.class,classTypeProperties={
		@ClassTypeProperty(index=-1,name="returnType"),@ClassTypeProperty(index=0,name="argumentType")
	})
	public String extract( Long l ) {
		return l.toString();
	}
	
	@SyntheticExtractor(adapterBeanName="classExtractor")
	public Class<?> extract( Object o ) {
		return o.getClass();
	}

}
