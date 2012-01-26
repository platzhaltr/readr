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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.platzhaltr.readr.functions.ChainFunction;
import com.platzhaltr.readr.functions.RejectingPredicateFunction;
import com.platzhaltr.readr.functions.RemovePrefixFunction;
import com.platzhaltr.readr.functions.ReplaceFunction;
import com.platzhaltr.readr.predicates.ContainingPredicate;
import com.platzhaltr.readr.predicates.EmptyPredicate;
import com.platzhaltr.readr.predicates.EndingWithPredicate;
import com.platzhaltr.readr.predicates.MatchingPredicate;
import com.platzhaltr.readr.predicates.StartingWithPredicate;

/**
 * The Class MogrifiedReaderMaker.
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 */
public class MogrifiedReaderMaker {

	/** The functions. */
	private final List<Function<String, String>> functions = Lists
			.newLinkedList();

	/** The omit lines builder. */
	private final OmitLinesBuilder omitLinesBuilder;

	/** The transform lines builder. */
	private final TransformLinesBuilder transformLinesBuilder;

	/**
	 * Instantiates a new mogrified reader maker.
	 */
	public MogrifiedReaderMaker() {
		omitLinesBuilder = new OmitLinesBuilder(this);
		transformLinesBuilder = new TransformLinesBuilder(this);
	}

	/**
	 * Skip empty lines.
	 *
	 * @return the mogrified reader maker
	 */
	public MogrifiedReaderMaker skipEmptyLines() {
		add(new RejectingPredicateFunction(
				Predicates.not(new EmptyPredicate()),
				Functions.<String> identity()));
		return this;
	}

	/**
	 * Omit lines.
	 *
	 * @return the omit lines builder
	 */
	public OmitLinesBuilder omitLines() {
		return omitLinesBuilder;
	}

	/**
	 * Transform lines.
	 *
	 * @return the transform lines builder
	 */
	public TransformLinesBuilder transformLines() {
		return transformLinesBuilder;
	}

	/**
	 * Adds the.
	 *
	 * @param function
	 *            the function
	 */
	protected void add(final Function<String, String> function) {
		functions.add(function);
	}

	/**
	 * Builds the.
	 *
	 * @param reader
	 *            the reader
	 * @return the reader
	 */
	public Reader wrap(final Reader reader) {
		if (!functions.isEmpty()) {
			return new FunctionFilterReader(reader, new ChainFunction<String>(
					functions));
		}

		return reader;
	}

	/**
	 * Read.
	 *
	 * @param file
	 *            the file
	 * @return the reader
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public Reader read(final File file) throws FileNotFoundException {
		return wrap(new FileReader(file));
	}

	/**
	 * The Class OmitLinesBuilder.
	 *
	 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
	 */
	public static class OmitLinesBuilder {

		/** The mogrified reader maker. */
		private final MogrifiedReaderMaker mogrifiedReaderMaker;

		/**
		 * Instantiates a new omit lines builder.
		 *
		 * @param mogrifiedReaderMaker
		 *            the mogrified reader maker
		 */
		private OmitLinesBuilder(final MogrifiedReaderMaker mogrifiedReaderMaker) {
			this.mogrifiedReaderMaker = mogrifiedReaderMaker;
		}

		/**
		 * Starting with.
		 *
		 * @param prefix
		 *            the prefix
		 * @return the filtered reader maker
		 */
		public MogrifiedReaderMaker startingWith(final String prefix) {

			mogrifiedReaderMaker.add(new RejectingPredicateFunction(Predicates
					.not(new StartingWithPredicate(prefix)), Functions
					.<String> identity()));

			return mogrifiedReaderMaker;
		}

		/**
		 * Ending with.
		 *
		 * @param suffix
		 *            the suffix
		 * @return the filtered reader maker
		 */
		public MogrifiedReaderMaker endingWith(final String suffix) {

			mogrifiedReaderMaker.add(new RejectingPredicateFunction(Predicates
					.not(new EndingWithPredicate(suffix)), Functions
					.<String> identity()));

			return mogrifiedReaderMaker;
		}

		/**
		 * Containing.
		 *
		 * @param needle
		 *            the needle
		 * @return the filtered reader maker
		 */
		public MogrifiedReaderMaker containing(final String needle) {

			mogrifiedReaderMaker.add(new RejectingPredicateFunction(Predicates
					.not(new ContainingPredicate(needle)), Functions
					.<String> identity()));

			return mogrifiedReaderMaker;
		}

		/**
		 * Matching.
		 *
		 * @param regex
		 *            the regex
		 * @return the filtered reader maker
		 */
		public MogrifiedReaderMaker matching(final String regex) {

			mogrifiedReaderMaker.add(new RejectingPredicateFunction(Predicates
					.not(new MatchingPredicate(regex)), Functions
					.<String> identity()));

			return mogrifiedReaderMaker;
		}

	}

	/**
	 * The Class TransformLinesBuilder.
	 *
	 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
	 */
	public static class TransformLinesBuilder {

		/** The mogrified reader maker. */
		private final MogrifiedReaderMaker mogrifiedReaderMaker;

		/** The functions. */
		private final List<Function<String, String>> functions = Lists
				.newLinkedList();

		/**
		 * Instantiates a new transform lines builder.
		 *
		 * @param mogrifiedReaderMaker
		 *            the mogrified reader maker
		 */
		public TransformLinesBuilder(
				final MogrifiedReaderMaker mogrifiedReaderMaker) {
			this.mogrifiedReaderMaker = mogrifiedReaderMaker;
		}

		/**
		 * By replacing.
		 *
		 * @param oldString
		 *            the old string
		 * @param newString
		 *            the new string
		 * @return the mogrified reader maker
		 */
		public MogrifiedReaderMaker byReplacing(final String oldString,
				final String newString) {

			functions.add(new ReplaceFunction(oldString, newString));

			return mogrifiedReaderMaker;
		}

		/**
		 * By removing prefix.
		 *
		 * @param prefix
		 *            the prefix
		 * @return the mogrified reader maker
		 */
		public MogrifiedReaderMaker byRemovingPrefix(final String prefix) {
			functions.add(new RemovePrefixFunction(prefix));

			return mogrifiedReaderMaker;
		}
	}

}
