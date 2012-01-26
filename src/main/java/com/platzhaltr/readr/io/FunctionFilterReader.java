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
package com.platzhaltr.readr.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

/**
 * The Class FunctionFilterReader.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class FunctionFilterReader extends BaseFilterReader {

	/** The line transformer. */
	private final Function<String, String> lineTransformer;

	/**
	 * Instantiates a new line transformer filter reader.
	 *
	 * @param in
	 *            the in
	 */
	protected FunctionFilterReader(final Reader in) {
		this(in, Functions.<String> identity());
	}

	/**
	 * Instantiates a new line transformer filter reader.
	 *
	 * @param in
	 *            the in
	 * @param lineTransformer
	 *            the line transformer
	 */
	public FunctionFilterReader(final Reader in,
			final Function<String, String> lineTransformer) {
		super(in);
		if (in instanceof BufferedReader) {
			bufferedReader = (BufferedReader) in;
		} else {
			bufferedReader = new BufferedReader(in);
		}
		this.lineTransformer = lineTransformer;
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
			if ((currentLine = lineTransformer.apply(currentLine)) != null) {
				emitNewline = true;
				currentLineIndex = 0;
				return;
			}
			currentLine = bufferedReader.readLine();
		}
	}

}
