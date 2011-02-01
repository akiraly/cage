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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class CageTest {

	@Test
	public void testDefault() throws IOException {
		testSize(new Cage());
	}

	@Test
	public void testMax() throws IOException {
		Random rnd = new Random();
		testSize(new Cage(
				new Painter(Painter.DEFAULT_WIDTH, Painter.DEFAULT_HEIGHT,
						null, null, true, true, true, true, rnd), null, null,
				null, Cage.DEFAULT_COMPRESS_RATIO, null, rnd));
	}

	@Test
	public void testG() throws IOException {
		testSize(Cage.likeG());
	}

	@Test
	public void testY() throws IOException {
		testSize(Cage.likeY());
	}

	protected void testSize(Cage cage) throws IOException {
		for (int fi = 0; fi < 100; fi++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			try {
				cage.draw("sometext", baos);
				Assert.assertTrue(
						"The image size is too small. Probably empty.",
						baos.size() > 1024);
			} finally {
				baos.close();
			}
		}
	}
}
