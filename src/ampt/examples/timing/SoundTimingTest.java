package ampt.examples.timing;

import ampt.core.time.Clock;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 *
 * @author Ben
 */
public class SoundTimingTest {

    public static void main(String[] args) throws Exception {

                
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage msg1 = new ShortMessage();
        ShortMessage msg2 = new ShortMessage();
        int note = 59;
        int sign = 1;
        msg1.setMessage(ShortMessage.NOTE_ON, 7, note, 63);
        msg2.setMessage(ShortMessage.NOTE_OFF, 7, note, 63);


        long natElapsed = 0;
        long last = 0;
        Clock clock = Clock.getInstance();
        // record time starting
        clock.stampTime();

        boolean noteOn = false;

        while(natElapsed < 100000) {

            natElapsed = clock.getElapsedTime(Clock.UNIT_MILLIS);


            // turn note on
            if(!noteOn && natElapsed-last >= 120) {

                last = natElapsed;

                // play note
                rcvr.send(msg1, -1);
                noteOn = true;

                //System.out.println(natElapsed);

                // give other threads a chance to run
                Thread.yield();

            }

            // turn note off
            if(noteOn && natElapsed-last >= 60) {
                rcvr.send(msg2, -1);
                noteOn = false;

                if(note > 71) {
                    sign = -1;
                } else if (note < 59) {
                    sign = 1;
                }
                note += sign;


                msg1.setMessage(ShortMessage.NOTE_ON, 7, note, 63);
                msg2.setMessage(ShortMessage.NOTE_OFF, 7, note, 63);
            }


        }
    }
}
