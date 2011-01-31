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
	public static final char[][] ENGLISH_LETTERS = new char[][] {
			"aeiou".toCharArray(), "bcdfghjklmnpqrstxvwz".toCharArray() };

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
		this(rnd, 7);
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
		this(rnd, minLength, delta, ENGLISH_LETTERS);
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

	public int getMinLength() {
		return minLength;
	}

	public int getDelta() {
		return delta;
	}

}
