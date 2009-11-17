package ampt.midi.note;

import javax.sound.midi.Sequence;

/**
 *
 * @author Robert
 */
public enum NoteValue {
    SIXTYFOURTH_NOTE(16F),
    THIRTYSECOND_NOTE(8F),
    SIXTEENTH_NOTE(4F),
    EIGHTH_NOTE(2F),
    QUARTER_NOTE(1F),
    HALF_NOTE(.5F),
    WHOLE_NOTE(.25F);

    float notesPerBeat;

    NoteValue(float notesPerBeat) {
        this.notesPerBeat = notesPerBeat;
    }

    public static long getTickLength(NoteValue value, float divisionType, int resolution) {
        if (divisionType == Sequence.PPQ)
            return (long)(resolution / value.notesPerBeat);
        if (divisionType == Sequence.SMPTE_24)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_25)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_30)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_30DROP)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        return -1;
    }
}