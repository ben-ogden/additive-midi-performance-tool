package ampt.midi.note;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;
import javax.sound.midi.MetaEventListener;

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

    /*
     * Adds a quarter note to the currently selected track
     *
     * @param channel the channel over which the note is to be played
     * @param value the tone value of the note
     * @param velocity velocity of the note
     */
    public void addQuarterNote(int channel, int value, int velocity) {
        MidiEvent event = new MidiEvent(noteOn(channel, value, velocity), tickPositions[currentTrack]);
        (sequence.getTracks())[currentTrack].add(event);
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.QUARTER_NOTE, divisionType, resolution);
        event = new MidiEvent(noteOff(channel, value, velocity), tickPositions[currentTrack] - 1);
        (sequence.getTracks())[currentTrack].add(event);
    }

    /*
     * Adds an eighth note to the currently selected track
     *
     * @param channel the channel over which the note is to be played
     * @param value the tone value of the note
     * @param velocity velocity of the note
     */
    public void addEighthNote(int channel, int value, int velocity) {
        MidiEvent event = new MidiEvent(noteOn(channel, value, velocity), tickPositions[currentTrack]);
        (sequence.getTracks())[currentTrack].add(event);
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.EIGHTH_NOTE, divisionType, resolution);
        event = new MidiEvent(noteOff(channel, value, velocity), tickPositions[currentTrack] - 1);
        tickPositions[currentTrack]++;
        (sequence.getTracks())[currentTrack].add(event);
    }

    /*
     * Adds a sixteenth note to the currently selected track
     *
     * @param channel the channel over which the note is to be played
     * @param value the tone value of the note
     * @param velocity velocity of the note
     */
    public void addSixteenthNote(int channel, int value, int velocity) {
        MidiEvent event = new MidiEvent(noteOn(channel, value, velocity), tickPositions[currentTrack]);
        (sequence.getTracks())[currentTrack].add(event);
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.SIXTEENTH_NOTE, divisionType, resolution);
        event = new MidiEvent(noteOff(channel, value, velocity), tickPositions[currentTrack] - 1);
        (sequence.getTracks())[currentTrack].add(event);
    }

    /*
     * Adds a quarter note value rest to the currently selected track
     *
     */
    public void addQuarterRest() {
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.QUARTER_NOTE, divisionType, resolution);
    }

    /*
     * Adds an eighth note value rest to the currently selected track
     *
     */
    public void addEighthRest() {
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.EIGHTH_NOTE, divisionType, resolution);
    }

    /*
     * Adds a sixteenth note value rest to the currently selected track
     *
     */
    public void addSixteenthRest() {
        tickPositions[currentTrack] += NoteValue.getTickLength(NoteValue.SIXTEENTH_NOTE, divisionType, resolution);
    }

    public void addBpmTempoChange(float bpm) {
        MetaMessage msg = new MetaMessage();
        long mpq = (long)(6000000F / bpm);
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
