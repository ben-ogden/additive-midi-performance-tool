package ampt.examples.timing;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * One of three versions of a timing test based on the idea behind Ben's Timer
 * test. This an Executor. As it progresses it loses accuracy, although
 * possibly not as quickly as the example using Timer(). (very
 * subjective - my ears could be playing tricks after listening too much)
 *
 * @author Rob Szewczyk
 */

public class TimingWithExecutor {
    public static void main(String args[]) throws MidiUnavailableException, InvalidMidiDataException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage on = new ShortMessage();
        ShortMessage off = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        off.setMessage(ShortMessage.NOTE_OFF, 0, 60, 93);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50);
        executor.scheduleAtFixedRate(getCommand(on, rcvr), 1000L, 80L, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(getCommand(off, rcvr), 1050L, 80L, TimeUnit.MILLISECONDS);
        
        while (true) {}
    }
    
    protected static Runnable getCommand(final ShortMessage m, final Receiver r) {
        return (new Runnable() {
            public void run() {
                r.send(m, -1);
            }
        });
    }
}