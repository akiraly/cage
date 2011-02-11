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

import java.awt.Color;
import java.util.Locale;
import java.util.Random;

import com.github.cage.image.ConstantColorGenerator;
import com.github.cage.image.Painter;
import com.github.cage.token.RandomCharacterGeneratorFactory;
import com.github.cage.token.RandomTokenGenerator;

/**
 * Creates and configures a {@link Cage} instance that can generate captcha
 * images similar to Yahoo's. This is the "Y" template. Simply create an
 * instance with <code>new YCage()</code> and you can generate images. See
 * {@link Cage} for more info.
 * 
 * This class is thread safe.
 * 
 * @author akiraly
 */
public class YCage extends com.github.cage.Cage {
	/**
	 * Character set supplied to the {@link RandomTokenGenerator} used by this
	 * template.
	 */
	public static final char[] TOKEN_DEFAULT_CHARACTER_SET = (new String(
			RandomCharacterGeneratorFactory.DEFAULT_DEFAULT_CHARACTER_SET)
			.replaceAll("b|i|j|l|o", "")
			+ new String(
					RandomCharacterGeneratorFactory.DEFAULT_DEFAULT_CHARACTER_SET)
					.replaceAll("i|o", "").toUpperCase(Locale.ENGLISH) + new String(
			RandomCharacterGeneratorFactory.ARAB_NUMBERS).replaceAll("0|1|9",
			"")).toCharArray();

	/**
	 * Constructor.
	 */
	public YCage() {
		this(new Random());
	}

	/**
	 * Constructor.
	 * 
	 * @param rnd
	 *            object used for random value generation. Not null.
	 */
	protected YCage(Random rnd) {
		super(new Painter(290, 80, null, null, true, true, true, false, rnd),
				null, new ConstantColorGenerator(Color.BLACK), null,
				Cage.DEFAULT_COMPRESS_RATIO, new RandomTokenGenerator(rnd,
						new RandomCharacterGeneratorFactory(
								TOKEN_DEFAULT_CHARACTER_SET, null, rnd), 6, 2),
				rnd);
	}
}