package com.platzhaltr.readr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class FilteredReaderMakerTest {

	/** The Constant PATH_SIMPLE. */
	private static final String PATH_SIMPLE = "/flatfile.filter.simple.txt";

	@Test
	public void test() throws IOException {
		final File input = new File(this.getClass().getResource(PATH_SIMPLE)
				.getFile());

		final MogrifiedReaderMaker maker = new MogrifiedReaderMaker();
		maker.omitLines().startingWith("#");
		maker.omitLines().containing("needle");
		final Reader reader = maker.read(input);

		final List<String> lines = readAsList(reader);

		assertNotNull(lines);
		assertFalse(lines.isEmpty());
		assertTrue(lines.size() == 1);
		assertEquals("foo", lines.get(0));

	}

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
