package ampt.examples.timing;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * One of three versions of a timing test based on the idea behind Ben's Timer
 * test. This uses Thread objects. As it progresses it loses accuracy, although
 * possibly not as quickly as the example using Timer(). (very
 * subjective - my ears could be playing tricks after listening too much)
 *
 * @author Rob Szewczyk
 */

public class TimingWithThreads {
    public static void main(String args[]) throws MidiUnavailableException, InvalidMidiDataException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage on = new ShortMessage();
        ShortMessage off = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 3, 60, 93);
        off.setMessage(ShortMessage.NOTE_OFF, 3, 60, 93);
        Thread t1 = new Thread(getCommand(on, rcvr));
        Thread t2 = new Thread(getCommand(off, rcvr));
        
        t1.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
        t2.start();
        
        
    }
    
    protected static Runnable getCommand(final ShortMessage m, final Receiver r) {
        return (new Runnable() {
            public void run() {
                try {
                    while (true) {
                        r.send(m, -1);
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
    }
}