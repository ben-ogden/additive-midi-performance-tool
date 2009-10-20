package ampt.examples;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import ampt.ui.keyboard.KeyboardPanel;
 
public class KeyboardWithFlutter extends JFrame {
 
    public KeyboardWithFlutter() {
        this.setTitle("Keyboard with Filters Test");
        KeyboardPanel keyboard = new KeyboardPanel(0);
        FlutterFilter filter = new FlutterFilter();
        this.add(keyboard);
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            filter.setReceiver(synth.getReceiver());
            keyboard.setReceiver(filter);
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
    }
    
    public static void main(String[] args) {
		new KeyboardWithFlutter();
	}
 }