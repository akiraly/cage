/*
 * Copyright 2011 Király Attila
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cage;

import java.util.Random;

/**
 * A simple random String generator that can be used to generate tokens for the
 * captcha images.
 * 
 * In its default mode instances of this class generate words from English
 * letters where vowels and consonants are alternating.
 * 
 * @author akiraly
 * 
 */
public class RandomWordGenerator implements IGenerator<String> {
	/**
	 * Letter set for the Google template
	 */
	public static final char[][] G_LETTER_SET = new char[][] {
			"aeiou".toCharArray(), "bcdfghjklmnpqrstxvwz".toCharArray() };

	/**
	 * Letter set for the Yahoo template
	 */
	public static final char[][] Y_LETTER_SET = new char[][] { "abcdefghjkmnpqrstxuvwzABCDEFGHJKLMNPQRSTXUVWZ23456789"
			.toCharArray() };

	private final Random rnd;
	private final int minLength;
	private final int delta;
	private final char[][] letters;

	/**
	 * Constructor.
	 * 
	 * @param rnd
	 *            random generator to be used, can be null
	 */
	public RandomWordGenerator(Random rnd) {
		this(rnd, 8, 2);
	}

	/**
	 * Constructor.
	 * 
	 * @param length
	 *            the length of the generated words, must be > 0
	 * @param rnd
	 *            random generator to be used, can be null
	 */
	public RandomWordGenerator(Random rnd, int length) {
		this(rnd, length, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param minLength
	 *            the minimum length of the generated words, must be > 0
	 * @param delta
	 *            minLength + delta is the maximum length of the generated
	 *            words, delta must be >= 0
	 * @param rnd
	 *            random generator to be used, can be null
	 */
	public RandomWordGenerator(Random rnd, int minLength, int delta) {
		this(rnd, minLength, delta, G_LETTER_SET);
	}

	/**
	 * Constructor.
	 * 
	 * @param rnd
	 *            random generator to be used, can be null
	 * @param minLength
	 *            the minimum length of the generated words, must be > 0
	 * @param delta
	 *            minLength + delta is the maximum length of the generated
	 *            words, delta must be >= 0
	 * @param letters
	 *            the array of character arrays to be used for the generated
	 *            words. Neither the letters neither the char[]-s inside it can
	 *            not be null, or empty.
	 */
	public RandomWordGenerator(Random rnd, int minLength, int delta,
			char[][] letters) {
		super();
		this.minLength = Math.abs(minLength);
		this.delta = Math.abs(delta);
		this.letters = letters;
		this.rnd = rnd != null ? rnd : new Random();
	}

	public String next() {
		int first = rnd.nextInt(letters.length);
		int length = (delta <= 1 ? 0 : rnd.nextInt(delta) + 1) + minLength;
		char[] word = new char[length];

		for (int fi = 0; fi < word.length; fi++) {
			char[] currentLetters = letters[(first + fi) % letters.length];
			word[fi] = currentLetters[rnd.nextInt(currentLetters.length)];
		}

		return new String(word);
	}

	/**
	 * @return minimum length of generated tokens
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * @return maximum length difference to add to the minimum length
	 */
	public int getDelta() {
		return delta;
	}

}
