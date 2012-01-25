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
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * The Class FunctionFilterReader.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public abstract class BaseFilterReader extends FilterReader {

	protected BaseFilterReader(final Reader in) {
		super(in);
	}

	/**
	 * The current line. If null and emitNewline is false, a newline must be
	 * fetched.
	 */
	protected String currentLine;

	/**
	 * The index of the first unread character in curLine. If at any time
	 * <code>curLineIx == curLine.length</code>, <code>currentLine</code> is set
	 * to <code>null</code>.
	 */
	int currentLineIndex;

	/**
	 * If <code>true</code>, the newline at the end of currentLine has not been
	 * returned.
	 *
	 * It would have been more convenient to append the newline onto freshly
	 * fetched lines. However, that would incur another allocation and copy.
	 */
	boolean emitNewline;

	/** The buffered reader. */
	protected BufferedReader bufferedReader;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.FilterReader#read()
	 */
	@Override
	public int read() throws IOException {
		final char[] chars = new char[1];
		final int read = read(chars, 0, 1);
		if (read == -1) {
			return -1;

		}
		return chars[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.FilterReader#read(char[], int, int)
	 */
	@Override
	public int read(final char cbuf[], int off, final int len)
			throws IOException {

		// Fetch new line if necessary
		if (currentLine == null && !emitNewline) {
			setNextMatchingLine();
		}

		// Return characters from current line
		if (currentLine != null) {
			int num = Math.min(
					len,
					Math.min(cbuf.length - off, currentLine.length()
							- currentLineIndex));
			// Copy characters from curLine to cbuf
			for (int i = 0; i < num; i++) {
				cbuf[off++] = currentLine.charAt(currentLineIndex++);
			}

			// No more characters in curLine
			if (currentLineIndex == currentLine.length()) {
				currentLine = null;

				// Is there room for the newline?
				if (num < len && off < cbuf.length) {
					cbuf[off++] = '\n';
					emitNewline = false;
					num++;
				}
			}

			// Return number of character read
			return num;
		} else if (emitNewline && len > 0) {
			// Emit just the newline
			cbuf[off] = '\n';
			emitNewline = false;
			return 1;
		} else if (len > 0) {
			// No more characters left in input reader
			return -1;
		} else {
			// Client did not ask for any characters
			return 0;
		}
	}

	/**
	 * Gets the next line.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected abstract void setNextMatchingLine() throws IOException;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.FilterReader#ready()
	 */
	@Override
	public boolean ready() throws IOException {
		return currentLine != null || emitNewline || in.ready();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.FilterReader#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void close() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}

		super.close();
	}
}
