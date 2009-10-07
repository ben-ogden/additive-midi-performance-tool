package ui.keyboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ui.keyboard.WhiteKey.KeyType;

/**
 * This is a JPanel that contains the keyboard. The keyboard has it's own
 * listener to generate MIDI messages, which it sends to a receiver. By
 * implementing Transmitter, this keyboard can be used in conjunction with any
 * MIDI device containing a receiver.
 * 
 * @author Christopher S. Redding
 * 
 * TODO: Key Bindings
 */
public class KeyboardPanel extends JPanel implements Transmitter, MouseListener {

	private static final long serialVersionUID = 1L;

	private JLayeredPane keyboard;
	private WhiteKey cKey, dKey, eKey, fKey, gKey, aKey, bKey;
	private BlackKey cSharpKey, dSharpKey, fSharpKey, gSharpKey, aSharpKey;

	private Receiver receiver = null;

	private int channel;

	/**
	 * Constructs the keyboard, adding each key. Sets the channel to send the
	 * MIDI messages on.
	 * 
	 * @param channel
	 *            The channel to send the MIDI messages on
	 */
	public KeyboardPanel(int channel) {
		super();

		this.channel = channel;

		// Use a JLayeredPane to enable overlapping components
		keyboard = new JLayeredPane();
		// Set the layout to null so I can manually place the keys where they
		// belong.
		keyboard.setLayout(null);

		// instantiate each key
		cKey = new WhiteKey(KeyType.Left, 60);
		cSharpKey = new BlackKey(61);
		dKey = new WhiteKey(KeyType.Center, 62);
		dSharpKey = new BlackKey(63);
		eKey = new WhiteKey(KeyType.Right, 64);
		fKey = new WhiteKey(KeyType.Left, 65);
		fSharpKey = new BlackKey(66);
		gKey = new WhiteKey(KeyType.Center, 67);
		gSharpKey = new BlackKey(68);
		aKey = new WhiteKey(KeyType.Center, 69);
		aSharpKey = new BlackKey(70);
		bKey = new WhiteKey(KeyType.Right, 71);

		// add the mouse listener to produce the notes
		cKey.addMouseListener(this);
		cSharpKey.addMouseListener(this);
		dKey.addMouseListener(this);
		dSharpKey.addMouseListener(this);
		eKey.addMouseListener(this);
		fKey.addMouseListener(this);
		gKey.addMouseListener(this);
		aKey.addMouseListener(this);
		bKey.addMouseListener(this);
		fSharpKey.addMouseListener(this);
		gSharpKey.addMouseListener(this);
		aSharpKey.addMouseListener(this);
		
		// set the location of each key
		int xPos = 0;
		cKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		cSharpKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
				BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		dKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		dSharpKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
				BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		eKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		fKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		fSharpKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
				BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		gKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		gSharpKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
				BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		aKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		aSharpKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
				BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;
		bKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
		xPos += WhiteKey.KEY_WIDTH;

		// add they keys to the layered panel, specifing the depth so the key is
		// on the correct level.
		keyboard.add(cKey, 1);
		keyboard.add(cSharpKey, 0);
		keyboard.add(dKey, 1);
		keyboard.add(dSharpKey, 0);
		keyboard.add(eKey, 1);
		keyboard.add(fKey, 1);
		keyboard.add(fSharpKey, 0);
		keyboard.add(gKey, 1);
		keyboard.add(gSharpKey, 0);
		keyboard.add(aKey, 1);
		keyboard.add(aSharpKey, 0);
		keyboard.add(bKey, 1);

		// set the preferred size of the keyboard
		keyboard.setPreferredSize(new Dimension(WhiteKey.KEY_WIDTH * 7,
				WhiteKey.KEY_HEIGHT));

		// add the keyboard to this panel
		this.add(keyboard, BorderLayout.CENTER);

	}

	/**
	 * Required method for implementing a transiever.
	 * 
	 * Does nothing. This may be implemented in the future
	 */
	@Override
	public void close() {
		// TODO Implement this if desired
	}

	/**
	 * Required method for implementing a transiever.
	 * 
	 * Returns the receiver currently set for the keyboard.
	 */
	@Override
	public Receiver getReceiver() {
		return this.receiver;
	}

	/**
	 * Required method for implementing a transiever.
	 * 
	 * Sets the receiver to send the MIDI messages to.
	 */
	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * Required method for implementing a MouseListener. Does nothing.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/**
	 * Required method for implementing a MouseListener. Does nothing.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * Required method for implementing a MouseListener. Does nothing.
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Required method for implementing a MouseListener.
	 * 
	 * Produces the MIDI NOTE_ON message for the key pressed, and sends it to
	 * the receiver
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getSource() instanceof KeyboardKey) {
			KeyboardKey key = (KeyboardKey) event.getSource();
			setNoteOn(key);
		}

	}
	
	public void setNoteOn(KeyboardKey key){
		if (receiver != null) {
			ShortMessage msgNote = new ShortMessage();
			try {
				msgNote.setMessage(ShortMessage.NOTE_ON, channel, key
						.getNote(), 93);
				receiver.send(msgNote, -1);
			} catch (InvalidMidiDataException e) {
			}
		}
	}

	/**
	 * Required method for implementing a MouseListener.
	 * 
	 * Produces the MIDI NOTE_OFF message for the key pressed, and sends it to
	 * the receiver
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getSource() instanceof KeyboardKey) {
			KeyboardKey key = (KeyboardKey) event.getSource();
			setNoteOff(key);
		}
	}
	
	public void setNoteOff(KeyboardKey key){
		if (receiver != null) {
			ShortMessage msgNote = new ShortMessage();
			try {
				msgNote.setMessage(ShortMessage.NOTE_OFF, channel, key
						.getNote(), 93);
				receiver.send(msgNote, -1);
			} catch (InvalidMidiDataException e) {
			}
		}
	}

}
