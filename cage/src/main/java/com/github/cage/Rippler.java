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

import java.awt.image.BufferedImage;

/**
 * A filter to generate ripple (wave) effected images. Uses a transformed sinus
 * wave for this. This class is thread safe.
 * 
 * @author akiraly
 * 
 */
public class Rippler {
	private final double start;

	private final double length;

	private final double amplitude;

	/**
	 * Constructor
	 * 
	 * @param start
	 *            the starting x offset to generate sinus values. Should be
	 *            between 0 and 2 * {@link Math#PI}.
	 * @param length
	 *            the length of x to be used to generate sinus values. Should be
	 *            between 0 and 4 * {@link Math#PI}.
	 * @param amplitude
	 *            the maximum y value, if it is too big, some important parts of
	 *            the image (like the text) can "wave" out on the top or on the
	 *            bottom of the image.
	 */
	public Rippler(double start, double length, double amplitude) {
		this.start = normalize(start, 2);
		this.length = normalize(length, 4);
		this.amplitude = amplitude;
	}

	/**
	 * Normalizes parameter to fall into [0, multi * {@link Math#PI}]
	 * 
	 * @param a
	 *            to be normalized
	 * @param multi
	 *            multiplicator used for end value
	 * @return normalized value
	 */
	protected double normalize(double a, int multi) {
		double piMulti = multi * Math.PI;

		a = Math.abs(a);
		double d = Math.floor(a / piMulti);

		return a - d * piMulti;
	}

	/**
	 * Draws a rippled (waved) variant of source into destination.
	 * 
	 * @param src
	 *            to be transformed, not null
	 * @param dest
	 *            to hold the result, not null
	 * @return dest is returned
	 */
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		int height = src.getHeight();
		double period = length / src.getWidth();
		double amplitude = src.getHeight() / 10.0;

		for (int x = 0; x < src.getWidth(); x++) {
			int delta = (int) Math.round(amplitude
					* Math.sin(start + x * period));
			for (int y = 0; y < height; y++) {
				int ny = (y + delta + height) % height;
				dest.setRGB(x, ny, src.getRGB(x, y));
			}
		}

		return dest;
	}

	public double getStart() {
		return start;
	}

	public double getLength() {
		return length;
	}

	public double getAmplitude() {
		return amplitude;
	}
}
