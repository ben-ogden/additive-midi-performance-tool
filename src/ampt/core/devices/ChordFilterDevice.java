package ampt.core.devices;

import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import ampt.midi.chord.ChordType;
import ampt.midi.chord.ChordInversion;
import java.util.ArrayList;

/**
 * This is an implementation of a filter that creates a chord.  In this 
 * implementation, the class implements MidiDevice, allowing it to be plugged-in
 * to the MidiSystem.
 * 
 * This class contains inner classes which represent it's receivers and 
 * transmitters.
 *
 * @author Christopher
 */
public class ChordFilterDevice extends AmptDevice {

    public static final String DEVICE_NAME = "Chord Filter";
    private static final String DEVICE_DESCRIPTION = "Creates a chord from a single note";

    private ChordType chordType;
    private ChordInversion chordInversion;


    public ChordFilterDevice() {

        super(DEVICE_NAME, DEVICE_DESCRIPTION);

        // set the default chord and inversion
        chordType = ChordType.MAJOR;
        chordInversion = ChordInversion.ROOT_POSITION;
    }

    public void setChordType(ChordType chordType){
        this.chordType = chordType;
    }

    public void setChordInversion(ChordInversion chordInversion){
        this.chordInversion = chordInversion;
    }

    @Override
    protected void initDevice() {
        // nothing to do
    }

    @Override
    protected void closeDevice() {
        // nothing to do
    }

    /**
     * Returns a new Chord Filter Receiver that is not yet bound to any
     * transmitters.
     *
     * @return a new ChordFilterReceiver
     */
    @Override
    protected Receiver getAmptReceiver() {
        return new ChordFilterReceiver();
    }
    
    /**
     * Inner class that implements a receiver for a chord filter.  This is
     * where all of the actual filtering takes place.
     */
    public class ChordFilterReceiver extends AmptReceiver {

        List<MidiMessage> messages = new ArrayList<MidiMessage>();


        @Override
        protected List<MidiMessage> filter(MidiMessage message, long timeStamp) {

            messages.clear();

            ShortMessage third = null;
            ShortMessage fifth = null;

            // If the message is not a short message, we don't know how to deal
            // with it now.
            if (message instanceof ShortMessage) {
                ShortMessage root = (ShortMessage) message;
                // We only care about note on and note off messages.
                if (root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF) {

                    try {
                        root.setMessage(root.getCommand(), root.getChannel(), root.getData1() + chordInversion.getRootInterval(), root.getData2());
                        messages.add(root);
                    } catch (InvalidMidiDataException ex){
                        // fail silently?
                    }

                    // Create the note that makes the third of the chord.
                    third = new ShortMessage();
                    // Try to make the message, if it fails, then the note for
                    // the third is probably not supported.
                    try {
                        third.setMessage(root.getCommand(), root.getChannel(), root.getData1() + chordType.getThirdInterval() + chordInversion.getThirdInterval(), root.getData2());
                        messages.add(third);
                    } catch (InvalidMidiDataException ex) {
                        // fail silently?
                    }
                    // Try to make the message, if it fails, then the note for
                    // the fifth is probably not supported.
                    fifth = new ShortMessage();
                    try {
                        fifth.setMessage(root.getCommand(), root.getChannel(), root.getData1() + chordType.getFifthInterval() + chordInversion.getFifthInterval(), root.getData2());
                        messages.add(fifth);
                    } catch (InvalidMidiDataException ex) {
                        // fail silently?
                    }
                }
            }
            return messages;
        }
    }

}