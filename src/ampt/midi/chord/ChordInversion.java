package ampt.midi.chord;

/**
 * Represents varios chord inversions, ie which tone is the bottom note in the
 * chord.
 * 
 */
public enum ChordInversion {

    ROOT_POSITION(0, 0, 0),
    FIRST_INVERSION(12, 0, 0),
    SECOND_INVERSION(12, 12, 0);
    private int rootInterval;
    private int thirdInterval;
    private int fifthInterval;

    ChordInversion(int rootInterval, int thirdInterval, int fifthInterval) {
        this.rootInterval = rootInterval;
        this.thirdInterval = thirdInterval;
        this.fifthInterval = fifthInterval;
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
}
