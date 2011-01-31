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
