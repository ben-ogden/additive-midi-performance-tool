package examples.timing;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Example that uses the Mentronome class. It creates a series of notes, each
 * in its own thread and schedules them. In this case, the notes are 32nd notes
 * at a tempo of 60bpm (or it can be looked at as 16th notes at 120bpm).
 *
 * <p>Note: this is not the only way to use the Metronome. It could also
 * schedule a series of notes from a single thread using a loop. Will add that
 * in a future example - but more important to find source of timing errors.
 *
 * <p>The results are ok, but not great. There are definitely some timing
 * issues. Will have to test further to determine if it because of the timer
 * itself, or because of some overhead involved in waiting and resuming threads.
 *
 * @author robert
 */
public class UseMetronome {

    public static void main(String args[])
            throws MidiUnavailableException, InvalidMidiDataException,
            InterruptedException {
        final Metronome met = new Metronome(60);
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        final Receiver rcvr = synth.getReceiver();
        final ShortMessage msg1 = new ShortMessage();
        final ShortMessage msg2 = new ShortMessage();
        msg1.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        msg2.setMessage(ShortMessage.NOTE_OFF, 0, 60, 93);


        met.start();

        int tickCount = met.getTickCount();
        for(int i = 0; i < 80; i++) {
            {
                final int t = tickCount;
                final int j = i;
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        met.play(t + j * 4);
                        rcvr.send(msg1, -1);
                        rcvr.send(msg2, -1);
                    }
                })).start();
            }
        }
        Thread.sleep(12000);
        met.stop();
    }
}
