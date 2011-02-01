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
import java.awt.Font;

import org.junit.Assert;
import org.junit.Test;

import com.github.cage.Painter.Quality;

public class PainterSpeedTest {
	private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN,
			Painter.DEFAULT_HEIGHT / 2);

	private final int warmUpNum = 1000;

	private final int sampleNum = 5000;

	@Test
	public void testDefaultQuality() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, Quality.DEFAULT, true, true,
				false, true, null);

		warmAndTest(painter, "default quality", 7);
	}

	@Test
	public void testMinQuality() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, Quality.MIN, true, true, false,
				true, null);

		warmAndTest(painter, "min quality", 7);
	}

	@Test
	public void testDefault() {
		Painter painter = new Painter();

		warmAndTest(painter, "default", 5);
	}

	@Test
	public void testNoRipple() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, false, true, false, true,
				null);

		warmAndTest(painter, "no ripple", 3);
	}

	@Test
	public void testNoBlur() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, true, false, false, true,
				null);

		warmAndTest(painter, "no blur", 4.5);
	}

	@Test
	public void testNoRotate() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, true, true, false, true,
				null);

		warmAndTest(painter, "no rotate", 5);
	}

	@Test
	public void testNoRippleNoBlur() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, false, false, false, true,
				null);

		warmAndTest(painter, "no ripple, no blur", 2.5);
	}

	@Test
	public void testNoRippleNoBlurYesOutlineNoRotate() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, false, false, true, false,
				null);

		warmAndTest(painter, "no ripple, no blur, yes outline, no rotate", 2);
	}

	@Test
	public void testNoRippleNoBlurNoOutlineNoRotate() {
		Painter painter = new Painter(Painter.DEFAULT_WIDTH,
				Painter.DEFAULT_HEIGHT, null, null, false, false, false, false,
				null);

		warmAndTest(painter, "no ripple, no blur, no outline, no rotate", 1.25);
	}

	protected long warmAndTest(Painter painter, String name, double limitMs) {
		innerTest(painter, warmUpNum);

		long start = System.nanoTime();

		innerTest(painter, sampleNum);

		long end = System.nanoTime();

		long runTime = end - start;

		double avgMs = runTime / (double) sampleNum / 1000000;

		System.out
				.println("name = \"" + name + "\", avg. time (ms) = " + avgMs);

		Assert.assertTrue("Speed is too slow, limit (ms) = " + limitMs
				+ ", avg. runTime (ms) = " + avgMs, limitMs > avgMs);

		return runTime;
	}

	protected void innerTest(Painter painter, int num) {

		for (int fi = 0; fi < num; fi++)
			painter.draw(font, Color.BLUE, "teszgynyij");
	}
}
