package ampt.examples.timing;

/**
 * Just a simple test to see if notes of the same length played in different
 * threads diverge
 *
 * @author Rob Szewczyk
 */

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class ThreadedTimingTest {
    
    protected static Receiver rcvr;
    
    public static void main(String args[]) throws MidiUnavailableException, InvalidMidiDataException {
        
        Thread t1, t2;
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        rcvr = synth.getReceiver();
        ShortMessage msg1 = new ShortMessage();
        ShortMessage msg2 = new ShortMessage();
        
        msg1.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        msg2.setMessage(ShortMessage.NOTE_ON, 0, 65, 93);
                
        t1 = makeThread(msg1, 2000);
        t2 = makeThread(msg2, 2000);
        
        t1.start();
        t2.start();
        
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            return;
        }
        
        t1.interrupt();
        t2.interrupt();
        
        msg1.setMessage(ShortMessage.NOTE_OFF, 0, 65, 93);
        msg2.setMessage(ShortMessage.NOTE_OFF, 1, 60, 93);
        
        rcvr.send(msg1, -1);
        rcvr.send(msg2, -1);
    }
    
    protected static Thread makeThread(final ShortMessage m, final long length) {
        Runnable runLoop = new Runnable() {
            public void run() {
                try {
                    while(true) {
                        send(m);
                        Thread.sleep(length);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        
        return new Thread(runLoop);
    }
    
    protected synchronized static void send(ShortMessage m) {
        rcvr.send(m, -1);
    }
}