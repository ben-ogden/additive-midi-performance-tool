package ampt.midi.note;

/**
 *
 * @author Rob
 */
public final class Arpeggio {

    public final static int ASCEND = 0;
    public final static int DESCEND = 1;
    public final static int ASCEND_DESCEND = 2;
    public final static int DESCEND_ASCEND = 3;
    public final static int RANDOM = 4;

    public final static int MAJOR = 0;
    public final static int MINOR = 1;
    public final static int AUGMENTED = 2;
    public final static int DIMINISHED = 3;

    private final int[] _intervals;
    private final NoteValue _noteValue;
    private final int _noteCount;

    private Arpeggio(int motion, int arpType, NoteValue noteValue) {
        _noteValue = noteValue;

        switch (motion) {
            case ASCEND:
                _intervals = new int[4];
                _noteCount = 4;

                try {
                    _intervals[0] = IntervalQuality.PERFECT.unison();
                    _intervals[3] = IntervalQuality.PERFECT.octave();

                    if (arpType == MAJOR) {
                        _intervals[1] = IntervalQuality.MAJOR.third();
                        _intervals[2] = IntervalQuality.PERFECT.fifth();
                    }

                    if (arpType == MINOR) {
                        _intervals[1] = IntervalQuality.MINOR.third();
                        _intervals[2] = IntervalQuality.PERFECT.fifth();
                    }

                    if (arpType == AUGMENTED) {
                        _intervals[1] = IntervalQuality.MAJOR.third();
                        _intervals[2] = IntervalQuality.AUGMENTED.fifth();
                    }

                    if (arpType == DIMINISHED) {
                        _intervals[1] = IntervalQuality.MINOR.third();
                        _intervals[2] = IntervalQuality.DIMINISHED.fifth();
                    }

                } catch (InvalidIntervalException e) {
                    e.printStackTrace();
                }

                break;

            case DESCEND:
                _intervals = new int[4];
                _noteCount = 4;

                try {
                    _intervals[0] = IntervalQuality.PERFECT.unison();
                    _intervals[3] = IntervalQuality.PERFECT.unison() - 12;

                    if (arpType == MAJOR) {
                        _intervals[2] = IntervalQuality.MAJOR.third() - 12;
                        _intervals[1] = IntervalQuality.PERFECT.fifth() - 12 ;
                    }

                    if (arpType == MINOR) {
                        _intervals[2] = IntervalQuality.MINOR.third() - 12;
                        _intervals[1] = IntervalQuality.PERFECT.fifth() - 12;
                    }

                    if (arpType == AUGMENTED) {
                        _intervals[2] = IntervalQuality.MAJOR.third() - 12;
                        _intervals[1] = IntervalQuality.AUGMENTED.fifth() - 12;
                    }

                    if (arpType == DIMINISHED) {
                        _intervals[2] = IntervalQuality.MINOR.third() - 12;
                        _intervals[1] = IntervalQuality.DIMINISHED.fifth() - 12;
                    }

                } catch (InvalidIntervalException e) {
                    e.printStackTrace();
                }

                break;

            case ASCEND_DESCEND:
                _intervals = new int[6];
                _noteCount = 6;

                try {
                    _intervals[0] = IntervalQuality.PERFECT.unison();
                    _intervals[3] = IntervalQuality.PERFECT.octave();

                    if (arpType == MAJOR) {
                        _intervals[1] = IntervalQuality.MAJOR.third();
                        _intervals[2] = IntervalQuality.PERFECT.fifth();
                        _intervals[4] = IntervalQuality.PERFECT.fifth();
                        _intervals[5] = IntervalQuality.MAJOR.third();
                    }

                    if (arpType == MINOR) {
                        _intervals[1] = IntervalQuality.MINOR.third();
                        _intervals[2] = IntervalQuality.PERFECT.fifth();
                        _intervals[4] = IntervalQuality.PERFECT.fifth();
                        _intervals[5] = IntervalQuality.MINOR.third();
                    }

                    if (arpType == AUGMENTED) {
                        _intervals[1] = IntervalQuality.MAJOR.third();
                        _intervals[2] = IntervalQuality.AUGMENTED.fifth();
                        _intervals[4] = IntervalQuality.AUGMENTED.fifth();
                        _intervals[5] = IntervalQuality.MAJOR.third();
                    }

                    if (arpType == DIMINISHED) {
                        _intervals[1] = IntervalQuality.MINOR.third();
                        _intervals[2] = IntervalQuality.DIMINISHED.fifth();
                        _intervals[4] = IntervalQuality.DIMINISHED.fifth();
                        _intervals[5] = IntervalQuality.MINOR.third();
                    }

                } catch (InvalidIntervalException e) {
                    e.printStackTrace();
                }

                break;

            case DESCEND_ASCEND:
                _intervals = new int[6];
                _noteCount = 6;

                try {
                    _intervals[0] = IntervalQuality.PERFECT.unison();
                    _intervals[3] = IntervalQuality.PERFECT.unison() - 12;

                    if (arpType == MAJOR) {
                        _intervals[2] = IntervalQuality.MAJOR.third() - 12;
                        _intervals[1] = IntervalQuality.PERFECT.fifth() - 12;
                        _intervals[5] = IntervalQuality.PERFECT.fifth() - 12;
                        _intervals[4] = IntervalQuality.MAJOR.third() - 12;
                    }

                    if (arpType == MINOR) {
                        _intervals[2] = IntervalQuality.MINOR.third() - 12;
                        _intervals[1] = IntervalQuality.PERFECT.fifth() - 12;
                        _intervals[5] = IntervalQuality.PERFECT.fifth() - 12;
                        _intervals[4] = IntervalQuality.MINOR.third() - 12;
                    }

                    if (arpType == AUGMENTED) {
                        _intervals[2] = IntervalQuality.MAJOR.third() - 12;
                        _intervals[1] = IntervalQuality.AUGMENTED.fifth() - 12;
                        _intervals[5] = IntervalQuality.AUGMENTED.fifth() - 12;
                        _intervals[4] = IntervalQuality.MAJOR.third() - 12;
                    }

                    if (arpType == DIMINISHED) {
                        _intervals[2] = IntervalQuality.MINOR.third() - 12;
                        _intervals[1] = IntervalQuality.DIMINISHED.fifth() - 12;
                        _intervals[5] = IntervalQuality.DIMINISHED.fifth() - 12;
                        _intervals[4] = IntervalQuality.MINOR.third() - 12;
                    }

                } catch (InvalidIntervalException e) {
                    e.printStackTrace();
                }

                break;

            case RANDOM:
            default:
                _intervals = null;
                _noteCount = 0;
                break;
        }
    }

    public int[] getIntervals() {
        int[] _temp = new int[_noteCount];
        System.arraycopy(_intervals, 0, _temp, 0, _noteCount);
        return _temp;
    }

    public NoteValue getNoteValue() {
        return _noteValue;
    }

    public int getNoteCount() {
        return _noteCount;
    }

    public static Arpeggio newArpeggio(int motion, int arpType, NoteValue noteValue) {
        return (new Arpeggio(motion, arpType, noteValue));
    }
}
