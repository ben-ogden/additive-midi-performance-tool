package ampt.ui.keyboard;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * Abstract class which represents a Keyboard Key. This class is not meant to be
 * explicitly initialized, and should only be accessed through it's subclasses.
 * This represents the note that the keyboard key represents, and contains a
 * mouse listener so the color of the button can be changed.
 * 
 * This class subclasses JButton to have the correct behavior.
 * 
 * @author Christopher S. Redding
 */
public abstract class KeyboardKey extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	private final int note;

	protected boolean pressed = false;

	/**
	 * Constructor to set the note number that this key represents. Also sets up
	 * the mouse listener for changing the color of the key
	 * 
	 * @param note
	 *            The note that this key represents
	 */
	public KeyboardKey(final int note) {
		super();
		this.note = note;
		this.addMouseListener(this);
	}

	public int getNote() {
		return note;
	}

	/**
	 * Mouse Listeners that are not used
	 */
	@Override
	public void mouseClicked(final MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(final MouseEvent arg0) {
	}

	@Override
	public void mouseExited(final MouseEvent arg0) {
	}

	@Override
	/**
	 * Listener for when the button is pressed to change the color of the button
	 */
	public void mousePressed(final MouseEvent arg0) {
		pressed = true;
		this.repaint();
	}

	/**
	 * Listener for when the button is pressed to change the color of the button
	 */
	@Override
	public void mouseReleased(final MouseEvent arg0) {
		pressed = false;
		this.repaint();

	}

}
