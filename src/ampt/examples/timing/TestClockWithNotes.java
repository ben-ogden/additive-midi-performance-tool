package ampt.examples.timing;

import ampt.core.time.Clock;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Comparison of scheduling notes using the various clocks we have available.
 * The notes are comparable to 16th notes at 120 bpm.
 *
 * <p>All of the results sound about the same. The timing is not great.
 *
 * @author robert
 */
public class TestClockWithNotes {
    public static void main(String args[])
            throws MidiUnavailableException, InvalidMidiDataException,
            InterruptedException {

        long timeStamp;
        long tickLength;
        long nextTick;
        int tickCount = 0;

        Clock clock;
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Receiver rcvr = synth.getReceiver();
        ShortMessage msg1 = new ShortMessage();
        ShortMessage msg2 = new ShortMessage();
        msg1.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        msg2.setMessage(ShortMessage.NOTE_OFF, 0, 60, 93);

        clock = Clock.getInstance(Clock.CLOCK_TYPE_STANDARD);
        tickLength = 125; //standard clock is in millis
        timeStamp = clock.getTime();
        //calculate the first tick
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            //get current time
            timeStamp = clock.getTime();
            //is it time to tick?
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                //calculate next tick
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }

        Thread.sleep(2000);

        clock = Clock.getInstance(Clock.CLOCK_TYPE_NANO);
        tickLength = 125000000; //nano clock is in nanos
        //reset ticker
        tickCount = 0;
        timeStamp = clock.getTime();
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            //get current time
            timeStamp = clock.getTime();
            //is it time to tick?
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                //calculate next tick
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }


        Thread.sleep(2000);

        clock = Clock.getInstance();
        tickLength = 125000; //native clock is in micros
        //reset ticker
        tickCount = 0;
        timeStamp = clock.getTime();
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            //get current time
            timeStamp = clock.getTime();
            //is it time to tick?
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                //calculate next tick
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }

    }
}
