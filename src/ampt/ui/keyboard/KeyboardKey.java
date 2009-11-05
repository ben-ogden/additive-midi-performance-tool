package ampt.ui.keyboard;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.ShortMessage;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.KeyStroke;

/**
 * Abstract class which represents a Keyboard Key. This class is not meant to be
 * explicitly initialized, and should only be accessed through it's subclasses.
 * This represents the note that the keyboard key represents, and contains a
 * mouse listener so the color of the button can be changed.
 * 
 * This class subclasses JButton to have the correct behavior.
 * 
 * ********Revisions******** 
 * 08/13/09 [Chris] Added displaying of key binding on each key
 * @author Christopher S. Redding 
 */
public abstract class KeyboardKey extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	private final int note;
	protected char keyBinding = ' ';
        private KeyboardDevice keyboardDevice;

	protected boolean pressed = false;

	/**
	 * Constructor to set the note number that this key represents. Also sets up
	 * the mouse listener for changing the color of the key
	 * 
	 * @param note
	 *            The note that this key represents
	 * @param channel
	 *            The channel to send MIDI messages on.
	 */
	public KeyboardKey(final int note, KeyboardDevice keyboardDevice) {
		super();
		this.note = note;
                this.keyboardDevice = keyboardDevice;
		this.addMouseListener(this);
	}

	/**
	 * Constructor to set the note number that this key represents. Also sets up
	 * the mouse listener for changing the color of the key
	 * 
	 * @param note
	 *            The note that this key represents
	 * @param keyBinding
	 *            The key to bind this button to
	 * @param channel
	 *            The channel to send MIDI messages on
	 */
	public KeyboardKey(final int note, char keyBinding, KeyboardDevice keyboardDevice) {
		this(note, keyboardDevice);

		this.keyBinding = keyBinding;

		InputMap inputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
		char upperKeyBinding = Character.toUpperCase(keyBinding);
		inputMap.put(KeyStroke.getKeyStroke("pressed " + upperKeyBinding),
				"pressed");
		inputMap.put(KeyStroke.getKeyStroke("released " + upperKeyBinding),
				"released");
		ActionMap actionMap = this.getActionMap();
		actionMap.put("pressed", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!pressed) {
					pressed = true;
					repaint();
					setNoteOn();
				}
			}
		});

		actionMap.put("released", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pressed = false;
				repaint();
				setNoteOff();
			}
		});

	}

	/**
	 * returns the note this key represents
	 * @return
	 */
	public int getNote() {
		return note;
	}

	/**
	 * Mouse Listeners that are not used
	 */
	@Override
	public void mouseClicked(final MouseEvent event) {
	}
	@Override
	public void mouseEntered(final MouseEvent event) {
	}
	@Override
	public void mouseExited(final MouseEvent event) {
	}

	/**
	 * Listener for when the button is pressed to change the color of the button
	 */
	@Override
	public void mousePressed(final MouseEvent event) {
		pressed = true;
		this.repaint();
		this.setNoteOn();
	}

	/**
	 * Listener for when the button is pressed to change the color of the button
	 */
	@Override
	public void mouseReleased(final MouseEvent event) {
		pressed = false;
		this.repaint();
		this.setNoteOff();

	}

	/**
	 * Sends a Note On message to the set receiver on the set channel.
	 */
	public void setNoteOn() {
		keyboardDevice.sendMessage(ShortMessage.NOTE_ON, note);
	}

	/**
	 * Sends a Note Off message to the set receiver on the set channel.
	 */
	public void setNoteOff() {
		keyboardDevice.sendMessage(ShortMessage.NOTE_OFF, note);
	}

}
