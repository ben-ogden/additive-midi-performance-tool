package ampt.examples;

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

	public KeyboardTest() {
		this.setTitle("Keyboard Test");
		
		KeyboardPanel keyboard = new KeyboardPanel(0);
		this.add(keyboard);
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			keyboard.setReceiver(synth.getReceiver());
		} catch (MidiUnavailableException ex) {
			ex.printStackTrace();
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new KeyboardTest();
	}
}
