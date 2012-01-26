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
import java.util.Stack;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.platzhaltr.readr.functions.ChainFunction;
import com.platzhaltr.readr.functions.RemovePrefixFunction;
import com.platzhaltr.readr.functions.ReplaceFunction;
import com.platzhaltr.readr.functions.TrimFunction;
import com.platzhaltr.readr.functions.TrimLeadingFunction;
import com.platzhaltr.readr.functions.TrimTrailingFunction;
import com.platzhaltr.readr.io.FunctionFilterReader;
import com.platzhaltr.readr.io.PredicateFilterReader;
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

	/** The stack. */
	private final Stack<Mogrifier> stack = new Stack<Mogrifier>();

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
		add(new EmptyPredicate());
		return this;
	}

	/**
	 * Trim lines.
	 *
	 * @return the mogrified reader maker
	 */
	public MogrifiedReaderMaker trim() {
		add(new TrimFunction());
		return this;
	}

	/**
	 * Trim trailing.
	 *
	 * @return the mogrified reader maker
	 */
	public MogrifiedReaderMaker trimTrailing() {
		add(new TrimTrailingFunction());
		return this;
	}

	/**
	 * Trim leading.
	 *
	 * @return the mogrified reader maker
	 */
	public MogrifiedReaderMaker trimLeading() {
		add(new TrimLeadingFunction());
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
	 * @param predicate
	 *            the predicate
	 */
	private void add(final Predicate<String> predicate) {
		stack.add(new Mogrifier(predicate, Mogrifier.Type.PREDICATE));
	}

	/**
	 * Adds the.
	 *
	 * @param function
	 *            the function
	 */
	private void add(final Function<String, String> function) {
		stack.add(new Mogrifier(function, Mogrifier.Type.FUNCTION));
	}

	/**
	 * Builds the.
	 *
	 * @param reader
	 *            the reader
	 * @return the reader
	 */
	public Reader wrap(final Reader reader) {
		if (!stack.isEmpty()) {
			final Mogrifier pop = stack.pop();
			final List<Object> objects = Lists.newLinkedList();
			objects.add(pop.getObject());
			return wrap(objects, pop.getType(), reader);
		}

		return reader;
	}

	/**
	 * Wrap.
	 *
	 * @param objects
	 *            the objects
	 * @param lastType
	 *            the last type
	 * @param reader
	 *            the reader
	 * @return the reader
	 */
	private Reader wrap(final List<Object> objects, Mogrifier.Type lastType,
			Reader reader) {

		while (!stack.empty()) {
			final Mogrifier pop = stack.pop();

			// the new mogrifier is not from the same type
			if (!pop.getType().equals(lastType)) {

				// add predicates
				if (lastType.equals(Mogrifier.Type.PREDICATE)) {
					reader = buildPredicateReader(objects, reader);
				}

				// functions
				else if (lastType.equals(Mogrifier.Type.FUNCTION)) {
					reader = buildFunctionReader(objects, reader);
				}

				// in any case we have to empty the list,
				// change the type
				lastType = pop.getType();
				objects.clear();
			}

			// we always have to add the current one to the list of objects
			objects.add(pop.getObject());

		}

		// lists of mogrifiers might not be empty
		if (!objects.isEmpty()) {
			// add predicates
			if (lastType.equals(Mogrifier.Type.PREDICATE)) {
				reader = buildPredicateReader(objects, reader);
			}

			// functions
			else if (lastType.equals(Mogrifier.Type.FUNCTION)) {
				reader = buildFunctionReader(objects, reader);
			}
		}

		return reader;
	}

	/**
	 * Builds the function reader.
	 *
	 * @param objects
	 *            the objects
	 * @param reader
	 *            the reader
	 * @return the reader
	 */
	private Reader buildFunctionReader(final List<Object> objects, Reader reader) {
		// multiple functions
		if (objects.size() > 1) {
			final List<Function<String, String>> functions = Lists
					.newLinkedList();
			for (final Object object : objects) {
				@SuppressWarnings("unchecked")
				final Function<String, String> function = (Function<String, String>) object;
				functions.add(function);
			}

			reader = new FunctionFilterReader(reader,
					new ChainFunction<String>(functions));

			// single function
		} else {
			@SuppressWarnings("unchecked")
			final Function<String, String> function = (Function<String, String>) objects
					.get(0);
			reader = new FunctionFilterReader(reader, function);
		}
		return reader;
	}

	/**
	 * Builds the predicate reader.
	 *
	 * @param objects
	 *            the objects
	 * @param reader
	 *            the reader
	 * @return the reader
	 */
	private Reader buildPredicateReader(final List<Object> objects,
			Reader reader) {
		// multiple predicates
		if (objects.size() > 1) {
			final List<Predicate<String>> predicates = Lists.newLinkedList();
			for (final Object object : objects) {
				@SuppressWarnings("unchecked")
				final Predicate<String> predicate = (Predicate<String>) object;
				predicates.add(predicate);
			}
			reader = new PredicateFilterReader(reader,
					Predicates.<String> not(Predicates.<String> or(predicates)));

			// a single predicate
		} else {
			@SuppressWarnings("unchecked")
			final Predicate<String> predicate = (Predicate<String>) objects
					.get(0);
			reader = new PredicateFilterReader(reader,
					Predicates.not(predicate));
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

			mogrifiedReaderMaker.add(new StartingWithPredicate(prefix));

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

			mogrifiedReaderMaker.add(new EndingWithPredicate(suffix));

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

			mogrifiedReaderMaker.add(new ContainingPredicate(needle));

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

			mogrifiedReaderMaker.add(new MatchingPredicate(regex));

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

			mogrifiedReaderMaker.add(new ReplaceFunction(oldString, newString));

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
			mogrifiedReaderMaker.add(new RemovePrefixFunction(prefix));

			return mogrifiedReaderMaker;
		}
	}

}
