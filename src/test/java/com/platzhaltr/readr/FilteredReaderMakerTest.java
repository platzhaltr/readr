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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FilteredReaderMakerTest {

	/** The Constant PATH_SIMPLE. */
	private static final String PATH_SIMPLE = "/file.simple.txt";

	/** The Constant PATH_SIMPLE. */
	private static final String PATH_SKIP_EMPTY = "/skip.empty.txt";

	/** The maker. */
	private MogrifiedReaderMaker maker;

	@Before
	public void setUp() {
		maker = new MogrifiedReaderMaker();
	}

	@Test
	public void test() throws IOException {

		maker.omitLines().startingWith("#");
		maker.omitLines().containing("needle");

		final List<String> lines = readAsList(maker.read(getFile(PATH_SIMPLE)));

		assertTrue(lines.size() == 1);
		assertEquals("foo", lines.get(0));
	}

	@Test
	public void testSkipEmptyLines() throws FileNotFoundException, IOException {
		maker.skipEmptyLines();

		final List<String> lines = readAsList(maker
				.read(getFile(PATH_SKIP_EMPTY)));

		assertTrue(lines.size() == 2);
		assertEquals("foo", lines.get(0));
		assertEquals("bar", lines.get(1));
	}

	/**
	 * Gets the file.
	 *
	 * @param path
	 *            the path
	 * @return the file
	 */
	private File getFile(final String path) {
		return new File(this.getClass().getResource(path).getFile());
	}

	/**
	 * Read as list.
	 *
	 * @param reader
	 *            the reader
	 * @return the list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static List<String> readAsList(final Reader reader)
			throws IOException {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final List<String> lines = new LinkedList<String>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}
}
