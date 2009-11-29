package ampt.midi.chord;

/**
 * Represents varios chord inversions, ie which tone is the bottom note in the
 * chord.
 * 
 */
public enum ChordInversion {

    ROOT_POSITION(0, 0, 0, "Root Position"),
    FIRST_INVERSION(12, 0, 0, "First Inversion"),
    SECOND_INVERSION(12, 12, 0, "Second Inversion");

    private final int rootInterval;
    private final int thirdInterval;
    private final int fifthInterval;
    private final String name;

    ChordInversion(int rootInterval, int thirdInterval, int fifthInterval, 
            String name) {
        this.rootInterval = rootInterval;
        this.thirdInterval = thirdInterval;
        this.fifthInterval = fifthInterval;
        this.name = name;
    }

    public int getRootInterval() {
        return rootInterval;
    }

    public int getThirdInterval() {
        return thirdInterval;
    }

    public int getFifthInterval() {
        return fifthInterval;
    }

    @Override
    public String toString() {
        return name;
    }


}
