package ampt.midi.note;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;

/**
 * This class aides in the construction of a MIDI sequence.
 *
 * TODO Add more note types
 * 
 * @author Robert Szewczyk
 */
public class SequenceBuilder {

    private Sequence sequence;
    long[] tickPositions;
    int currentTrack;
    float divisionType;
    int resolution;

    /*
     * divisionType should be one of those specified in Sequence. If PPQ is the division type,
     * resolution is in ticks per beat. If SMPTPE is selected, it is in clicks per frame.
     *
     * @param the division type (SMTPE, PPQ, ETC)
     * @resolution ticks per frame or beat
     */
    public SequenceBuilder(float divisionType, int resolution) {
        this.divisionType = divisionType;
        this.resolution = resolution;

        if (divisionType != Sequence.PPQ)
            throw new UnsupportedOperationException("Only PPQ Timing Supported as of This Time");
        try {
            sequence = new Sequence(divisionType, resolution);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        sequence.createTrack();
        tickPositions = new long[1];
        tickPositions[0] = 0;
        currentTrack = 0;
    }

    /*
     * Helper method used by the addNote methods to create a NOTE_ON ShortMessage
     */
    private ShortMessage noteOn(int channel, int value, int velocity) {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(ShortMessage.NOTE_ON, channel, value, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /*
     * Helper method used by the addNote methods to create a NOTE_OFF ShortMessage
     */
    private ShortMessage noteOff(int channel, int value, int velocity) {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(ShortMessage.NOTE_OFF, channel, value, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void addNote(NoteValue noteValue, int channel, int tone, int velocity) {
        MidiEvent event = new MidiEvent(noteOn(channel, tone, velocity), tickPositions[currentTrack]);
        (sequence.getTracks())[currentTrack].add(event);
        tickPositions[currentTrack] += NoteValue.getTickLength(noteValue, divisionType, resolution);
        event = new MidiEvent(noteOff(channel, tone, velocity), tickPositions[currentTrack] - 1);
        (sequence.getTracks())[currentTrack].add(event);
    }

    public void addRest(NoteValue noteValue) {
        tickPositions[currentTrack] += NoteValue.getTickLength(noteValue, divisionType, resolution);
    }

    /*
     * Returns an instance of the sequence
     *
     * @return the sequence associated with this builder
     */
    public Sequence getSequence() {
        return sequence;
    }

    /*
     * Adds a track to the sequence
     *
     */
    public void addTrack() {
        sequence.createTrack();
        long[] temp = new long[tickPositions.length + 1];
        for(int i = 0; i < tickPositions.length; i++)
            temp[i] = tickPositions[i];
        temp[tickPositions.length] = 0;
        tickPositions = temp;
    }

    /*
     * Switches the current track
     *
     * @param the desired track
     */
    public void switchTrack(int currentTrack) {
        this.currentTrack = currentTrack;
    }

    /*
     * Returns the number of tracks in the sequence
     *
     * @return the number of tracks in the sequence
     */
    public int trackCount() {
        return sequence.getTracks().length;
    }
}
