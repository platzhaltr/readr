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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * This class takes a {@link Reader} and a {@link Predicate} and keeps lines to
 * which the predicate applies. Line terminators are converted to a
 * <code>\n</code>.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class PredicateFilterReader extends BaseFilterReader {

	/** The line predicate. */
	private final Predicate<String> linePredicate;

	/**
	 * Instantiates a new comment filter reader.
	 *
	 * @param in
	 *            the in
	 */
	protected PredicateFilterReader(final Reader in) {
		this(in, Predicates.<String> alwaysTrue());
	}

	/**
	 * Instantiates a new comment filter reader.
	 *
	 * @param in
	 *            the reader
	 * @param linePredicate
	 *            the line predicate
	 */
	public PredicateFilterReader(final Reader in,
			final Predicate<String> linePredicate) {
		super(in);
		if (in instanceof BufferedReader) {
			bufferedReader = (BufferedReader) in;
		} else {
			bufferedReader = new BufferedReader(in);
		}
		this.linePredicate = linePredicate;
	}

	/**
	 * Gets the next line.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	protected void setNextMatchingLine() throws IOException {
		currentLine = bufferedReader.readLine();
		while (currentLine != null) {
			if (linePredicate.apply(currentLine)) {
				emitNewline = true;
				currentLineIndex = 0;
				return;
			}
			currentLine = bufferedReader.readLine();
		}
	}

}
