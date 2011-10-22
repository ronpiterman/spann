
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

package com.masetta.spann.metadata.common;

/**
 * RuntimeIOException - has the same semantics as IOException but does not require catching.
 * 
 * @author Ron Piterman
 */
public class RuntimeIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for RuntimeIOException.</p>
	 */
	public RuntimeIOException() {
		super();
	}

	/**
	 * <p>Constructor for RuntimeIOException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public RuntimeIOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for RuntimeIOException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public RuntimeIOException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for RuntimeIOException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public RuntimeIOException(Throwable cause) {
		super(cause);
	}

}
