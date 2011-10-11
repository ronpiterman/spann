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

package com.masetta.spann.spring.integration.replace;

import com.masetta.spann.spring.base.Implement;
import com.masetta.spann.spring.base.method.Replace;

@Implement
public abstract class BeanAbstract implements ReplacedMethods{
	
	@Replace( methodReplacer=MethodReplacerImpl.class )
	public abstract String replacedByClass(String string, int i, float f);

	@Replace( methodReplacerBean="methodReplacerBean")
	public abstract String replacedBySpringBean(String string, int i, float f);

	public String notReplacedImplemented(String string, int i, float f) {
		return string;
	}

	public abstract String notReplacedNotImplemented(String string, int i, float f);

}
