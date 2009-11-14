package examples.timing;

/**
 * One of three versions of a timing test based on the idea behind Ben's Timer
 * test. This uses a Timer() object. As it progresses it loses accuracy.
 *
 * @author Rob Szewczyk
 */

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.util.Timer;
import java.util.TimerTask;

public class TimingWithTimer {
    
    public static void main(String args[]) throws MidiUnavailableException, InvalidMidiDataException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage onMsg = new ShortMessage();
        ShortMessage offMsg = new ShortMessage();
        onMsg.setMessage(ShortMessage.NOTE_ON, 3, 60, 93);
        offMsg.setMessage(ShortMessage.NOTE_OFF, 3, 60, 93);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(getCommand(rcvr, onMsg), 1000L, 80L);
        timer.scheduleAtFixedRate(getCommand(rcvr, offMsg), 1020L, 80L);
        
        while(true) {
        }
        
    }
    
    protected static TimerTask getCommand(final Receiver r, final ShortMessage s) {
        return (new TimerTask() {
            public void run() {
                r.send(s, -1);
            }
        });
    }
}