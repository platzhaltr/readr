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

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

/**
 * The Class ChainFunction.
 *
 * @param <T>
 *            the generic type
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class ChainFunction<T> implements Function<T, T> {

	/** The functions. */
	private final List<Function<T, T>> functions;

	/**
	 * Instantiates a new chained function.
	 *
	 * @param functions
	 *            the functions
	 */
	public ChainFunction(final Function<T, T>... functions) {
		this(Arrays.asList(functions));
	}

	/**
	 * Instantiates a new chain function.
	 *
	 * @param functions
	 *            the functions
	 */
	public ChainFunction(final List<Function<T, T>> functions) {
		this.functions = functions;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public T apply(final T input) {
		T o = input;
		for (final Function<T, T> function : functions) {
			o = function.apply(o);
		}
		return o;
	}

}