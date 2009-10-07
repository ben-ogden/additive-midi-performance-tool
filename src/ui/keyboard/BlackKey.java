package ui.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This is a black key on a keyboard, and can be added to a JPanel just like any
 * other button.
 * 
 * @author Christopher S. Redding
 * 
 */
public class BlackKey extends KeyboardKey {

	private static final long serialVersionUID = 1L;

	public static final int KEY_WIDTH = 15;
	public static final int KEY_HEIGHT = 75;

	/**
	 * Constructor which sets the note of the key, and the preferred size of the
	 * key.
	 * 
	 * @param note
	 *            The note that this key represents
	 */
	public BlackKey(final int note) {
		super(note);

		this.setPreferredSize(new Dimension(KEY_WIDTH, KEY_HEIGHT));
	}

	/**
	 * Paints the border of the black key
	 */
	@Override
	protected void paintBorder(final Graphics g) {
		g.drawLine(0, 0, KEY_WIDTH - 1, 0);
		g.drawLine(KEY_WIDTH - 1, 0, KEY_WIDTH - 1, KEY_HEIGHT - 1);
		g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT - 1, 0, KEY_HEIGHT - 1);
		g.drawLine(0, KEY_HEIGHT - 1, 0, 0);
	}

	/**
	 * Paints the black key, changing the color if the key is pressed.
	 */
	@Override
	public void paintComponent(final Graphics g) {

		final Color color = g.getColor();
		if (pressed) {
			g.setColor(Color.GRAY);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillRect(0, 0, KEY_WIDTH, KEY_HEIGHT);
		g.setColor(color);
	}

}