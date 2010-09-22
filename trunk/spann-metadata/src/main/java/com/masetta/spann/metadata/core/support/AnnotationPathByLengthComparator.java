
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

package com.masetta.spann.metadata.core.support;

import java.io.Serializable;
import java.util.Comparator;

import com.masetta.spann.metadata.core.AnnotationPath;
public final class AnnotationPathByLengthComparator implements Comparator<AnnotationPath> , Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	/** Constant <code>INSTANCE</code> */
	public static final Comparator<AnnotationPath> INSTANCE = new AnnotationPathByLengthComparator();
	
	private AnnotationPathByLengthComparator() {}

	/**
	 * <p>compare</p>
	 *
	 * @param o1 a {@link com.masetta.spann.metadata.core.AnnotationPath} object.
	 * @param o2 a {@link com.masetta.spann.metadata.core.AnnotationPath} object.
	 * @return a int.
	 */
	public int compare(AnnotationPath o1, AnnotationPath o2) {
		int l1 = o1.getPath().length;
		int l2 = o2.getPath().length;
		return l1 > l2 ? 1 : l1 < l2 ? -1 : 0;
	}

}
