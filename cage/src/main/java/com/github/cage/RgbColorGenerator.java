package com.github.cage;

import java.awt.Color;
import java.util.Random;

/**
 * Random RGB {@link Color} object generator. The returned {@link Color}-s are
 * not too bright so they look good on white background. This class is thread
 * safe.
 * 
 * @author akiraly
 */
public class RgbColorGenerator implements IGenerator<Color> {
	private final Random rnd;

	/**
	 * Constructor
	 * 
	 * @param rnd
	 *            random generator to be used, can be null
	 */
	public RgbColorGenerator(Random rnd) {
		this.rnd = rnd != null ? rnd : new Random();
	}

	public Color next() {
		int c[] = new int[3];

		int i = rnd.nextInt(c.length);

		for (int fi = 0; fi < c.length; fi++)
			if (fi == i)
				c[fi] = rnd.nextInt(81);
			else
				c[fi] = rnd.nextInt(256);

		return new Color(c[0], c[1], c[2]);
	}

}
