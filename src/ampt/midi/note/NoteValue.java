package ampt.midi.note;

import javax.sound.midi.Sequence;

/**
 * For getting tick values of notes types for a given tempo
 * 
 * @author Robert Szewczyk
 */
public enum NoteValue {
    SIXTYFOURTH_NOTE(16F, "Sixty-Fourth"),
    THIRTYSECOND_NOTE(8F, "Thirty-Second"),
    SIXTEENTH_NOTE(4F, "Sixteenth"),
    EIGHTH_NOTE(2F, "Eighth"),
    QUARTER_NOTE(1F, "Quarter"),
    HALF_NOTE(.5F, "Half"),
    WHOLE_NOTE(.25F, "Whole");

    float _notesPerBeat;
    String _name;

    NoteValue(float notesPerBeat, String name) {
        _notesPerBeat = notesPerBeat;
        _name = name;
    }

    public float getNotesPerBeat() {
        return _notesPerBeat;
    }

    @Override
    public String toString() {
        return _name;
    }

    public static long getTickLength(NoteValue value, float divisionType, int resolution) {
        if (divisionType == Sequence.PPQ)
            return (long)(resolution / value._notesPerBeat);
        if (divisionType == Sequence.SMPTE_24)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_25)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_30)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");
        if (divisionType == Sequence.SMPTE_30DROP)
            throw new UnsupportedOperationException("SMPTE Timing Not Yet Supported");

        throw new IllegalArgumentException("Division Type is Not Legal");
    }

    public static long getTickLength(NoteValue value, int resolution) {
        return (long) (resolution / value._notesPerBeat);
    }
}