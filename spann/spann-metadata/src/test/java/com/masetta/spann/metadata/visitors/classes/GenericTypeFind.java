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

public class GenericTypeFind {
	
	public interface Aux<S> {}
	
	public class AuxImpl<S> implements Aux<S> {};
	
	public interface Super<T,V,X> {}
	
	public static interface Sub<T1,V1,X1> extends Super<T1,V1,X1> {}

	public static interface Mix<A extends Aux<String>,B extends Aux<Integer>, C extends Aux<Boolean>> 
		extends Sub<C,B,A> {}
	
	public static interface Submix extends Mix<AuxImpl<String>,AuxImpl<Integer>,AuxImpl<Boolean>> {}
}
