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

package com.masetta.spann.metadata.visitors.classes;

import com.masetta.spann.metadata.visitors.annotations.Child;
import com.masetta.spann.metadata.visitors.annotations.Grandchild;
import com.masetta.spann.metadata.visitors.annotations.Parent;
import com.masetta.spann.metadata.visitors.annotations.Super;

@Grandchild
@Super
public class AnnotatedClassForPaths {
	
	@Super("annSuper")
	public void annSuper() {}
	
	@Parent("annParent")
	public void annParent() {}
	
	@Child("annChild")
	public void annChild() {}
	
	@Grandchild("annGrandchild")
	public void annGrandchild() {}

	@Super("annSuperParent")
	@Parent("annSuperParent")
	public void annSuperParent() {}
	
	@Child("annChildGrandchild")
	@Grandchild("annChildGrandchild")
	public void annChildGrandchild() {}

	@Super("annAll")
	@Parent("annAll")
	@Child("annAll")
	@Grandchild("annAll")
	public void annAll() {}

}
