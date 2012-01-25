package com.platzhaltr.readr.examples;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.platzhaltr.readr.MogrifiedReaderMaker;

public class UsageExample {

	public void test(final File file) throws IOException {
		final MogrifiedReaderMaker maker = new MogrifiedReaderMaker();
		maker.omitLines().startingWith("#");
		maker.omitLines().containing("needle");
		maker.transformLines().byReplacing("foobar", " foobaz");
		final Reader reader = maker.read(file);
	}

}
