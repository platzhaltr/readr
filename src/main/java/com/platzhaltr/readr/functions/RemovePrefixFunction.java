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
package com.platzhaltr.readr.functions;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

/**
 * The Class RemovePrefixFunction.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class RemovePrefixFunction implements Function<String, String> {

	/** The prefixes. */
	private final Set<String> prefixes;

	/**
	 * Instantiates a new prefix remover line transformer.
	 *
	 * @param prefix
	 *            the prefix
	 */
	public RemovePrefixFunction(final String prefix) {
		this(Sets.newHashSet(prefix));
	}

	/**
	 * Instantiates a new prefix remover line transformer.
	 *
	 * @param prefixes
	 *            the prefixes
	 */
	public RemovePrefixFunction(final String... prefixes) {
		this(Sets.newHashSet(prefixes));
	}

	/**
	 * Instantiates a new prefix remover line transformer.
	 *
	 * @param prefixes
	 *            the prefixes
	 */
	public RemovePrefixFunction(final Set<String> prefixes) {
		this.prefixes = prefixes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(final String line) {
		for (final String prefix : prefixes) {
			if (line.startsWith(prefix)) {
				return line.substring(prefix.length());
			}
		}
		return line;
	}

}
