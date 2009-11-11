package ampt.examples.timing;

import ampt.core.time.Clock;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * This test plays a scale to a given receiver. See HarwareTimingTest for a
 * user interface to this test.
 * 
 * @author Ben
 */
public class SoundTimingTest {

    public void playNotesAtInterval(Receiver rcvr, int midiChannel, int noteInterval, int noteLength) throws InvalidMidiDataException {

        ShortMessage msg1 = new ShortMessage();
        ShortMessage msg2 = new ShortMessage();

        int note = 59;
        int sign = 1;
        msg1.setMessage(ShortMessage.NOTE_ON, midiChannel, note, 63);
        msg2.setMessage(ShortMessage.NOTE_OFF, midiChannel, note, 63);

        long natElapsed = 0;
        long last = 0;
        Clock clock = Clock.getInstance();
        // record time starting
        clock.stampTime();

        boolean noteOn = false;

        while (natElapsed < 100000) {

            natElapsed = clock.getElapsedTime(Clock.UNIT_MILLIS);

            // turn note on
            if (!noteOn && natElapsed - last >= noteInterval) {

                last = natElapsed;

                // play note
                rcvr.send(msg1, -1);
                noteOn = true;

                //System.out.println(natElapsed);

                // give other threads a chance to run
                Thread.yield();

            }

            // turn note off
            if (noteOn && natElapsed - last >= noteLength) {
                rcvr.send(msg2, -1);
                noteOn = false;

                if (note > 71) {
                    sign = -1;
                } else if (note < 59) {
                    sign = 1;
                }
                note += sign;


                msg1.setMessage(ShortMessage.NOTE_ON, midiChannel, note, 63);
                msg2.setMessage(ShortMessage.NOTE_OFF, midiChannel, note, 63);
            }

        }
    }

    
    public static void main(String[] args) throws Exception {

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();

        SoundTimingTest test = new SoundTimingTest();
        test.playNotesAtInterval(rcvr, 7, 120, 60);

    }
}
