package ampt.ui.keyboard;

import ampt.core.devices.KeyboardDevice;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ampt.ui.keyboard.WhiteKey.KeyType;
import javax.sound.midi.Receiver;

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
public class KeyboardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLayeredPane keyboard;
    private Vector<KeyboardKey> keys;

    /**
     * Constructs the keyboard, adding each key. Sets the channel to send the
     * MIDI messages on.
     *
     * @param channel
     *            The channel to send the MIDI messages on
     */
    public KeyboardPanel(KeyboardDevice keyboardDevice) throws MidiUnavailableException {

        super();

        Receiver keyboardReceiver = keyboardDevice.getReceiver();

        // Use a JLayeredPane to enable overlapping components
        keyboard = new JLayeredPane();
        // Set the layout to null so I can manually place the keys where they
        // belong.
        keyboard.setLayout(null);

        // Setup Vector to hold keys
        keys = new Vector<KeyboardKey>();

        // instantiate each key
        WhiteKey cKey = new WhiteKey(KeyType.Left, 0, 'a', keyboardReceiver);
        keys.add(cKey);
        BlackKey cSharpKey = new BlackKey(1, 'w', keyboardReceiver);
        keys.add(cSharpKey);
        WhiteKey dKey = new WhiteKey(KeyType.Center, 2, 's', keyboardReceiver);
        keys.add(dKey);
        BlackKey dSharpKey = new BlackKey(3, 'e', keyboardReceiver);
        keys.add(dSharpKey);
        WhiteKey eKey = new WhiteKey(KeyType.Right, 4, 'd', keyboardReceiver);
        keys.add(eKey);
        WhiteKey fKey = new WhiteKey(KeyType.Left, 5, 'f', keyboardReceiver);
        keys.add(fKey);
        BlackKey fSharpKey = new BlackKey(6, 't', keyboardReceiver);
        keys.add(fSharpKey);
        WhiteKey gKey = new WhiteKey(KeyType.Center, 7, 'g', keyboardReceiver);
        keys.add(gKey);
        BlackKey gSharpKey = new BlackKey(8, 'y', keyboardReceiver);
        keys.add(gSharpKey);
        WhiteKey aKey = new WhiteKey(KeyType.Center, 9, 'h', keyboardReceiver);
        keys.add(aKey);
        BlackKey aSharpKey = new BlackKey(10, 'u', keyboardReceiver);
        keys.add(aSharpKey);
        WhiteKey bKey = new WhiteKey(KeyType.Right, 11, 'j', keyboardReceiver);
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
}
