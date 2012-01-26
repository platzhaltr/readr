/*
 * Copyright 2011 Oliver Schrenk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.platzhaltr.readr;

/**
 * The Class Mogrifier.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class Mogrifier {

	/** The object. */
	private final Object object;

	/** The type. */
	private final Type type;

	/**
	 * Instantiates a new mogrifier.
	 *
	 * @param object
	 *            the object
	 * @param type
	 *            the type
	 */
	protected Mogrifier(final Object object, final Type type) {
		super();
		this.object = object;
		this.type = type;
	}

	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * The Enum Type.
	 *
	 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
	 */
	protected static enum Type {

		/** The PREDICATE. */
		PREDICATE,

		/** The FUNCTION. */
		FUNCTION;
	}

}
