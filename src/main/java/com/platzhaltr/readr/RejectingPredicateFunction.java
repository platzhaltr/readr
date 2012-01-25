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

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * The Class RejectingPredicateFunction.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class RejectingPredicateFunction implements Function<String, String> {

	/** The predicate. */
	private final Predicate<String> predicate;

	/** The inner function. */
	private final Function<String, String> innerFunction;

	/**
	 * Instantiates a new predicate function.
	 *
	 * @param predicate
	 *            the predicate
	 * @param innerFunction
	 *            the inner function
	 */
	public RejectingPredicateFunction(final Predicate<String> predicate,
			final Function<String, String> innerFunction) {
		super();
		this.predicate = predicate;
		this.innerFunction = innerFunction;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(final String line) {
		if (line == null)
			return null;

		if (predicate.apply(line)) {
			return innerFunction.apply(line);
		}

		return null;
	}
}
