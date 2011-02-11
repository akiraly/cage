package com.github.cage.examples.cage_e01_simple;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.github.cage.Cage;
import com.github.cage.GCage;

public class QuickStart {
	public static void main(String[] args) throws IOException {
		Cage cage = new GCage();

		OutputStream os = new FileOutputStream("captcha.jpg", false);
		try {
			cage.draw(cage.getTokenGenerator().next(), os);
		} finally {
			os.close();
		}
	}
}
