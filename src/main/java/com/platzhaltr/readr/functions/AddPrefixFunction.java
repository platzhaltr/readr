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

import com.google.common.base.Function;

/**
 * The Class AddPrefixFunction.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class AddPrefixFunction implements Function<String, String> {

	/** The prefix. */
	private final String prefix;

	/**
	 * Instantiates a new prefix injector line transformer.
	 *
	 * @param prefix
	 *            the prefix
	 */
	public AddPrefixFunction(final String prefix) {
		super();
		this.prefix = prefix;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(final String line) {
		return prefix.concat(line);
	}

}
