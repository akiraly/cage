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
package com.github.cage.image;

import java.awt.Color;
import java.awt.Font;

import org.junit.Assert;
import org.junit.Test;

import com.github.cage.image.Painter.Quality;

/**
 * Performance test for image generation ({@link Painter}). This might not pass
 * on all computers.
 * 
 * @author akiraly
 * 
 */
public class PainterSpeedTest {
	private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN,
			Painter.DEFAULT_HEIGHT / 2);

	private final static int warmUpNum = 1000;

	private final static int sampleNum = 5000;

	/**
	 * Tests with {@link Quality#DEFAULT} setting.
	 */
	@Test
	public void testDefaultQuality() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, Quality.DEFAULT, null, null);

		warmAndTest(painter, "default quality", 5.5);
	}

	/**
	 * Tests with {@link Quality#MIN} setting.
	 */
	@Test
	public void testMinQuality() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, Quality.MIN, null, null);

		warmAndTest(painter, "min quality", 5.5);
	}

	/**
	 * Tests with default settings.
	 */
	@Test
	public void testDefault() {
		final Painter painter = new Painter();

		warmAndTest(painter, "default", 4.5);
	}

	/**
	 * Tests with ripple disabled.
	 */
	@Test
	public void testNoRipple() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						true, true, true, null), null);

		warmAndTest(painter, "no ripple", 3);
	}

	/**
	 * Tests with blur disabled.
	 */
	@Test
	public void testNoBlur() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(true,
						false, true, true, null), null);

		warmAndTest(painter, "no blur", 4.5);
	}

	/**
	 * Tests with outline disabled.
	 */
	@Test
	public void testNoOutline() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(true,
						true, false, true, null), null);

		warmAndTest(painter, "no outline", 4);
	}

	/**
	 * Tests with rotate disabled.
	 */
	@Test
	public void testNoRotate() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(true,
						true, true, false, null), null);

		warmAndTest(painter, "no rotate", 4.5);
	}

	/**
	 * Tests with ripple enabled.
	 */
	@Test
	public void testYesRipple() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(true,
						false, false, false, null), null);

		warmAndTest(painter, "yes ripple", 3.5);
	}

	/**
	 * Tests with blur enabled.
	 */
	@Test
	public void testYesBlur() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						true, false, false, null), null);

		warmAndTest(painter, "yes blur", 1.5);
	}

	/**
	 * Tests with outline enabled.
	 */
	@Test
	public void testYesOutline() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						false, true, false, null), null);

		warmAndTest(painter, "yes outline", 2);
	}

	/**
	 * Tests with rotate enabled.
	 */
	@Test
	public void testYesRotate() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						false, false, true, null), null);

		warmAndTest(painter, "yes rotate", 2);
	}

	/**
	 * Tests with ripple, blur, outline and rotate disabled.
	 */
	@Test
	public void testNoRippleNoBlurNoOutlineNoRotate() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						false, false, false, null), null);

		warmAndTest(painter, "no ripple, no blur, no outline, no rotate", 1.25);
	}

	/**
	 * Tests with ripple, blur, outline and rotate enabled.
	 */
	@Test
	public void testYesRippleYesBlurYesOutlineYesRotate() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(true,
						true, true, true, null), null);

		warmAndTest(painter, "yes ripple, yes blur, yes outline, yes rotate", 5);
	}

	/**
	 * Tests with rotate and blur disabled.
	 */
	@Test
	public void testNoRippleNoBlur() {
		final Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, new EffectConfig(false,
						false, true, true, null), null);

		warmAndTest(painter, "no ripple, no blur", 2.5);
	}

	/**
	 * Tests the passed in {@link Painter} object. Does some warm up calls
	 * before starting measuring.
	 * 
	 * @param painter
	 *            to be tested, not null
	 * @param name
	 *            test name used for logging, not null
	 * @param limitMs
	 *            the maximum allowed time in ms for an image generation
	 */
	protected void warmAndTest(Painter painter, String name, double limitMs) {
		innerTest(painter, warmUpNum);

		final long start = System.nanoTime();

		innerTest(painter, sampleNum);

		final long end = System.nanoTime();

		final long runTime = end - start;

		final double avgMs = runTime / (double) sampleNum / 1000000;

		System.out.println("name = \"" + name + "\", avg. time (ms) = " + avgMs
				+ ", limit time (ms) = " + limitMs);

		Assert.assertTrue("Speed is too slow name = \"" + name
				+ "\", avg. time (ms) = " + avgMs + ", limit time (ms) = "
				+ limitMs, limitMs > avgMs);
	}

	/**
	 * Helper method to call {@link Painter#draw(Font, Color, String)} several
	 * times.
	 * 
	 * @param painter
	 *            to be tested, not null
	 * @param num
	 *            number of calls to be made
	 */
	protected void innerTest(Painter painter, int num) {
		for (int fi = 0; fi < num; fi++)
			painter.draw(font, Color.BLUE, "teszgynyij");
	}
}
