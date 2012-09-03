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
package com.platzhaltr.readr.predicates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;

/**
 * The Class MatchingPredicate.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class MatchingPredicate implements Predicate<String> {

	/** The pattern. */
	private final Pattern pattern;

	/**
	 * Instantiates a new matching predicate.
	 *
	 * @param regex
	 *            the regex
	 */
	public MatchingPredicate(final String regex) {
		pattern = Pattern.compile(regex);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(final String line) {
		final Matcher matcher = pattern.matcher(line);
		return matcher.matches();
	}

}
