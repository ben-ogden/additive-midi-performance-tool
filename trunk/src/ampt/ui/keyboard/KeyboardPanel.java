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
     * @param keyboardDevice
     *            The device backing this keyboard panel
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
        WhiteKey cKey = new WhiteKey(KeyType.Left, 0, 'q', keyboardReceiver);
        keys.add(cKey);
        BlackKey cSharpKey = new BlackKey(1, '2', keyboardReceiver);
        keys.add(cSharpKey);
        WhiteKey dKey = new WhiteKey(KeyType.Center, 2, 'w', keyboardReceiver);
        keys.add(dKey);
        BlackKey dSharpKey = new BlackKey(3, '3', keyboardReceiver);
        keys.add(dSharpKey);
        WhiteKey eKey = new WhiteKey(KeyType.Right, 4, 'e', keyboardReceiver);
        keys.add(eKey);
        WhiteKey fKey = new WhiteKey(KeyType.Left, 5, 'r', keyboardReceiver);
        keys.add(fKey);
        BlackKey fSharpKey = new BlackKey(6, '5', keyboardReceiver);
        keys.add(fSharpKey);
        WhiteKey gKey = new WhiteKey(KeyType.Center, 7, 't', keyboardReceiver);
        keys.add(gKey);
        BlackKey gSharpKey = new BlackKey(8, '6', keyboardReceiver);
        keys.add(gSharpKey);
        WhiteKey aKey = new WhiteKey(KeyType.Center, 9, 'y', keyboardReceiver);
        keys.add(aKey);
        BlackKey aSharpKey = new BlackKey(10, '7', keyboardReceiver);
        keys.add(aSharpKey);
        WhiteKey bKey = new WhiteKey(KeyType.Right, 11, 'u', keyboardReceiver);
        keys.add(bKey);
        WhiteKey cUpperKey = new WhiteKey(KeyType.Left, 12, 'v', keyboardReceiver);
        keys.add(cUpperKey);
        BlackKey cSharpUpperKey = new BlackKey(13, 'g', keyboardReceiver);
        keys.add(cSharpUpperKey);
        WhiteKey dUpperKey = new WhiteKey(KeyType.Center, 14, 'b', keyboardReceiver);
        keys.add(dUpperKey);
        BlackKey dSharpUpperKey = new BlackKey(15, 'h', keyboardReceiver);
        keys.add(dSharpUpperKey);
        WhiteKey eUpperKey = new WhiteKey(KeyType.Right, 16, 'n', keyboardReceiver);
        keys.add(eUpperKey);
        WhiteKey fUpperKey = new WhiteKey(KeyType.Left, 17, 'm', keyboardReceiver);
        keys.add(fUpperKey);
        BlackKey fSharpUpperKey = new BlackKey(18, 'k', keyboardReceiver);
        keys.add(fSharpUpperKey);
        WhiteKey gUpperKey = new WhiteKey(KeyType.Center, 19, ',', keyboardReceiver);
        keys.add(gUpperKey);
        BlackKey gSharpUpperKey = new BlackKey(20, 'l', keyboardReceiver);
        keys.add(gSharpUpperKey);
        WhiteKey aUpperKey = new WhiteKey(KeyType.Center, 21, '.', keyboardReceiver);
        keys.add(aUpperKey);
        BlackKey aSharpUpperKey = new BlackKey(22, ';', keyboardReceiver);
        keys.add(aSharpUpperKey);
        WhiteKey bUpperKey = new WhiteKey(KeyType.Right, 23, '/', keyboardReceiver);
        keys.add(bUpperKey);

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
        cUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        cSharpUpperKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
                BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        dUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        dSharpUpperKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
                BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        eUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        fUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        fSharpUpperKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
                BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        gUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        gSharpUpperKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
                BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        aUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
        aSharpUpperKey.setBounds(xPos + 3 * WhiteKey.KEY_WIDTH / 4, 0,
                BlackKey.KEY_WIDTH, BlackKey.KEY_HEIGHT);
        xPos += WhiteKey.KEY_WIDTH;
        bUpperKey.setBounds(xPos, 0, WhiteKey.KEY_WIDTH, WhiteKey.KEY_HEIGHT);
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
        keyboard.add(cUpperKey, 1);
        keyboard.add(cSharpUpperKey, 0);
        keyboard.add(dUpperKey, 1);
        keyboard.add(dSharpUpperKey, 0);
        keyboard.add(eUpperKey, 1);
        keyboard.add(fUpperKey, 1);
        keyboard.add(fSharpUpperKey, 0);
        keyboard.add(gUpperKey, 1);
        keyboard.add(gSharpUpperKey, 0);
        keyboard.add(aUpperKey, 1);
        keyboard.add(aSharpUpperKey, 0);
        keyboard.add(bUpperKey, 1);


        // set the preferred size of the keyboard
        keyboard.setPreferredSize(new Dimension(WhiteKey.KEY_WIDTH * 14,
                WhiteKey.KEY_HEIGHT));

        // add the keyboard to this panel
        this.add(keyboard, BorderLayout.CENTER);

    }
}
