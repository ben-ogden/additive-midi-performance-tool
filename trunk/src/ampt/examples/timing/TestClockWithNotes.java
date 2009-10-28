package ampt.examples.timing;

import ampt.core.time.Clock;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
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
        tickLength = 125;
        timeStamp = clock.getTime();
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            timeStamp = clock.getTime();
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }

        Thread.sleep(2000);

        clock = Clock.getInstance(Clock.CLOCK_TYPE_NANO);
        tickLength = 125000000;
        tickCount = 0;
        timeStamp = clock.getTime();
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            timeStamp = clock.getTime();
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }

        Thread.sleep(2000);

        clock = Clock.getInstance();
        tickLength = 125000;
        tickCount = 0;
        timeStamp = clock.getTime();
        nextTick = timeStamp + tickLength;
        while (tickCount <= 30) {
            timeStamp = clock.getTime();
            if (timeStamp >= nextTick) {
                rcvr.send(msg1, -1);
                rcvr.send(msg2, -1);
                nextTick = timeStamp + tickLength;
                tickCount++;
            }
        }
    }
}
