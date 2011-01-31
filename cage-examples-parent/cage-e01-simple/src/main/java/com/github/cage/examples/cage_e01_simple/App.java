package com.github.cage.examples.cage_e01_simple;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.github.cage.Cage;

/**
 * An example console application that generates a few hundreds captcha images
 * and writes them to disk.
 * 
 * @author akiraly
 * 
 */
public class App {

	public static void main(String[] args) throws IOException {
		generate(Cage.likeG(), 100, "cg1", ".jpg", "colding");
		generate(Cage.likeY(), 100, "cy1", ".jpg", "eT6wLAH");
		generate(Cage.likeG(), 100, "cg2", ".jpg", null);
		generate(Cage.likeY(), 100, "cy2", ".jpg", null);
	}

	protected static void generate(Cage cage, int num, String namePrefix,
			String namePostfix, String text) throws IOException {

		for (int fi = 0; fi < num; fi++) {
			OutputStream os = new FileOutputStream(namePrefix + fi
					+ namePostfix, false);
			try {
				cage.draw(
						text != null ? text : cage.getTokenGenerator().next(),
						os);
			} finally {
				os.close();
			}
		}
	}
}
