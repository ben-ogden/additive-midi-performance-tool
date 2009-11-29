package ampt.midi.chord;

/**
 * Represents the note offsets for various chord types.
 *
 */
public enum ChordType {

    MAJOR(4, 7, "Major"),
    MINOR(3, 7, "Minor"),
    AUGMENTED(3, 6, "Augmented"),
    DIMINISHED(4, 8, "Diminished");

    private final int thirdInterval;
    private final int fifthInterval;
    private final String name;

    ChordType(int third, int fifth, String name) {
        this.thirdInterval = third;
        this.fifthInterval = fifth;
        this.name = name;
    }

    public int getThirdInterval() {
        return thirdInterval;
    }

    public int getFifthInterval() {
        return fifthInterval;
    }

    public String toString() {
        return name;
    }
    
};
