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
package com.github.cage.examples.cage_e01_simple;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.github.cage.YCage;

/**
 * An example console application that generates a few hundreds captcha images
 * and writes them to disk.
 * 
 * @author akiraly
 * 
 */
public class App {
	public static void main(String[] args) throws IOException {
		generate(new GCage(), 10, "cg1", ".jpg", "colding");
		generate(new YCage(), 10, "cy1", ".jpg", "eT6wLAH");
		generate(new GCage(), 100, "cg2", ".jpg", null);
		generate(new YCage(), 100, "cy2", ".jpg", null);
	}

	protected static void generate(Cage cage, int num, String namePrefix,
			String namePostfix, String text) throws IOException {
		for (int fi = 0; fi < num; fi++) {
			final OutputStream os = new FileOutputStream(namePrefix + fi
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
