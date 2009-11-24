package ampt.midi.chord;

/**
 * Represents the note offsets for various chord types.
 *
 */
public enum ChordType {

    MAJOR(4, 7),
    MINOR(3, 7),
    AUGMENTED(3, 6),
    DIMINISHED(4, 8);
    private final int thirdInterval;
    private final int fifthInterval;

    ChordType(int third, int fifth) {
        this.thirdInterval = third;
        this.fifthInterval = fifth;
    }

    public int getThirdInterval() {
        return thirdInterval;
    }

    public int getFifthInterval() {
        return fifthInterval;
    }
};
