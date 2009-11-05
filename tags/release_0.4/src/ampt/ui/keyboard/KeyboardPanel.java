package ampt.ui.keyboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ampt.ui.keyboard.WhiteKey.KeyType;

/**
 * This is a JPanel that contains the keyboard. The keyboard has it's own
 * listener to generate MIDI messages, which it sends to a receiver. By
 * implementing Transmitter, this keyboard can be used in conjunction with any
 * MIDI device containing a receiver.
 * 
 * ********Revisions********
 * 10-13-09 [Chris] Removed the mouse listener from this class and added it 
 * into the KeyboardKey class.
 * 
 * @author Christopher S. Redding
 */
public class KeyboardPanel extends JPanel implements Transmitter {

	private static final long serialVersionUID = 1L;

	private JLayeredPane keyboard;
	private Vector<KeyboardKey> keys;

	private Receiver receiver = null;

	/**
	 * Constructs the keyboard, adding each key. Sets the channel to send the
	 * MIDI messages on.
	 * 
	 * @param channel
	 *            The channel to send the MIDI messages on
	 */
	public KeyboardPanel(int channel) {
		super();

		// Use a JLayeredPane to enable overlapping components
		keyboard = new JLayeredPane();
		// Set the layout to null so I can manually place the keys where they
		// belong.
		keyboard.setLayout(null);
		
		// Setup Vector to hold keys
		keys = new Vector<KeyboardKey>();

		// instantiate each key
		WhiteKey cKey = new WhiteKey(KeyType.Left, 60, 'a', channel);
		keys.add(cKey);
		BlackKey cSharpKey = new BlackKey(61, 'w', channel);
		keys.add(cSharpKey);
		WhiteKey dKey = new WhiteKey(KeyType.Center, 62, 's', channel);
		keys.add(dKey);
		BlackKey dSharpKey = new BlackKey(63, 'e', channel);
		keys.add(dSharpKey);
		WhiteKey eKey = new WhiteKey(KeyType.Right, 64, 'd', channel);
		keys.add(eKey);
		WhiteKey fKey = new WhiteKey(KeyType.Left, 65, 'f', channel);
		keys.add(fKey);
		BlackKey fSharpKey = new BlackKey(66, 't', channel);
		keys.add(fSharpKey);
		WhiteKey gKey = new WhiteKey(KeyType.Center, 67, 'g', channel);
		keys.add(gKey);
		BlackKey gSharpKey = new BlackKey(68, 'y', channel);
		keys.add(gSharpKey);
		WhiteKey aKey = new WhiteKey(KeyType.Center, 69, 'h', channel);
		keys.add(aKey);
		BlackKey aSharpKey = new BlackKey(70, 'u', channel);
		keys.add(aSharpKey);
		WhiteKey bKey = new WhiteKey(KeyType.Right, 71, 'j', channel);
		keys.add(bKey);
		
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
		for(KeyboardKey key: keys){
			key.setReceiver(receiver);
		}
	}
	
	public void setChannel(int channel){
		for(KeyboardKey key: keys){
			key.setChannel(channel);
		}
	}

}
