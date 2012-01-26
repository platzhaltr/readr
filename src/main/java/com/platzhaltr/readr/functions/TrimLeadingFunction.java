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

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;

/**
 * The Class TrimLeadingFunction.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class TrimLeadingFunction implements Function<String, String> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(final String input) {
		return CharMatcher.WHITESPACE.and(CharMatcher.isNot(' '))
				.trimTrailingFrom(input);
	}
}