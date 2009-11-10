package ampt.examples;

import ampt.ui.keyboard.KeyboardDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import ampt.ui.keyboard.KeyboardPanel;
 
public class KeyboardWithFlutter extends JFrame {
 
    public KeyboardWithFlutter() {
        this.setTitle("Keyboard with Filters Test");
        KeyboardDevice keyboardDevice = new KeyboardDevice();
        KeyboardPanel keyboard = new KeyboardPanel(keyboardDevice);
        FlutterFilter filter = new FlutterFilter();
        this.add(keyboard);
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            keyboardDevice.open();
            filter.setReceiver(synth.getReceiver());
            keyboardDevice.getTransmitter().setReceiver(filter);
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