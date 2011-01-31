package com.github.cage;

import java.awt.Color;

/**
 * {@link IGenerator} implementation that returns always the same {@link Color}.
 * This class is thread safe.
 * 
 * @author akiraly
 * 
 */
public class ConstantColorGenerator implements IGenerator<Color> {
	private final Color color;

	/**
	 * Constructor
	 * 
	 * @param color
	 *            not null
	 */
	public ConstantColorGenerator(Color color) {
		this.color = color;
	}

	public Color next() {
		return color;
	}

}
