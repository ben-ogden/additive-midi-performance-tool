package ampt.examples.timing;

/**
 * A version of Ben's timer test with sound. This really demonstrates the problem
 * of trying to do timing related functions in a single thread.
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

public class TimerWithSound {
    TimerTask task;
    
    public TimerWithSound(long period, long start, TimerTask task) {
        this.task = task;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, start, period);
    }
    
    public static void main(String args[]) throws MidiUnavailableException, InvalidMidiDataException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage onMsg = new ShortMessage();
        ShortMessage offMsg = new ShortMessage();
        onMsg.setMessage(ShortMessage.NOTE_ON, 8, 60, 93);
        offMsg.setMessage(ShortMessage.NOTE_OFF, 8, 60, 93);
        
        new TimerWithSound(100L, 20L, getAnonTask(rcvr, onMsg));
        new TimerWithSound(100L, 25L, getAnonTask(rcvr, offMsg));
        
        while(true) {
        }
        
    }
    
    public static TimerTask getAnonTask(final Receiver r, final ShortMessage s) {
        TimerTask task = new TimerTask() {
            public void run() {
                r.send(s, -1);
            }
        };
        
        return task;
    }
}