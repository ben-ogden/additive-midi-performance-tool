package ampt.examples;

import ampt.core.devices.KeyboardDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;

import ampt.ui.keyboard.KeyboardPanel;

/**
 * Class that tests the ui.keyboard package. Uses the default synthesizer to
 * produce the notes.
 * 
 * @author Christopher S. Redding
 * 
 */
public class KeyboardTest extends JFrame {

    public KeyboardTest() throws MidiUnavailableException {
        this.setTitle("Keyboard Test");
        KeyboardDevice keyboardDevice = new KeyboardDevice();
        KeyboardPanel keyboard = new KeyboardPanel(keyboardDevice);
        this.add(keyboard);
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            keyboardDevice.open();

            keyboardDevice.getTransmitter().setReceiver(synth.getReceiver());
//			keyboard.setReceiver(synth.getReceiver());
        } catch (MidiUnavailableException ex) {
            ex.printStackTrace();
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) throws MidiUnavailableException {
        new KeyboardTest();
    }
}
